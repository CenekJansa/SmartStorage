/* SmartStorage — Item detail: metadata view + inline edit mode */

function MetaField({ attr, editing, value, draft, onDraftChange }) {
  const formatted = fmtValue(value, attr.type);
  const numeric = attr.type === "NUMBER";

  const inputStyle = {
    width: "100%", fontFamily: (numeric || attr.type === "DATE") ? "var(--mono)" : "var(--ui)",
    fontSize: 14, fontWeight: 500, color: "var(--text)", background: "var(--field)", outline: "none",
    border: "1px solid var(--border)", borderRadius: 8, padding: "8px 10px",
    fontVariantNumeric: "tabular-nums",
    boxSizing: "border-box",
  };

  return (
    <div style={{ display: "flex", flexDirection: "column", gap: 6, padding: "13px 0", borderBottom: "1px solid var(--border)" }}>
      <div style={{ display: "flex", alignItems: "center", gap: 7 }}>
        <span style={{ fontSize: 12, fontWeight: 540, color: "var(--text-3)", whiteSpace: "nowrap" }}>{attr.name}</span>
        {attr.identifier && <IdBadge />}
        <span style={{ marginLeft: "auto" }}><TypeChip type={attr.type} withLabel={false} /></span>
      </div>
      {editing ? (
        attr.type === "DATE" ? (
          <input type="date" style={inputStyle} value={draft ?? ""} onChange={(e) => onDraftChange(e.target.value || null)} />
        ) : attr.type === "NUMBER" ? (
          <input type="number" style={inputStyle} value={draft ?? ""} placeholder="—"
            onChange={(e) => onDraftChange(e.target.value === "" ? null : Number(e.target.value))} />
        ) : (
          <input type="text" style={inputStyle} value={draft ?? ""} placeholder="—"
            onChange={(e) => onDraftChange(e.target.value || null)} />
        )
      ) : (
        <div style={{ fontSize: 15, fontWeight: 500, color: formatted === null ? "var(--text-4)" : "var(--text)",
          fontFamily: (numeric || attr.type === "DATE") ? "var(--mono)" : "var(--ui)", fontVariantNumeric: "tabular-nums" }}>
          {formatted === null ? <span style={{ fontStyle: "italic", fontSize: 13.5 }}>Not extracted</span> : formatted}
        </div>
      )}
    </div>
  );
}

function FileRow({ att, accent, onDownload }) {
  const [hover, setHover] = React.useState(false);
  const failed = att.status === "FAILED";
  const processing = att.status === "PROCESSING";
  const clickable = !processing;
  return (
    <div
      onClick={() => clickable && onDownload(att)}
      onMouseEnter={() => setHover(true)} onMouseLeave={() => setHover(false)}
      style={{ display: "flex", alignItems: "center", gap: 13, padding: "13px 15px", borderRadius: 12,
        border: `1px solid ${failed ? "color-mix(in oklch, var(--danger) 30%, transparent)" : "var(--border)"}`,
        background: failed ? "color-mix(in oklch, var(--danger) 6%, var(--surface))" : (hover && clickable ? "var(--row-hover)" : "var(--surface)"),
        cursor: clickable ? "pointer" : "default", transition: "background .12s, border-color .12s", opacity: processing ? .85 : 1 }}>
      <div style={{ width: 38, height: 38, flex: "none", borderRadius: 9, display: "grid", placeItems: "center",
        background: failed ? "color-mix(in oklch, var(--danger) 13%, transparent)" : "color-mix(in oklch, #d6453d 12%, transparent)",
        color: failed ? "var(--danger)" : "var(--pdf)" }}>
        <Icon name="filePdf" size={20} />
      </div>
      <div style={{ flex: 1, minWidth: 0 }}>
        <div style={{ fontSize: 13.5, fontWeight: 540, color: failed ? "var(--danger)" : "var(--text)", overflow: "hidden", textOverflow: "ellipsis", whiteSpace: "nowrap" }}>{att.fileName}</div>
        <div style={{ fontSize: 12, color: "var(--text-4)", marginTop: 2 }}>
          {failed ? "Could not extract data from this file" : processing ? "Reading document…" : "Source PDF · click to download"}
        </div>
      </div>
      <StatusPill status={att.status} size="sm" />
      {clickable && <Icon name="download" size={17} style={{ color: hover ? accent : "var(--text-4)", flex: "none", transition: "color .12s" }} />}
    </div>
  );
}

