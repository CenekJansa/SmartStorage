/* SmartStorage — line icon set. Stroke icons, 1.6 width, currentColor. */
const Icon = ({ name, size = 18, sw = 1.6, style, ...rest }) => {
  const p = { width: size, height: size, viewBox: "0 0 24 24", fill: "none", strokeWidth: sw, strokeLinecap: "round", strokeLinejoin: "round", style, ...rest, stroke: "currentColor" };
  const paths = {
    receipt: <><path d="M5 3v18l2-1.2 2 1.2 2-1.2 2 1.2 2-1.2 2 1.2V3l-2 1.2L17 3l-2 1.2L13 3l-2 1.2L9 3 7 4.2 5 3Z"/><path d="M8 8h8M8 12h8M8 16h5"/></>,
    contract: <><path d="M7 3h7l5 5v13H7z"/><path d="M14 3v5h5"/><path d="M10 13h6M10 17h4"/></>,
    user: <><circle cx="12" cy="8" r="3.4"/><path d="M5.5 20a6.5 6.5 0 0 1 13 0"/></>,
    wallet: <><rect x="3" y="6" width="18" height="13" rx="2.4"/><path d="M3 10h18"/><circle cx="16.5" cy="13.5" r="1.1" fill="currentColor" stroke="none"/></>,
    book: <><path d="M5 4.5A2.5 2.5 0 0 1 7.5 2H19v17H7.5A2.5 2.5 0 0 0 5 21.5z"/><path d="M5 19.5V4.5"/></>,
    grid: <><rect x="3" y="3" width="7" height="7" rx="1.6"/><rect x="14" y="3" width="7" height="7" rx="1.6"/><rect x="3" y="14" width="7" height="7" rx="1.6"/><rect x="14" y="14" width="7" height="7" rx="1.6"/></>,
    folder: <><path d="M3 7a2 2 0 0 1 2-2h4l2 2.5h8a2 2 0 0 1 2 2V18a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/></>,
    plus: <><path d="M12 5v14M5 12h14"/></>,
    upload: <><path d="M12 16V4"/><path d="m7 9 5-5 5 5"/><path d="M5 16v3a1 1 0 0 0 1 1h12a1 1 0 0 0 1-1v-3"/></>,
    file: <><path d="M7 3h7l5 5v13H7z"/><path d="M14 3v5h5"/></>,
    filePdf: <><path d="M7 3h7l5 5v13H7z"/><path d="M14 3v5h5"/><path d="M9.2 17.8v-3.2h.9a1 1 0 0 1 0 2h-.9M13 14.6v3.2m0-3.2h1.4m-1.4 1.6h1M16.8 14.6h1.3"/></>,
    download: <><path d="M12 4v12"/><path d="m7 11 5 5 5-5"/><path d="M5 20h14"/></>,
    chevronRight: <><path d="m9 6 6 6-6 6"/></>,
    chevronLeft: <><path d="m15 6-6 6 6 6"/></>,
    arrowLeft: <><path d="M19 12H5"/><path d="m12 19-7-7 7-7"/></>,
    search: <><circle cx="11" cy="11" r="7"/><path d="m21 21-4.3-4.3"/></>,
    key: <><circle cx="8" cy="15" r="4"/><path d="m10.8 12.2 8.2-8.2M16 5l3 3M14 7l2.5 2.5"/></>,
    close: <><path d="M6 6l12 12M18 6 6 18"/></>,
    check: <><path d="m4 12 5 5L20 6"/></>,
    checkCircle: <><circle cx="12" cy="12" r="9"/><path d="m8.5 12 2.4 2.4 4.6-4.8"/></>,
    spinner: <><path d="M12 3a9 9 0 1 0 9 9"/></>,
    alert: <><circle cx="12" cy="12" r="9"/><path d="M12 7.5v5M12 16h.01"/></>,
    type: <><path d="M4 7V5h16v2M9 19h6M12 5v14"/></>,
    calendar: <><rect x="3.5" y="4.5" width="17" height="16" rx="2"/><path d="M3.5 9h17M8 3v3M16 3v3"/></>,
    hash: <><path d="M9 4 7 20M17 4l-2 16M5 9h14M4 15h14"/></>,
    trash: <><path d="M4 7h16M9 7V5a1 1 0 0 1 1-1h4a1 1 0 0 1 1 1v2M6 7l1 13a1 1 0 0 0 1 1h8a1 1 0 0 0 1-1l1-13"/></>,
    sun: <><circle cx="12" cy="12" r="4"/><path d="M12 2v2M12 20v2M4 12H2M22 12h-2M5 5l1.5 1.5M17.5 17.5 19 19M19 5l-1.5 1.5M6.5 17.5 5 19"/></>,
    moon: <><path d="M20 14.5A8 8 0 1 1 9.5 4a6.5 6.5 0 0 0 10.5 10.5Z"/></>,
    sparkle: <><path d="M12 3l1.8 5.2L19 10l-5.2 1.8L12 17l-1.8-5.2L5 10l5.2-1.8L12 3Z"/></>,
    dots: <><circle cx="5" cy="12" r="1.4" fill="currentColor" stroke="none"/><circle cx="12" cy="12" r="1.4" fill="currentColor" stroke="none"/><circle cx="19" cy="12" r="1.4" fill="currentColor" stroke="none"/></>,
    layers: <><path d="m12 3 9 5-9 5-9-5 9-5Z"/><path d="m3 13 9 5 9-5"/></>,
  };
  return <svg {...p}>{paths[name] || null}</svg>;
};

const typeIcon = (t) => (t === "DATE" ? "calendar" : t === "NUMBER" ? "hash" : "type");

window.Icon = Icon;
window.typeIcon = typeIcon;
