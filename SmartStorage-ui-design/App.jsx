/* SmartStorage — App controller: nav, state, upload pipeline, tweaks, chrome */

const TWEAK_DEFAULTS = /*EDITMODE-BEGIN*/{
  "tileStyle": "card",
  "density": "comfortable",
  "accent": "#A069FF",
  "dark": false
}/*EDITMODE-END*/;

const ACCENT_OPTS = ["#A069FF", "#2A7DE1", "#13A36B", "#E0892A"];

// fake-extracted values for newly uploaded docs, per section
const DEMO_FILL = {
  sec_invoices: { "Invoice No": "INV-2049", Vendor: "Cyberdyne Systems", "Issue Date": "2026-04-02", "Due Date": "2026-05-02", Amount: 9325.0, Tax: 1958.25 },
  sec_contracts: { "Contract ID": "C-1005", Counterparty: "Cyberdyne Systems", "Effective Date": "2026-04-01", "Term (mo)": 18, "Annual Value": 96000 },
  sec_candidates: { Email: "sven.eriksson@mail.com", "Full Name": "Sven Eriksson", "Role Applied": "Frontend Engineer", "Years Exp": 5, Applied: "2026-06-01" },
  sec_receipts: { "Receipt ID": "R-5501", Merchant: "Blue Bottle Coffee", Date: "2026-06-03", Category: "Meals", Total: 18.4 },
  sec_papers: { DOI: "10.1101/2026.06.204", Title: "Structured Extraction at Scale", "Lead Author": "P. Nguyen", Year: 2026 },
};

function TopBar({ t, setTweak, query, setQuery, onHome, showSearch }) {
  return (
    <header style={{ position: "sticky", top: 0, zIndex: 40, background: "color-mix(in oklch, var(--bg) 86%, transparent)",
      backdropFilter: "saturate(1.4) blur(12px)", borderBottom: "1px solid var(--border)" }}>
      <div style={{ maxWidth: 1320, margin: "0 auto", padding: "0 22px", height: 58, display: "flex", alignItems: "center", gap: 16 }}>
        <button onClick={onHome} style={{ display: "flex", alignItems: "center", gap: 10, background: "transparent", border: "none", cursor: "pointer", padding: 0 }}>
          <div style={{ width: 30, height: 30, borderRadius: 9, background: "var(--accent)", display: "grid", placeItems: "center",
            boxShadow: "0 1px 3px color-mix(in oklch, var(--accent) 50%, transparent), inset 0 1px 0 rgba(255,255,255,.25)" }}>
            <Icon name="layers" size={17} sw={1.8} style={{ color: "#fff" }} />
          </div>
          <span style={{ fontSize: 15.5, fontWeight: 660, color: "var(--text)", letterSpacing: "-.02em" }}>SmartStorage</span>
        </button>

        <div style={{ flex: 1 }} />

        {showSearch && (
          <div style={{ position: "relative", width: 240 }}>
            <Icon name="search" size={15} style={{ position: "absolute", left: 11, top: "50%", transform: "translateY(-50%)", color: "var(--text-4)" }} />
            <input value={query} onChange={(e) => setQuery(e.target.value)} placeholder="Search sections…"
              style={{ width: "100%", fontFamily: "var(--ui)", fontSize: 13.5, color: "var(--text)", background: "var(--field)", outline: "none",
                border: "1px solid var(--border)", borderRadius: 10, padding: "8px 12px 8px 33px" }} />
          </div>
        )}

        <button onClick={() => setTweak("dark", !t.dark)} title="Toggle theme"
          style={{ width: 36, height: 36, display: "grid", placeItems: "center", borderRadius: 10, cursor: "pointer",
            border: "1px solid var(--border)", background: "var(--surface)", color: "var(--text-2)" }}>
          <Icon name={t.dark ? "sun" : "moon"} size={17} />
        </button>
      </div>
    </header>
  );
}

