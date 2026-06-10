/* SmartStorage — Section view (data table + upload) */

function HeaderCell({ attr, density, sort, onSort }) {
  const numeric = attr.type === "NUMBER";
  const active = sort && sort.key === attr.name;
  return (
    <th
      onClick={() => onSort(attr.name)}
      style={{ position: "sticky", top: 0, zIndex: 2, cursor: "pointer", userSelect: "none",
        background: "var(--thead)", borderBottom: "1px solid var(--border)",
        padding: density === "compact" ? "9px 14px" : "12px 16px",
        textAlign: numeric ? "right" : "left", whiteSpace: "nowrap",
        boxShadow: "0 1px 0 var(--border)" }}>
      <div style={{ display: "inline-flex", alignItems: "center", gap: 6, flexDirection: numeric ? "row-reverse" : "row" }}>
        <span style={{ display: "inline-flex", alignItems: "center", gap: 5, fontSize: 12.5, fontWeight: 600, color: active ? "var(--text)" : "var(--text-2)", letterSpacing: ".01em" }}>
          {attr.identifier && <Icon name="key" size={12.5} style={{ color: "var(--accent)" }} />}
          {attr.name}
        </span>
        <Icon name="type" size={0} style={{ display: "none" }} />
        <span title={attr.type} style={{ color: "var(--text-4)", display: "inline-flex" }}>
          <Icon name={typeIcon(attr.type)} size={12.5} />
        </span>
        {active && <Icon name="chevronRight" size={12} style={{ color: "var(--accent)", transform: sort.dir === "asc" ? "rotate(-90deg)" : "rotate(90deg)" }} />}
      </div>
    </th>
  );
}

function DataCell({ value, type, density, mono }) {
  const numeric = type === "NUMBER";
  const formatted = fmtValue(value, type);
  return (
    <td style={{ padding: density === "compact" ? "8px 14px" : "12px 16px", textAlign: numeric ? "right" : "left",
      borderBottom: "1px solid var(--border)", fontSize: density === "compact" ? 13 : 13.5,
      color: formatted === null ? "var(--text-4)" : "var(--text)", whiteSpace: "nowrap",
      fontFamily: (numeric || type === "DATE") ? "var(--mono)" : "var(--ui)",
      fontVariantNumeric: "tabular-nums" }}>
      {formatted === null ? <EmptyCell /> : formatted}
    </td>
  );
}

function SkeletonRow({ section, density }) {
  return (
    <tr>
      <td style={{ padding: density === "compact" ? "8px 14px" : "12px 16px", borderBottom: "1px solid var(--border)" }}>
        <span style={{ display: "inline-flex", alignItems: "center", gap: 7, color: "var(--warn)", fontSize: 12.5, fontWeight: 540 }}>
          <Icon name="spinner" size={13} className="ss-spin" /> Extracting…
        </span>
      </td>
      {section.attributes.slice(1).map((a, i) => (
        <td key={a.name} style={{ padding: density === "compact" ? "8px 14px" : "12px 16px", borderBottom: "1px solid var(--border)", textAlign: a.type === "NUMBER" ? "right" : "left" }}>
          <span className="ss-shimmer" style={{ display: "inline-block", height: 11, borderRadius: 4, width: [70, 52, 88, 40, 60][i % 5] }} />
        </td>
      ))}
    </tr>
  );
}

