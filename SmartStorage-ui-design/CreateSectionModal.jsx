/* SmartStorage — Create Section modal (schema editor) */

const TYPE_OPTS = ["STRING", "DATE", "NUMBER"];

function AttrRow({ attr, onChange, onRemove, canRemove }) {
  return (
    <div style={{ display: "grid", gridTemplateColumns: "1fr 116px auto auto", gap: 8, alignItems: "center" }}>
      <input
        value={attr.name}
        onChange={(e) => onChange({ ...attr, name: e.target.value })}
        placeholder="Attribute name"
        style={{ fontFamily: "var(--ui)", fontSize: 13.5, color: "var(--text)", background: "var(--field)", outline: "none",
          border: "1px solid var(--border)", borderRadius: 9, padding: "9px 11px", minWidth: 0 }}
      />
      <div style={{ position: "relative" }}>
        <select
          value={attr.type}
          onChange={(e) => onChange({ ...attr, type: e.target.value })}
          style={{ appearance: "none", width: "100%", fontFamily: "var(--mono)", fontSize: 12, fontWeight: 500, color: "var(--text-2)",
            background: "var(--field)", border: "1px solid var(--border)", borderRadius: 9, padding: "9px 26px 9px 28px", cursor: "pointer", outline: "none" }}
        >
          {TYPE_OPTS.map((t) => <option key={t} value={t}>{t}</option>)}
        </select>
        <Icon name={typeIcon(attr.type)} size={13} style={{ position: "absolute", left: 9, top: "50%", transform: "translateY(-50%)", color: "var(--text-3)", pointerEvents: "none" }} />
        <Icon name="chevronRight" size={12} style={{ position: "absolute", right: 8, top: "50%", transform: "translateY(-50%) rotate(90deg)", color: "var(--text-4)", pointerEvents: "none" }} />
      </div>
      <button
        type="button"
        onClick={() => onChange({ ...attr, identifier: !attr.identifier })}
        title="Identifier — used to de-duplicate items"
        style={{ display: "inline-flex", alignItems: "center", gap: 5, cursor: "pointer", height: 36, padding: "0 10px",
          fontFamily: "var(--mono)", fontSize: 11, fontWeight: 600, letterSpacing: ".03em", borderRadius: 9,
          border: `1px solid ${attr.identifier ? "color-mix(in oklch, var(--accent) 36%, transparent)" : "var(--border)"}`,
          color: attr.identifier ? "var(--accent-ink)" : "var(--text-4)",
          background: attr.identifier ? "var(--accent-soft)" : "var(--field)", transition: "all .14s" }}
      >
        <Icon name="key" size={13} /> ID
      </button>
      <button type="button" onClick={onRemove} disabled={!canRemove}
        style={{ width: 36, height: 36, display: "grid", placeItems: "center", borderRadius: 9, cursor: canRemove ? "pointer" : "not-allowed",
          border: "1px solid var(--border)", background: "var(--field)", color: canRemove ? "var(--text-3)" : "var(--text-4)", opacity: canRemove ? 1 : .4 }}>
        <Icon name="trash" size={15} />
      </button>
    </div>
  );
}