function ItemDetail({ section, item, onBack, onDownload, onSave, onDelete }) {
  const [editing, setEditing] = React.useState(false);
  const [draft, setDraft] = React.useState({});

  const startEdit = () => { setDraft({ ...item.metadata }); setEditing(true); };
  const cancelEdit = () => setEditing(false);
  const saveEdit = () => { onSave && onSave(draft); setEditing(false); };

  const setField = (name, val) => setDraft((d) => ({ ...d, [name]: val }));

  const failedCount = item.attachments.filter((a) => a.status === "FAILED").length;
  const idAttr = section.attributes.find((a) => a.identifier);
  const idValue = idAttr ? fmtValue(item.metadata[idAttr.name], idAttr.type) : null;

  return (
    <div style={{ maxWidth: 940, margin: "0 auto", padding: "22px 32px 64px", width: "100%" }}>
      {/* breadcrumb */}
      <div style={{ display: "flex", alignItems: "center", gap: 8, marginBottom: 18, fontSize: 13, flexWrap: "wrap" }}>
        <button onClick={onBack} style={{ display: "inline-flex", alignItems: "center", gap: 6, background: "transparent", border: "none", cursor: "pointer", color: "var(--text-3)", fontFamily: "var(--ui)", fontSize: 13, fontWeight: 500, padding: "4px 8px 4px 4px", borderRadius: 7 }}>
          <Icon name="arrowLeft" size={16} /> {section.name}
        </button>
        <Icon name="chevronRight" size={13} style={{ color: "var(--text-4)" }} />
        <span style={{ color: "var(--text-2)", fontWeight: 540, fontFamily: "var(--mono)", fontSize: 12.5, whiteSpace: "nowrap" }}>{idValue || item.name}</span>
      </div>

      {/* header */}
      <div style={{ display: "flex", alignItems: "center", gap: 15, marginBottom: 26, flexWrap: "wrap" }}>
        <SectionGlyph section={section} size={52} />
        <div style={{ flex: 1, minWidth: 0 }}>
          <h1 style={{ fontSize: 23, fontWeight: 640, color: "var(--text)", letterSpacing: "-.02em", margin: 0 }}>{item.name}</h1>
          <p style={{ fontSize: 13, color: "var(--text-3)", margin: "4px 0 0", display: "flex", alignItems: "center", gap: 8, flexWrap: "wrap" }}>
            <span>{section.name}</span>
            <span style={{ color: "var(--text-4)" }}>·</span>
            <span>{item.attachments.length} source file{item.attachments.length === 1 ? "" : "s"}</span>
            {failedCount > 0 && (
              <span style={{ display: "inline-flex", alignItems: "center", gap: 4, color: "var(--danger)" }}>
                <Icon name="alert" size={13} /> {failedCount} failed
              </span>
            )}
          </p>
        </div>
        {/* action buttons */}
        <div style={{ display: "flex", gap: 8 }}>
          {editing ? (
            <>
              <Button variant="ghost" onClick={cancelEdit}>Cancel</Button>
              <Button variant="primary" icon="check" onClick={saveEdit}>Save changes</Button>
            </>
          ) : (
            <>
              <Button variant="default" icon="type" onClick={startEdit}>Edit</Button>
              <Button variant="danger" icon="trash" onClick={onDelete} style={{ border: "1px solid color-mix(in oklch, var(--danger) 30%, transparent)" }}>Delete</Button>
            </>
          )}
        </div>
      </div>

      <div style={{ display: "grid", gridTemplateColumns: "1fr 380px", gap: 28, alignItems: "start" }} className="ss-detail-grid">
        {/* metadata */}
        <section>
          <h2 style={{ fontSize: 12.5, fontWeight: 620, color: "var(--text-3)", textTransform: "uppercase", letterSpacing: ".06em", margin: "0 0 4px" }}>
            {editing ? "Editing metadata" : "Extracted metadata"}
          </h2>
          <div style={{ background: "var(--surface)", border: `1px solid ${editing ? "color-mix(in oklch, var(--accent) 40%, var(--border))" : "var(--border)"}`, borderRadius: 14, padding: "4px 18px",
            boxShadow: editing ? `0 0 0 3px color-mix(in oklch, var(--accent) 12%, transparent)` : "0 1px 2px color-mix(in oklch, var(--shadow-ink) 8%, transparent)",
            transition: "border-color .16s, box-shadow .16s" }}>
            {section.attributes.map((a) => (
              <MetaField key={a.name} attr={a} editing={editing}
                value={item.metadata[a.name]}
                draft={draft[a.name] !== undefined ? draft[a.name] : item.metadata[a.name]}
                onDraftChange={(v) => setField(a.name, v)} />
            ))}
          </div>
          {editing && (
            <p style={{ fontSize: 12, color: "var(--text-4)", margin: "10px 2px 0", lineHeight: 1.5 }}>
              Editing only changes the stored extracted values — original source files are not modified.
            </p>
          )}
        </section>

        {/* source files */}
        <section>
          <h2 style={{ fontSize: 12.5, fontWeight: 620, color: "var(--text-3)", textTransform: "uppercase", letterSpacing: ".06em", margin: "0 0 4px" }}>Source files</h2>
          <div style={{ display: "flex", flexDirection: "column", gap: 9 }}>
            {item.attachments.length === 0 ? (
              <div style={{ fontSize: 13, color: "var(--text-4)", padding: "20px 0", textAlign: "center" }}>No source files.</div>
            ) : item.attachments.map((att) => (
              <FileRow key={att.id} att={att} accent={section.accent} onDownload={onDownload} />
            ))}
          </div>
          <p style={{ fontSize: 12, color: "var(--text-4)", margin: "12px 2px 0", lineHeight: 1.5, display: "flex", gap: 6 }}>
            <Icon name="sparkle" size={13} style={{ color: "var(--accent)", flex: "none", marginTop: 1 }} />
            Values above were extracted by SmartStorage from these files. Click any completed file to download the original PDF.
          </p>
        </section>
      </div>
    </div>
  );
}

window.ItemDetail = ItemDetail;