function DataRow({ section, item, density, onOpen, onDelete }) {
  const [hover, setHover] = React.useState(false);
  const failed = item.attachments.some((a) => a.status === "FAILED");
  const allFailed = item.attachments.length > 0 && item.attachments.every((a) => a.status === "FAILED");
  return (
    <tr onClick={onOpen} onMouseEnter={() => setHover(true)} onMouseLeave={() => setHover(false)}
      style={{ cursor: "pointer", background: hover ? "var(--row-hover)" : "transparent", transition: "background .1s" }}>
      {section.attributes.map((a, i) => (
        <td key={a.name} style={{ padding: density === "compact" ? "8px 14px" : "12px 16px",
          textAlign: a.type === "NUMBER" ? "right" : "left", borderBottom: "1px solid var(--border)",
          fontSize: density === "compact" ? 13 : 13.5, whiteSpace: "nowrap",
          color: fmtValue(item.metadata[a.name], a.type) === null ? "var(--text-4)" : "var(--text)",
          fontWeight: i === 0 ? 560 : 400,
          fontFamily: (a.type === "NUMBER" || a.type === "DATE") ? "var(--mono)" : "var(--ui)",
          fontVariantNumeric: "tabular-nums" }}>
          {i === 0 ? (
            <span style={{ display: "inline-flex", alignItems: "center", gap: 8 }}>
              {fmtValue(item.metadata[a.name], a.type) === null ? <EmptyCell /> : fmtValue(item.metadata[a.name], a.type)}
              {failed && <span title={allFailed ? "All source files failed" : "A source file failed"}><Icon name="alert" size={14} style={{ color: "var(--danger)", display: "block" }} /></span>}
            </span>
          ) : (
            fmtValue(item.metadata[a.name], a.type) === null ? <EmptyCell /> : fmtValue(item.metadata[a.name], a.type)
          )}
        </td>
      ))}
      <td onClick={(e) => e.stopPropagation()} style={{ padding: "0 8px", borderBottom: "1px solid var(--border)", width: 72, textAlign: "right", whiteSpace: "nowrap" }}>
        <span style={{ display: "inline-flex", alignItems: "center", gap: 4 }}>
          {hover && (
            <button onClick={(e) => { e.stopPropagation(); onDelete && onDelete(); }}
              title="Delete item"
              style={{ width: 28, height: 28, display: "grid", placeItems: "center", borderRadius: 7,
                border: "none", background: "transparent", cursor: "pointer", color: "var(--text-4)",
                transition: "color .1s, background .1s" }}
              onMouseEnter={(e) => { e.currentTarget.style.color = "var(--danger)"; e.currentTarget.style.background = "color-mix(in oklch, var(--danger) 10%, transparent)"; }}
              onMouseLeave={(e) => { e.currentTarget.style.color = "var(--text-4)"; e.currentTarget.style.background = "transparent"; }}>
              <Icon name="trash" size={14} />
            </button>
          )}
          <Icon name="chevronRight" size={15} style={{ color: hover ? "var(--accent)" : "var(--text-4)", verticalAlign: "middle", transition: "color .1s" }} />
        </span>
      </td>
    </tr>
  );
}

function SectionScreen({ section, items, density, onBack, onOpenItem, onDeleteItem, onUpload, query, setQuery }) {
  const [sort, setSort] = React.useState(null);
  const fileRef = React.useRef(null);

  const onSort = (key) => setSort((s) => (s && s.key === key ? (s.dir === "asc" ? { key, dir: "desc" } : null) : { key, dir: "asc" }));

  let rows = items.filter((it) => {
    if (!query) return true;
    const q = query.toLowerCase();
    return it.name.toLowerCase().includes(q) || Object.values(it.metadata).some((v) => v != null && String(v).toLowerCase().includes(q));
  });
  if (sort) {
    const attr = section.attributes.find((a) => a.name === sort.key);
    rows = [...rows].sort((a, b) => {
      const va = a.metadata[sort.key], vb = b.metadata[sort.key];
      if (va == null) return 1; if (vb == null) return -1;
      let c;
      if (attr.type === "NUMBER") c = Number(va) - Number(vb);
      else if (attr.type === "DATE") c = new Date(va) - new Date(vb);
      else c = String(va).localeCompare(String(vb));
      return sort.dir === "asc" ? c : -c;
    });
  }
  const pending = rows.filter((r) => r._pending);
  const settled = rows.filter((r) => !r._pending);

  const triggerUpload = () => fileRef.current?.click();
  const handleFiles = (fileList) => {
    const names = Array.from(fileList || []).map((f) => f.name);
    onUpload(section.id, names.length ? names : ["document.pdf"]);
  };

  const isEmpty = items.length === 0;

  return (
    <div style={{ maxWidth: 1180, margin: "0 auto", padding: "22px 32px 64px", width: "100%" }}>
      {/* breadcrumb + back */}
      <div style={{ display: "flex", alignItems: "center", gap: 8, marginBottom: 18, fontSize: 13 }}>
        <button onClick={onBack} style={{ display: "inline-flex", alignItems: "center", gap: 6, background: "transparent", border: "none", cursor: "pointer", color: "var(--text-3)", fontFamily: "var(--ui)", fontSize: 13, fontWeight: 500, padding: "4px 8px 4px 4px", borderRadius: 7 }}>
          <Icon name="arrowLeft" size={16} /> Sections
        </button>
        <Icon name="chevronRight" size={13} style={{ color: "var(--text-4)" }} />
        <span style={{ color: "var(--text-2)", fontWeight: 540 }}>{section.name}</span>
      </div>

      {/* title row */}
      <div style={{ display: "flex", alignItems: "center", gap: 14, marginBottom: 18, flexWrap: "wrap" }}>
        <SectionGlyph section={section} size={46} />
        <div style={{ flex: 1, minWidth: 200 }}>
          <h1 style={{ fontSize: 22, fontWeight: 640, color: "var(--text)", letterSpacing: "-.02em", margin: 0 }}>{section.name}</h1>
          <p style={{ fontSize: 13, color: "var(--text-3)", margin: "3px 0 0" }}>
            {items.length} {items.length === 1 ? "item" : "items"} · {section.attributes.length} attributes
          </p>
        </div>
        <div style={{ position: "relative", width: 220 }}>
          <Icon name="search" size={15} style={{ position: "absolute", left: 11, top: "50%", transform: "translateY(-50%)", color: "var(--text-4)" }} />
          <input value={query} onChange={(e) => setQuery(e.target.value)} placeholder="Search items…"
            style={{ width: "100%", fontFamily: "var(--ui)", fontSize: 13.5, color: "var(--text)", background: "var(--field)", outline: "none",
              border: "1px solid var(--border)", borderRadius: 10, padding: "9px 12px 9px 33px" }} />
        </div>
        <Button variant="primary" icon="upload" onClick={triggerUpload}>Upload PDF</Button>
        <input ref={fileRef} type="file" accept="application/pdf" multiple style={{ display: "none" }} onChange={(e) => { handleFiles(e.target.files); e.target.value = ""; }} />
      </div>

      {isEmpty ? (
        <UploadZone section={section} onPick={triggerUpload} onDropFiles={handleFiles} large />
      ) : (
        <>
          <UploadZone section={section} onPick={triggerUpload} onDropFiles={handleFiles} />
          <div style={{ marginTop: 16, border: "1px solid var(--border)", borderRadius: 14, overflow: "hidden", background: "var(--surface)",
            boxShadow: "0 1px 2px color-mix(in oklch, var(--shadow-ink) 8%, transparent)" }}>
            <div className="ss-table-wrap" style={{ overflowX: "scroll" }}>
              <table style={{ width: "100%", borderCollapse: "collapse", fontFamily: "var(--ui)" }}>
                <thead>
                  <tr>
                    {section.attributes.map((a) => <HeaderCell key={a.name} attr={a} density={density} sort={sort} onSort={onSort} />)}
                    <th style={{ position: "sticky", top: 0, background: "var(--thead)", borderBottom: "1px solid var(--border)", width: 36 }}></th>
                  </tr>
                </thead>
                <tbody>
                  {pending.map((it) => <SkeletonRow key={it.id} section={section} density={density} />)}
                  {settled.map((it) => <DataRow key={it.id} section={section} item={it} density={density} onOpen={() => onOpenItem(it.id)} onDelete={() => onDeleteItem && onDeleteItem(it.id)} />)}
                </tbody>
              </table>
            </div>
            {settled.length === 0 && pending.length === 0 && (
              <div style={{ padding: "40px 0", textAlign: "center", color: "var(--text-4)", fontSize: 13.5 }}>No items match “{query}”.</div>
            )}
          </div>
        </>
      )}
    </div>
  );
}

