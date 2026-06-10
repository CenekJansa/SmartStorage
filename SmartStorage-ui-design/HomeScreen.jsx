/* SmartStorage — Home screen (section grid) */

function TileDotMenu({ onDelete }) {
  const [open, setOpen] = React.useState(false);
  const ref = React.useRef(null);
  React.useEffect(() => {
    if (!open) return;
    const h = (e) => { if (!ref.current?.contains(e.target)) setOpen(false); };
    setTimeout(() => document.addEventListener("mousedown", h), 0);
    return () => document.removeEventListener("mousedown", h);
  }, [open]);
  return (
    <div ref={ref} style={{ position: "relative" }}>
      <button
        onClick={(e) => { e.stopPropagation(); setOpen((o) => !o); }}
        style={{ width: 28, height: 28, display: "grid", placeItems: "center", borderRadius: 7,
          border: open ? "1px solid var(--border)" : "1px solid transparent",
          background: open ? "var(--chip)" : "transparent",
          color: "var(--text-3)", cursor: "pointer", transition: "all .12s" }}>
        <Icon name="dots" size={15} />
      </button>
      {open && (
        <div onClick={(e) => e.stopPropagation()} style={{ position: "absolute", right: 0, top: "calc(100% + 5px)", zIndex: 30,
          background: "var(--surface)", border: "1px solid var(--border)", borderRadius: 10, padding: 4,
          boxShadow: "0 8px 24px -6px color-mix(in oklch, var(--shadow-ink) 32%, transparent)", minWidth: 160 }}>
          <button onClick={() => { setOpen(false); onDelete && onDelete(); }}
            style={{ display: "flex", alignItems: "center", gap: 9, width: "100%", padding: "8px 10px", borderRadius: 7,
              background: "transparent", border: "none", cursor: "pointer", color: "var(--danger)",
              fontFamily: "var(--ui)", fontSize: 13.5, fontWeight: 500, transition: "background .1s" }}
            onMouseEnter={(e) => e.currentTarget.style.background = "color-mix(in oklch, var(--danger) 10%, transparent)"}
            onMouseLeave={(e) => e.currentTarget.style.background = "transparent"}>
            <Icon name="trash" size={15} /> Delete section
          </button>
        </div>
      )}
    </div>
  );
}

function SectionTileCard({ section, items, onOpen, onDelete }) {
  const [hover, setHover] = React.useState(false);
  const count = items.length;
  const preview = section.attributes.slice(0, 4);
  return (
    <div
      role="button" tabIndex={0}
      onClick={onOpen}
      onKeyDown={(e) => e.key === "Enter" && onOpen()}
      onMouseEnter={() => setHover(true)}
      onMouseLeave={() => setHover(false)}
      style={{
        textAlign: "left", cursor: "pointer", fontFamily: "var(--ui)",
        background: "var(--surface)", border: "1px solid var(--border)", borderRadius: 16,
        padding: 18, display: "flex", flexDirection: "column", gap: 14,
        transition: "transform .16s cubic-bezier(.2,.8,.3,1), box-shadow .16s, border-color .16s",
        transform: hover ? "translateY(-3px)" : "none",
        boxShadow: hover
          ? `0 14px 30px -14px color-mix(in oklch, ${section.accent} 50%, transparent), 0 2px 6px color-mix(in oklch, var(--shadow-ink) 14%, transparent)`
          : "0 1px 2px color-mix(in oklch, var(--shadow-ink) 8%, transparent)",
        borderColor: hover ? `color-mix(in oklch, ${section.accent} 40%, var(--border))` : "var(--border)",
        userSelect: "none",
      }}
    >
      <div style={{ display: "flex", alignItems: "flex-start", justifyContent: "space-between", gap: 8 }}>
        <SectionGlyph section={section} size={50} />
        <div style={{ display: "flex", alignItems: "center", gap: 6 }}>
          <span style={{ display: "inline-flex", alignItems: "center", gap: 5, fontFamily: "var(--mono)", fontSize: 12, whiteSpace: "nowrap",
            color: "var(--text-3)", background: "var(--chip)", border: "1px solid var(--border)", borderRadius: 999, padding: "3px 10px" }}>
            {count} {count === 1 ? "item" : "items"}
          </span>
          {hover && <TileDotMenu onDelete={onDelete} />}
        </div>
      </div>
      <div>
        <div style={{ fontSize: 17, fontWeight: 600, color: "var(--text)", letterSpacing: "-.01em" }}>{section.name}</div>
        <div style={{ fontSize: 12.5, color: "var(--text-3)", marginTop: 3 }}>
          {section.attributes.length} attributes
        </div>
      </div>
      <div style={{ display: "flex", flexWrap: "wrap", gap: 6, marginTop: "auto" }}>
        {preview.map((a) => (
          <span key={a.name} style={{ display: "inline-flex", alignItems: "center", gap: 4, fontSize: 11.5, color: "var(--text-2)", whiteSpace: "nowrap",
            background: "var(--chip)", border: "1px solid var(--border)", borderRadius: 6, padding: "2px 7px" }}>
            {a.identifier && <Icon name="key" size={11} style={{ color: section.accent }} />}
            {a.name}
          </span>
        ))}
        {section.attributes.length > preview.length && (
          <span style={{ fontSize: 11.5, color: "var(--text-4)", alignSelf: "center" }}>+{section.attributes.length - preview.length}</span>
        )}
      </div>
    </div>
  );
}