function CreateSectionModal({ open, onClose, onCreate }) {
  const [name, setName] = React.useState("");
  const [attrs, setAttrs] = React.useState([{ name: "", type: "STRING", identifier: true }]);

  React.useEffect(() => {
    if (open) { setName(""); setAttrs([{ name: "", type: "STRING", identifier: true }]); }
  }, [open]);

  const setAttr = (i, next) => setAttrs((a) => a.map((x, j) => (j === i ? next : x)));
  const addAttr = () => setAttrs((a) => [...a, { name: "", type: "STRING", identifier: false }]);
  const removeAttr = (i) => setAttrs((a) => a.filter((_, j) => j !== i));

  const validAttrs = attrs.filter((a) => a.name.trim());
  const canSave = name.trim() && validAttrs.length > 0;

  const submit = () => {
    if (!canSave) return;
    onCreate({ name: name.trim(), attributes: validAttrs.map((a) => ({ ...a, name: a.name.trim() })) });
  };

  return (
    <Modal open={open} onClose={onClose} width={620} labelledBy="cs-title">
      <div style={{ display: "flex", alignItems: "center", gap: 12, padding: "20px 24px 16px", borderBottom: "1px solid var(--border)" }}>
        <div style={{ width: 40, height: 40, borderRadius: 11, display: "grid", placeItems: "center", background: "var(--accent-soft)", color: "var(--accent)", flex: "none" }}>
          <Icon name="layers" size={21} sw={1.5} />
        </div>
        <div style={{ flex: 1 }}>
          <h2 id="cs-title" style={{ fontSize: 17, fontWeight: 620, color: "var(--text)", margin: 0, letterSpacing: "-.01em" }}>New storage section</h2>
          <p style={{ fontSize: 12.5, color: "var(--text-3)", margin: "2px 0 0" }}>Define the schema. Uploaded PDFs are extracted into these fields.</p>
        </div>
        <button onClick={onClose} style={{ width: 32, height: 32, display: "grid", placeItems: "center", borderRadius: 8, border: "none", background: "transparent", color: "var(--text-3)", cursor: "pointer" }}>
          <Icon name="close" size={18} />
        </button>
      </div>

      <div style={{ padding: "18px 24px", overflowY: "auto" }}>
        <label style={{ display: "block", fontSize: 12.5, fontWeight: 580, color: "var(--text-2)", marginBottom: 7 }}>Section name</label>
        <input
          value={name} autoFocus
          onChange={(e) => setName(e.target.value)}
          onKeyDown={(e) => e.key === "Enter" && document.getElementById("cs-add-attr")?.focus()}
          placeholder="e.g. Invoices, Contracts, Candidates…"
          style={{ width: "100%", fontFamily: "var(--ui)", fontSize: 15, color: "var(--text)", background: "var(--field)", outline: "none",
            border: "1px solid var(--border)", borderRadius: 10, padding: "11px 13px", marginBottom: 20 }}
        />

        <div style={{ display: "flex", alignItems: "center", justifyContent: "space-between", marginBottom: 10 }}>
          <label style={{ fontSize: 12.5, fontWeight: 580, color: "var(--text-2)" }}>Attributes</label>
          <span style={{ fontSize: 11.5, color: "var(--text-4)", display: "inline-flex", alignItems: "center", gap: 5 }}>
            <Icon name="key" size={12} /> = identifier (de-dup key)
          </span>
        </div>

        <div style={{ display: "flex", flexDirection: "column", gap: 8 }}>
          {attrs.map((a, i) => (
            <AttrRow key={i} attr={a} onChange={(n) => setAttr(i, n)} onRemove={() => removeAttr(i)} canRemove={attrs.length > 1} />
          ))}
        </div>

        <button id="cs-add-attr" type="button" onClick={addAttr}
          style={{ marginTop: 10, display: "inline-flex", alignItems: "center", gap: 7, fontFamily: "var(--ui)", fontSize: 13, fontWeight: 540,
            color: "var(--accent-ink)", background: "transparent", border: "1px dashed color-mix(in oklch, var(--accent) 36%, var(--border))",
            borderRadius: 9, padding: "9px 13px", cursor: "pointer", width: "100%", justifyContent: "center" }}>
          <Icon name="plus" size={15} /> Add attribute
        </button>
      </div>

      <div style={{ display: "flex", alignItems: "center", justifyContent: "space-between", gap: 12, padding: "15px 24px", borderTop: "1px solid var(--border)" }}>
        <span style={{ fontSize: 12.5, color: "var(--text-4)" }}>{validAttrs.length} attribute{validAttrs.length === 1 ? "" : "s"}</span>
        <div style={{ display: "flex", gap: 9 }}>
          <Button variant="ghost" onClick={onClose}>Cancel</Button>
          <Button variant="primary" icon="check" onClick={submit} disabled={!canSave} style={{ opacity: canSave ? 1 : .5, pointerEvents: canSave ? "auto" : "none" }}>Create section</Button>
        </div>
      </div>
    </Modal>
  );
}

window.CreateSectionModal = CreateSectionModal;