function App() {
  const [t, setTweak] = useTweaks(TWEAK_DEFAULTS);
  const [sections, setSections] = React.useState(window.SS.sections);
  const [itemsBySection, setItems] = React.useState(window.SS.items);
  const [nav, setNav] = React.useState({ screen: "home", sectionId: null, itemId: null });
  const [homeQuery, setHomeQuery] = React.useState("");
  const [tableQuery, setTableQuery] = React.useState("");
  const [createOpen, setCreateOpen] = React.useState(false);
  const [confirm, setConfirm] = React.useState(null);
  const [toasts, setToasts] = React.useState([]);

  // theme + accent application
  React.useEffect(() => {
    document.documentElement.setAttribute("data-theme", t.dark ? "dark" : "light");
  }, [t.dark]);
  React.useEffect(() => {
    document.documentElement.style.setProperty("--accent", t.accent);
  }, [t.accent]);

  const pushToast = (toast) => {
    const id = window.SS.uid("t");
    setToasts((ts) => [...ts, { id, ...toast }]);
    if (toast.ttl !== 0) setTimeout(() => dismissToast(id), toast.ttl || 4200);
    return id;
  };
  const dismissToast = (id) => setToasts((ts) => ts.filter((x) => x.id !== id));
  const updateToast = (id, patch) => setToasts((ts) => ts.map((x) => (x.id === id ? { ...x, ...patch } : x)));

  const go = (screen, extra = {}) => { setNav({ screen, sectionId: null, itemId: null, ...extra }); window.scrollTo({ top: 0 }); };

  const requestConfirm = (opts) => setConfirm(opts);

  const deleteSection = (sectionId) => {
    const sec = sections.find((s) => s.id === sectionId);
    const count = (itemsBySection[sectionId] || []).length;
    requestConfirm({
      title: `Delete \u201c${sec.name}\u201d?`,
      body: count > 0
        ? `This section contains ${count} item${count === 1 ? "" : "s"}. All data and source files will be permanently removed. This cannot be undone.`
        : `Permanently remove the \u201c${sec.name}\u201d section? This cannot be undone.`,
      confirmLabel: "Delete section",
      onConfirm: () => {
        setSections((s) => s.filter((x) => x.id !== sectionId));
        setItems((m) => { const n = { ...m }; delete n[sectionId]; return n; });
        if (nav.sectionId === sectionId) go("home");
        pushToast({ title: "Section deleted", tone: "ok", icon: "trash" });
      },
    });
  };

  const deleteItem = (sectionId, itemId) => {
    const sec = sections.find((s) => s.id === sectionId);
    const it = (itemsBySection[sectionId] || []).find((i) => i.id === itemId);
    const idAttr = sec.attributes.find((a) => a.identifier);
    const label = (idAttr && it?.metadata[idAttr.name]) ? String(it.metadata[idAttr.name]) : (it?.name || "this item");
    requestConfirm({
      title: "Delete item?",
      body: `Remove \u201c${label}\u201d from ${sec.name}? This cannot be undone.`,
      confirmLabel: "Delete",
      onConfirm: () => {
        setItems((m) => ({ ...m, [sectionId]: (m[sectionId] || []).filter((i) => i.id !== itemId) }));
        if (nav.itemId === itemId) go("section", { sectionId });
        pushToast({ title: "Item deleted", body: `Removed from ${sec.name}`, tone: "ok", icon: "trash" });
      },
    });
  };

  const saveItem = (sectionId, itemId, newMetadata) => {
    const sec = sections.find((s) => s.id === sectionId);
    const idAttr = sec.attributes.find((a) => a.identifier);
    const newName = (idAttr && newMetadata[idAttr.name] != null) ? String(newMetadata[idAttr.name]) : undefined;
    setItems((m) => ({
      ...m,
      [sectionId]: (m[sectionId] || []).map((i) => {
        if (i.id !== itemId) return i;
        return { ...i, metadata: newMetadata, ...(newName ? { name: newName } : {}) };
      }),
    }));
    pushToast({ title: "Changes saved", tone: "ok", icon: "checkCircle" });
  };

  const openSection = (sectionId) => { setTableQuery(""); go("section", { sectionId }); };
  const openItem = (itemId) => go("item", { sectionId: nav.sectionId, itemId });

  const createSection = (def) => {
    const id = window.SS.uid("sec");
    const accents = Object.values(window.SS.ACCENT);
    const sec = { id, name: def.name, icon: "folder", accent: accents[sections.length % accents.length], attributes: def.attributes };
    setSections((s) => [...s, sec]);
    setItems((m) => ({ ...m, [id]: [] }));
    setCreateOpen(false);
    pushToast({ title: "Section created", body: `“${def.name}” is ready for documents.`, tone: "ok", icon: "checkCircle" });
    openSection(id);
  };

  // upload pipeline: add pending item -> resolve to data after delay
  const uploadDocs = (sectionId, fileNames) => {
    const section = sections.find((s) => s.id === sectionId);
    const itemId = window.SS.uid("itm");
    const willFail = Math.random() < 0.18; // occasional failed extraction for realism
    const pendingItem = {
      id: itemId, name: "New upload", _pending: true,
      metadata: {},
      attachments: fileNames.map((fn) => ({ id: window.SS.uid("att"), fileName: fn, status: "PROCESSING" })),
    };
    setItems((m) => ({ ...m, [sectionId]: [pendingItem, ...(m[sectionId] || [])] }));

    const tId = pushToast({ title: "Processing document", body: `Extracting ${fileNames.length} file${fileNames.length === 1 ? "" : "s"} into ${section.name}…`, tone: "accent", icon: "spinner", spin: true, ttl: 0 });

    setTimeout(() => {
      setItems((m) => {
        const list = (m[sectionId] || []).map((it) => {
          if (it.id !== itemId) return it;
          if (willFail) {
            return { ...it, _pending: false, name: fileNames[0].replace(/\.pdf$/i, ""), metadata: {}, attachments: it.attachments.map((a) => ({ ...a, status: "FAILED" })) };
          }
          const fill = DEMO_FILL[sectionId] || {};
          const idAttr = section.attributes.find((a) => a.identifier);
          const name = idAttr && fill[idAttr.name] != null ? String(fill[idAttr.name]) : fileNames[0].replace(/\.pdf$/i, "");
          return { ...it, _pending: false, name, metadata: fill, attachments: it.attachments.map((a) => ({ ...a, status: "COMPLETED" })) };
        });
        return { ...m, [sectionId]: list };
      });
      if (willFail) {
        updateToast(tId, { title: "Extraction failed", body: `Couldn't read ${fileNames[0]}. Try re-uploading.`, tone: "error", icon: "alert", spin: false });
      } else {
        updateToast(tId, { title: "Document processed", body: `New item added to ${section.name}.`, tone: "ok", icon: "checkCircle", spin: false });
      }
      setTimeout(() => dismissToast(tId), 4200);
    }, 3400);
  };

  const downloadFile = (att) => {
    // simulate a download of the original PDF
    const blob = new Blob([`%PDF-1.4\n% SmartStorage sample — ${att.fileName}\n`], { type: "application/pdf" });
    const url = URL.createObjectURL(blob);
    const a = document.createElement("a");
    a.href = url; a.download = att.fileName; document.body.appendChild(a); a.click(); a.remove();
    setTimeout(() => URL.revokeObjectURL(url), 1500);
    pushToast({ title: "Downloading", body: att.fileName, tone: "accent", icon: "download" });
  };

  const curSection = sections.find((s) => s.id === nav.sectionId);
  const curItems = itemsBySection[nav.sectionId] || [];
  const curItem = curItems.find((i) => i.id === nav.itemId);

  return (
    <div style={{ minHeight: "100vh", display: "flex", flexDirection: "column" }}>
      <TopBar t={t} setTweak={setTweak} query={homeQuery} setQuery={setHomeQuery} onHome={() => go("home")} showSearch={nav.screen === "home" && sections.length > 0} />

      <main style={{ flex: 1, display: "flex", flexDirection: "column" }}>
        {nav.screen === "home" && (
          <HomeScreen sections={sections} itemsBySection={itemsBySection} tileStyle={t.tileStyle}
            onOpenSection={openSection} onCreate={() => setCreateOpen(true)}
            onDeleteSection={deleteSection} query={homeQuery} />
        )}
        {nav.screen === "section" && curSection && (
          <SectionScreen section={curSection} items={curItems} density={t.density}
            onBack={() => go("home")} onOpenItem={openItem}
            onDeleteItem={(itemId) => deleteItem(nav.sectionId, itemId)}
            onUpload={uploadDocs} query={tableQuery} setQuery={setTableQuery} />
        )}
        {nav.screen === "item" && curSection && curItem && (
          <ItemDetail section={curSection} item={curItem}
            onBack={() => openSection(curSection.id)}
            onDownload={downloadFile}
            onSave={(newMeta) => saveItem(nav.sectionId, nav.itemId, newMeta)}
            onDelete={() => deleteItem(nav.sectionId, nav.itemId)} />
        )}
      </main>

      <CreateSectionModal open={createOpen} onClose={() => setCreateOpen(false)} onCreate={createSection} />
      {confirm && (
        <ConfirmModal open={!!confirm} onClose={() => setConfirm(null)} onConfirm={confirm.onConfirm}
          title={confirm.title} body={confirm.body} confirmLabel={confirm.confirmLabel || "Delete"} />
      )}
      <Toasts toasts={toasts} onDismiss={dismissToast} />

      <TweaksPanel>
        <TweakSection label="Home tiles" />
        <TweakRadio label="Tile style" value={t.tileStyle} options={["card", "icon"]} onChange={(v) => setTweak("tileStyle", v)} />
        <TweakSection label="Data table" />
        <TweakRadio label="Density" value={t.density} options={["comfortable", "compact"]} onChange={(v) => setTweak("density", v)} />
        <TweakSection label="Theme" />
        <TweakColor label="Accent" value={t.accent} options={ACCENT_OPTS} onChange={(v) => setTweak("accent", v)} />
        <TweakToggle label="Dark mode" value={t.dark} onChange={(v) => setTweak("dark", v)} />
      </TweaksPanel>
    </div>
  );
}

ReactDOM.createRoot(document.getElementById("root")).render(<App />);