function SectionTileIcon({ section, items, onOpen, onDelete }) {
  const [hover, setHover] = React.useState(false);
  const count = items.length;
  return (
    <div onMouseEnter={() => setHover(true)} onMouseLeave={() => setHover(false)}
      style={{ fontFamily: "var(--ui)", padding: 8, display: "flex", flexDirection: "column", alignItems: "center", gap: 11, position: "relative" }}>
      <div onClick={onOpen} style={{ position: "relative", cursor: "pointer",
        transition: "transform .16s cubic-bezier(.2,.8,.3,1)", transform: hover ? "translateY(-4px) scale(1.03)" : "none" }}>
        <SectionGlyph section={section} size={84} radius={22} />
        <span style={{ position: "absolute", top: -6, right: -6, minWidth: 22, height: 22, padding: "0 6px",
          display: "grid", placeItems: "center", fontFamily: "var(--mono)", fontSize: 11.5, fontWeight: 600, color: "#fff",
          background: section.accent, borderRadius: 999, border: "2px solid var(--bg)" }}>{count}</span>
      </div>
      <div onClick={onOpen} style={{ fontSize: 13.5, fontWeight: 540, color: "var(--text)", textAlign: "center", maxWidth: 120, cursor: "pointer" }}>{section.name}</div>
      {hover && (
        <div style={{ position: "absolute", top: 4, right: 4 }}>
          <TileDotMenu onDelete={onDelete} />
        </div>
      )}
    </div>
  );
}

function AddTile({ variant, onClick }) {
  const [hover, setHover] = React.useState(false);
  if (variant === "icon") {
    return (
      <button onClick={onClick} onMouseEnter={() => setHover(true)} onMouseLeave={() => setHover(false)}
        style={{ cursor: "pointer", fontFamily: "var(--ui)", background: "transparent", border: "none", padding: 8,
          display: "flex", flexDirection: "column", alignItems: "center", gap: 11 }}>
        <div style={{ width: 84, height: 84, borderRadius: 22, display: "grid", placeItems: "center",
          border: "2px dashed var(--border-2)", color: hover ? "var(--accent)" : "var(--text-4)",
          background: hover ? "var(--accent-soft)" : "transparent", transition: "all .16s",
          transform: hover ? "translateY(-4px)" : "none" }}>
          <Icon name="plus" size={30} />
        </div>
        <div style={{ fontSize: 13.5, fontWeight: 540, color: "var(--text-3)" }}>New section</div>
      </button>
    );
  }
  return (
    <button onClick={onClick} onMouseEnter={() => setHover(true)} onMouseLeave={() => setHover(false)}
      style={{ cursor: "pointer", fontFamily: "var(--ui)", borderRadius: 16, minHeight: 188,
        border: "1.5px dashed var(--border-2)", color: hover ? "var(--accent)" : "var(--text-3)",
        background: hover ? "var(--accent-soft)" : "transparent", transition: "all .16s",
        display: "flex", flexDirection: "column", alignItems: "center", justifyContent: "center", gap: 12 }}>
      <div style={{ width: 46, height: 46, borderRadius: 12, display: "grid", placeItems: "center",
        background: hover ? "var(--accent)" : "var(--chip)", color: hover ? "#fff" : "var(--text-3)", transition: "all .16s" }}>
        <Icon name="plus" size={22} />
      </div>
      <div style={{ fontSize: 14.5, fontWeight: 560 }}>Create a section</div>
      <div style={{ fontSize: 12.5, color: "var(--text-4)", maxWidth: 180, textAlign: "center" }}>Define a schema, then upload PDFs to fill it</div>
    </button>
  );
}

