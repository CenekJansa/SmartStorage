/* SmartStorage — shared components + formatting helpers */

// ---- formatting ----
function fmtValue(value, type) {
  if (value === null || value === undefined || value === "") return null;
  if (type === "NUMBER") {
    const n = Number(value);
    if (Number.isNaN(n)) return String(value);
    return n.toLocaleString("en-US", { minimumFractionDigits: n % 1 === 0 ? 0 : 2, maximumFractionDigits: 2 });
  }
  if (type === "DATE") {
    const d = new Date(value);
    if (Number.isNaN(d.getTime())) return String(value);
    return d.toLocaleDateString("en-US", { day: "numeric", month: "short", year: "numeric" });
  }
  return String(value);
}

// ---- Button ----
function Button({ variant = "default", size = "md", icon, iconRight, children, style, ...rest }) {
  const base = {
    display: "inline-flex", alignItems: "center", gap: children ? 8 : 0, justifyContent: "center",
    fontFamily: "var(--ui)", fontWeight: 540, cursor: "pointer", border: "1px solid transparent",
    borderRadius: 10, transition: "background .14s, border-color .14s, color .14s, transform .06s",
    whiteSpace: "nowrap", lineHeight: 1,
  };
  const sizes = {
    sm: { fontSize: 13, padding: children ? "7px 11px" : 7, height: 32 },
    md: { fontSize: 14, padding: children ? "9px 15px" : 9, height: 38 },
    lg: { fontSize: 15, padding: children ? "12px 20px" : 12, height: 46 },
  };
  const variants = {
    primary: { background: "var(--accent)", color: "#fff", boxShadow: "0 1px 2px rgba(0,0,0,.12), inset 0 1px 0 rgba(255,255,255,.18)" },
    default: { background: "var(--surface)", color: "var(--text)", borderColor: "var(--border)" },
    ghost: { background: "transparent", color: "var(--text-2)" },
    soft: { background: "var(--accent-soft)", color: "var(--accent-ink)" },
    danger: { background: "transparent", color: "var(--danger)", borderColor: "color-mix(in oklch, var(--danger) 30%, transparent)" },
  };
  return (
    <button
      {...rest}
      onMouseDown={(e) => (e.currentTarget.style.transform = "translateY(1px)")}
      onMouseUp={(e) => (e.currentTarget.style.transform = "")}
      onMouseLeave={(e) => (e.currentTarget.style.transform = "")}
      style={{ ...base, ...sizes[size], ...variants[variant], ...style }}
      data-variant={variant}
    >
      {icon && <Icon name={icon} size={size === "lg" ? 19 : 17} />}
      {children}
      {iconRight && <Icon name={iconRight} size={size === "lg" ? 19 : 17} />}
    </button>
  );
}

// ---- Type badge (attribute type) ----
function TypeChip({ type, withLabel = true }) {
  return (
    <span style={{ display: "inline-flex", alignItems: "center", gap: 5, fontFamily: "var(--mono)", fontSize: 11, fontWeight: 500,
      color: "var(--text-3)", background: "var(--chip)", border: "1px solid var(--border)", borderRadius: 6, padding: "2px 6px", letterSpacing: ".02em" }}>
      <Icon name={typeIcon(type)} size={12} />
      {withLabel && type}
    </span>
  );
}

// ---- Identifier badge ----
function IdBadge({ compact }) {
  return (
    <span title="Identifier — used to detect duplicates"
      style={{ display: "inline-flex", alignItems: "center", gap: 4, fontFamily: "var(--mono)", fontSize: 10.5, fontWeight: 600,
        color: "var(--accent-ink)", background: "var(--accent-soft)", border: "1px solid color-mix(in oklch, var(--accent) 24%, transparent)",
        borderRadius: 5, padding: compact ? "1px 4px" : "2px 6px", letterSpacing: ".03em", textTransform: "uppercase" }}>
      <Icon name="key" size={11} />
      {!compact && "ID"}
    </span>
  );
}

// ---- Status pill (attachment / processing) ----
const STATUS_META = {
  COMPLETED: { label: "Completed", color: "var(--ok)", icon: "checkCircle" },
  PROCESSING: { label: "Processing", color: "var(--warn)", icon: "spinner" },
  FAILED: { label: "Failed", color: "var(--danger)", icon: "alert" },
};
function StatusPill({ status, size = "md" }) {
  const m = STATUS_META[status] || STATUS_META.PROCESSING;
  const sm = size === "sm";
  return (
    <span style={{ display: "inline-flex", alignItems: "center", gap: 6, fontFamily: "var(--ui)", fontSize: sm ? 11.5 : 12.5, fontWeight: 560,
      color: m.color, background: `color-mix(in oklch, ${m.color} 12%, transparent)`,
      border: `1px solid color-mix(in oklch, ${m.color} 26%, transparent)`, borderRadius: 999, padding: sm ? "2px 9px 2px 7px" : "4px 12px 4px 9px" }}>
      <Icon name={m.icon} size={sm ? 12 : 14} className={status === "PROCESSING" ? "ss-spin" : ""} />
      {m.label}
    </span>
  );
}

// ---- Section glyph tile (rounded-square icon w/ accent) ----
function SectionGlyph({ section, size = 48, radius }) {
  const r = radius != null ? radius : Math.round(size * 0.28);
  return (
    <div style={{ width: size, height: size, borderRadius: r, flex: "none",
      display: "grid", placeItems: "center",
      background: `color-mix(in oklch, ${section.accent} 16%, var(--surface))`,
      color: section.accent,
      border: `1px solid color-mix(in oklch, ${section.accent} 30%, transparent)`,
      boxShadow: `inset 0 1px 0 color-mix(in oklch, #fff 40%, transparent)` }}>
      <Icon name={section.icon} size={Math.round(size * 0.5)} sw={1.5} />
    </div>
  );
}