function UploadZone({ section, onPick, onDropFiles, large }) {
  const [over, setOver] = React.useState(false);
  return (
    <div
      onClick={onPick}
      onDragOver={(e) => { e.preventDefault(); setOver(true); }}
      onDragLeave={() => setOver(false)}
      onDrop={(e) => { e.preventDefault(); setOver(false); onDropFiles(e.dataTransfer.files); }}
      style={{ cursor: "pointer", borderRadius: 14, transition: "all .16s",
        border: `1.5px dashed ${over ? "var(--accent)" : "var(--border-2)"}`,
        background: over ? "var(--accent-soft)" : (large ? "var(--surface)" : "transparent"),
        padding: large ? "56px 24px" : "16px 20px",
        display: "flex", alignItems: "center", justifyContent: "center", gap: large ? 0 : 14,
        flexDirection: large ? "column" : "row", textAlign: "center" }}>
      <div style={{ width: large ? 60 : 38, height: large ? 60 : 38, borderRadius: large ? 16 : 10, display: "grid", placeItems: "center",
        background: over ? "var(--accent)" : "var(--accent-soft)", color: over ? "#fff" : "var(--accent)", transition: "all .16s", marginBottom: large ? 18 : 0, flex: "none" }}>
        <Icon name="upload" size={large ? 28 : 19} />
      </div>
      <div style={{ textAlign: large ? "center" : "left" }}>
        <div style={{ fontSize: large ? 17 : 14, fontWeight: 600, color: "var(--text)" }}>
          {large ? "Upload your first document" : "Drop PDFs here, or click to browse"}
        </div>
        <div style={{ fontSize: large ? 14 : 12.5, color: "var(--text-3)", marginTop: large ? 6 : 1, maxWidth: large ? 360 : "none" }}>
          {large ? "SmartStorage reads each PDF and extracts values into your columns automatically." : "Processing happens in the background — keep working."}
        </div>
      </div>
      {large && <div style={{ marginTop: 20 }}><Button variant="primary" icon="upload">Choose PDF</Button></div>}
    </div>
  );
}

window.SectionScreen = SectionScreen;