function HomeScreen({ sections, itemsBySection, tileStyle, onOpenSection, onCreate, onDeleteSection, query }) {
  const filtered = sections.filter((s) => s.name.toLowerCase().includes(query.toLowerCase()));
  const isEmpty = sections.length === 0;

  if (isEmpty) {
    return (
      <div style={{ maxWidth: 560, margin: "0 auto", padding: "10vh 24px", textAlign: "center" }}>
        <div style={{ width: 76, height: 76, margin: "0 auto 22px", borderRadius: 20, display: "grid", placeItems: "center",
          background: "var(--accent-soft)", color: "var(--accent)" }}>
          <Icon name="layers" size={36} sw={1.4} />
        </div>
        <h1 style={{ fontSize: 26, fontWeight: 640, color: "var(--text)", letterSpacing: "-.02em", margin: 0 }}>No sections yet</h1>
        <p style={{ fontSize: 15, color: "var(--text-3)", lineHeight: 1.55, margin: "10px auto 26px", maxWidth: 420 }}>
          A section is a schema for the documents you collect. Define its attributes once, then drop in PDFs — SmartStorage extracts the values for you.
        </p>
        <Button variant="primary" size="lg" icon="plus" onClick={onCreate}>Create your first section</Button>
      </div>
    );
  }

  const grid = tileStyle === "icon"
    ? { display: "grid", gridTemplateColumns: "repeat(auto-fill, minmax(132px, 1fr))", gap: 16, alignItems: "start" }
    : { display: "grid", gridTemplateColumns: "repeat(auto-fill, minmax(244px, 1fr))", gap: 16 };

  return (
    <div style={{ maxWidth: 1120, margin: "0 auto", padding: "30px 32px 64px", width: "100%" }}>
      <div style={{ display: "flex", alignItems: "flex-end", justifyContent: "space-between", marginBottom: 22, flexWrap: "wrap", gap: 12 }}>
        <div>
          <h1 style={{ fontSize: 24, fontWeight: 640, color: "var(--text)", letterSpacing: "-.02em", margin: 0 }}>Your sections</h1>
          <p style={{ fontSize: 13.5, color: "var(--text-3)", margin: "5px 0 0" }}>
            {sections.length} {sections.length === 1 ? "section" : "sections"} · {Object.values(itemsBySection).reduce((a, b) => a + b.length, 0)} items stored
          </p>
        </div>
        <Button variant="primary" icon="plus" onClick={onCreate}>New section</Button>
      </div>

      {filtered.length === 0 ? (
        <div style={{ padding: "60px 0", textAlign: "center", color: "var(--text-4)", fontSize: 14 }}>
          No sections match “{query}”.
        </div>
      ) : (
        <div style={grid}>
          {filtered.map((s) =>
            tileStyle === "icon" ? (
              <SectionTileIcon key={s.id} section={s} items={itemsBySection[s.id] || []} onOpen={() => onOpenSection(s.id)} onDelete={() => onDeleteSection(s.id)} />
            ) : (
              <SectionTileCard key={s.id} section={s} items={itemsBySection[s.id] || []} onOpen={() => onOpenSection(s.id)} onDelete={() => onDeleteSection(s.id)} />
            )
          )}
          {!query && <AddTile variant={tileStyle} onClick={onCreate} />}
        </div>
      )}
    </div>
  );
}

window.HomeScreen = HomeScreen;