// ---- Empty cell placeholder ----
function EmptyCell() {
  return <span style={{ color: "var(--text-4)", fontFamily: "var(--mono)", fontSize: 12 }}>—</span>;
}

// ---- Modal shell ----
function Modal({ open, onClose, children, width = 560, labelledBy }) {
  React.useEffect(() => {
    if (!open) return;
    const h = (e) => e.key === "Escape" && onClose();
    window.addEventListener("keydown", h);
    return () => window.removeEventListener("keydown", h);
  }, [open, onClose]);
  if (!open) return null;
  return (
    <div onMouseDown={onClose} style={{ position: "fixed", inset: 0, zIndex: 80, display: "grid", placeItems: "center", padding: 24,
      background: "color-mix(in oklch, var(--shadow-ink) 42%, transparent)", backdropFilter: "blur(3px)", animation: "ss-fade .16s ease" }}>
      <div role="dialog" aria-modal="true" aria-labelledby={labelledBy} onMouseDown={(e) => e.stopPropagation()}
        style={{ width, maxWidth: "100%", maxHeight: "calc(100vh - 48px)", display: "flex", flexDirection: "column",
          background: "var(--surface)", border: "1px solid var(--border)", borderRadius: 18,
          boxShadow: "0 24px 60px -12px color-mix(in oklch, var(--shadow-ink) 50%, transparent), 0 2px 6px color-mix(in oklch, var(--shadow-ink) 20%, transparent)",
          animation: "ss-pop .18s cubic-bezier(.2,.8,.3,1)" }}>
        {children}
      </div>
    </div>
  );
}

// ---- Toast stack ----
function Toasts({ toasts, onDismiss }) {
  return (
    <div style={{ position: "fixed", right: 22, bottom: 22, zIndex: 90, display: "flex", flexDirection: "column", gap: 10, alignItems: "flex-end" }}>
      {toasts.map((t) => (
        <div key={t.id} style={{ display: "flex", alignItems: "center", gap: 11, minWidth: 280, maxWidth: 380,
          background: "var(--surface)", border: "1px solid var(--border)", borderRadius: 12, padding: "12px 14px",
          boxShadow: "0 12px 30px -8px color-mix(in oklch, var(--shadow-ink) 38%, transparent)", animation: "ss-slide-in .22s cubic-bezier(.2,.8,.3,1)" }}>
          <div style={{ width: 30, height: 30, flex: "none", borderRadius: 8, display: "grid", placeItems: "center",
            color: t.tone === "ok" ? "var(--ok)" : t.tone === "error" ? "var(--danger)" : "var(--accent)",
            background: `color-mix(in oklch, ${t.tone === "ok" ? "var(--ok)" : t.tone === "error" ? "var(--danger)" : "var(--accent)"} 13%, transparent)` }}>
            <Icon name={t.icon || "sparkle"} size={16} className={t.spin ? "ss-spin" : ""} />
          </div>
          <div style={{ flex: 1, minWidth: 0 }}>
            <div style={{ fontSize: 13.5, fontWeight: 580, color: "var(--text)" }}>{t.title}</div>
            {t.body && <div style={{ fontSize: 12.5, color: "var(--text-3)", marginTop: 1 }}>{t.body}</div>}
          </div>
          <button onClick={() => onDismiss(t.id)} style={{ background: "none", border: "none", cursor: "pointer", color: "var(--text-4)", padding: 2, display: "grid", placeItems: "center" }}>
            <Icon name="close" size={15} />
          </button>
        </div>
      ))}
    </div>
  );
}

// ---- Confirm / danger modal ----
function ConfirmModal({ open, onClose, onConfirm, title, body, confirmLabel = "Delete", icon = "trash" }) {
  return (
    <Modal open={open} onClose={onClose} width={420} labelledBy="cm-title">
      <div style={{ padding: "24px 24px 20px", display: "flex", gap: 14, alignItems: "flex-start" }}>
        <div style={{ width: 42, height: 42, flex: "none", borderRadius: 12, display: "grid", placeItems: "center",
          background: "color-mix(in oklch, var(--danger) 12%, transparent)", color: "var(--danger)" }}>
          <Icon name={icon} size={21} />
        </div>
        <div style={{ flex: 1, minWidth: 0 }}>
          <h2 id="cm-title" style={{ fontSize: 16.5, fontWeight: 630, color: "var(--text)", margin: "0 0 7px", letterSpacing: "-.01em" }}>{title}</h2>
          <p style={{ fontSize: 13.5, color: "var(--text-3)", margin: 0, lineHeight: 1.55 }}>{body}</p>
        </div>
      </div>
      <div style={{ display: "flex", gap: 9, justifyContent: "flex-end", padding: "14px 24px 20px", borderTop: "1px solid var(--border)" }}>
        <Button variant="ghost" onClick={onClose}>Cancel</Button>
        <Button variant="danger" onClick={() => { onConfirm(); onClose(); }}>{confirmLabel}</Button>
      </div>
    </Modal>
  );
}

Object.assign(window, { fmtValue, Button, TypeChip, IdBadge, StatusPill, STATUS_META, SectionGlyph, EmptyCell, Modal, ConfirmModal, Toasts });
