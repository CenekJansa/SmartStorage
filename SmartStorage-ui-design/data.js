// SmartStorage sample data — plain globals (loaded before Babel scripts)
// Data model:
//   StorageSection { id, name, icon, accent, attributes:[{name,type,identifier}] }
//   StorageItem    { id, name, metadata:{[attr]:value}, attachments:[{fileName,status}] }
//   status: PROCESSING | COMPLETED | FAILED

(function () {
  const T = { STRING: "STRING", DATE: "DATE", NUMBER: "NUMBER" };

  // Per-section accent hues (oklch, shared L/C so they harmonize with the purple brand)
  const ACCENT = {
    purple: "oklch(0.64 0.19 300)",
    blue: "oklch(0.64 0.16 255)",
    teal: "oklch(0.64 0.13 205)",
    green: "oklch(0.64 0.15 155)",
    amber: "oklch(0.66 0.15 75)",
    rose: "oklch(0.64 0.18 5)",
  };

  const sections = [
    {
      id: "sec_invoices",
      name: "Invoices",
      icon: "receipt",
      accent: ACCENT.purple,
      attributes: [
        { name: "Invoice No", type: T.STRING, identifier: true },
        { name: "Vendor", type: T.STRING, identifier: false },
        { name: "Issue Date", type: T.DATE, identifier: false },
        { name: "Due Date", type: T.DATE, identifier: false },
        { name: "Amount", type: T.NUMBER, identifier: false },
        { name: "Tax", type: T.NUMBER, identifier: false },
      ],
    },
    {
      id: "sec_contracts",
      name: "Contracts",
      icon: "contract",
      accent: ACCENT.blue,
      attributes: [
        { name: "Contract ID", type: T.STRING, identifier: true },
        { name: "Counterparty", type: T.STRING, identifier: false },
        { name: "Effective Date", type: T.DATE, identifier: false },
        { name: "Term (mo)", type: T.NUMBER, identifier: false },
        { name: "Annual Value", type: T.NUMBER, identifier: false },
      ],
    },
    {
      id: "sec_candidates",
      name: "Candidates",
      icon: "user",
      accent: ACCENT.teal,
      attributes: [
        { name: "Email", type: T.STRING, identifier: true },
        { name: "Full Name", type: T.STRING, identifier: false },
        { name: "Role Applied", type: T.STRING, identifier: false },
        { name: "Years Exp", type: T.NUMBER, identifier: false },
        { name: "Applied", type: T.DATE, identifier: false },
      ],
    },
    {
      id: "sec_receipts",
      name: "Expense Receipts",
      icon: "wallet",
      accent: ACCENT.amber,
      attributes: [
        { name: "Receipt ID", type: T.STRING, identifier: true },
        { name: "Merchant", type: T.STRING, identifier: false },
        { name: "Date", type: T.DATE, identifier: false },
        { name: "Category", type: T.STRING, identifier: false },
        { name: "Total", type: T.NUMBER, identifier: false },
      ],
    },
    {
      id: "sec_papers",
      name: "Research Papers",
      icon: "book",
      accent: ACCENT.green,
      attributes: [
        { name: "DOI", type: T.STRING, identifier: true },
        { name: "Title", type: T.STRING, identifier: false },
        { name: "Lead Author", type: T.STRING, identifier: false },
        { name: "Year", type: T.NUMBER, identifier: false },
      ],
    },
  ];

  let _id = 0;
  const uid = (p) => `${p}_${(++_id).toString(36)}${Date.now().toString(36).slice(-3)}`;

  const mkAtt = (fileName, status) => ({ id: uid("att"), fileName, status });

  const items = {
    sec_invoices: [
      {
        id: "itm_inv_1", name: "INV-2041",
        metadata: { "Invoice No": "INV-2041", Vendor: "Northwind Traders", "Issue Date": "2026-03-02", "Due Date": "2026-04-01", Amount: 12480.0, Tax: 2620.8 },
        attachments: [mkAtt("northwind-INV-2041.pdf", "COMPLETED")],
      },
      {
        id: "itm_inv_2", name: "INV-2042",
        metadata: { "Invoice No": "INV-2042", Vendor: "Acme Industrial", "Issue Date": "2026-03-05", "Due Date": "2026-04-04", Amount: 3890.5, Tax: 817.0 },
        attachments: [mkAtt("acme_invoice_mar.pdf", "COMPLETED")],
      },
      {
        id: "itm_inv_3", name: "INV-2043",
        metadata: { "Invoice No": "INV-2043", Vendor: "Globex Corp", "Issue Date": "2026-03-09", "Due Date": null, Amount: 540.0, Tax: 113.4 },
        attachments: [mkAtt("globex-scan.pdf", "COMPLETED"), mkAtt("globex-addendum.pdf", "FAILED")],
      },
      {
        id: "itm_inv_4", name: "INV-2044",
        metadata: { "Invoice No": "INV-2044", Vendor: "Initech", "Issue Date": "2026-03-12", "Due Date": "2026-04-11", Amount: 27750.0, Tax: 5827.5 },
        attachments: [mkAtt("initech-q1.pdf", "COMPLETED")],
      },
      {
        id: "itm_inv_5", name: "INV-2045",
        metadata: { "Invoice No": "INV-2045", Vendor: "Soylent Foods", "Issue Date": "2026-03-15", "Due Date": "2026-04-14", Amount: 1120.25, Tax: null },
        attachments: [mkAtt("soylent-receipt.pdf", "COMPLETED")],
      },
      {
        id: "itm_inv_6", name: "INV-2046",
        metadata: { "Invoice No": "INV-2046", Vendor: "Umbrella Supplies", "Issue Date": "2026-03-18", "Due Date": "2026-04-17", Amount: 8650.0, Tax: 1816.5 },
        attachments: [mkAtt("umbrella-mar.pdf", "COMPLETED")],
      },
      {
        id: "itm_inv_7", name: "INV-2047",
        metadata: { "Invoice No": "INV-2047", Vendor: "Wayne Enterprises", "Issue Date": "2026-03-22", "Due Date": "2026-04-21", Amount: 45200.0, Tax: 9492.0 },
        attachments: [mkAtt("wayne-ent-2047.pdf", "COMPLETED"), mkAtt("wayne-po.pdf", "COMPLETED")],
      },
      {
        id: "itm_inv_8", name: "INV-2048",
        metadata: { "Invoice No": "INV-2048", Vendor: "Stark Industries", "Issue Date": "2026-03-26", "Due Date": "2026-04-25", Amount: null, Tax: null },
        attachments: [mkAtt("stark-blurry-scan.pdf", "FAILED")],
      },
    ],
    sec_contracts: [
      {
        id: "itm_con_1", name: "MSA-Northwind",
        metadata: { "Contract ID": "C-1001", Counterparty: "Northwind Traders", "Effective Date": "2025-11-01", "Term (mo)": 24, "Annual Value": 144000 },
        attachments: [mkAtt("northwind-MSA-signed.pdf", "COMPLETED")],
      },
      {
        id: "itm_con_2", name: "SOW-Globex",
        metadata: { "Contract ID": "C-1002", Counterparty: "Globex Corp", "Effective Date": "2026-01-15", "Term (mo)": 12, "Annual Value": 86000 },
        attachments: [mkAtt("globex-sow-2026.pdf", "COMPLETED")],
      },
      {
        id: "itm_con_3", name: "NDA-Initech",
        metadata: { "Contract ID": "C-1003", Counterparty: "Initech", "Effective Date": "2026-02-20", "Term (mo)": 36, "Annual Value": null },
        attachments: [mkAtt("initech-nda.pdf", "COMPLETED")],
      },
      {
        id: "itm_con_4", name: "MSA-Wayne",
        metadata: { "Contract ID": "C-1004", Counterparty: "Wayne Enterprises", "Effective Date": "2025-09-10", "Term (mo)": 48, "Annual Value": 520000 },
        attachments: [mkAtt("wayne-master-agreement.pdf", "COMPLETED"), mkAtt("wayne-exhibit-a.pdf", "COMPLETED")],
      },
    ],
    sec_candidates: [
      {
        id: "itm_can_1", name: "A. Okafor",
        metadata: { Email: "ada.okafor@mail.com", "Full Name": "Ada Okafor", "Role Applied": "Senior Backend Eng", "Years Exp": 8, Applied: "2026-05-12" },
        attachments: [mkAtt("ada-okafor-cv.pdf", "COMPLETED")],
      },
      {
        id: "itm_can_2", name: "M. Lindqvist",
        metadata: { Email: "m.lindqvist@mail.com", "Full Name": "Mira Lindqvist", "Role Applied": "Product Designer", "Years Exp": 6, Applied: "2026-05-14" },
        attachments: [mkAtt("mira-portfolio-resume.pdf", "COMPLETED")],
      },
      {
        id: "itm_can_3", name: "R. Banerjee",
        metadata: { Email: "rohan.b@mail.com", "Full Name": "Rohan Banerjee", "Role Applied": "Data Scientist", "Years Exp": 4, Applied: "2026-05-18" },
        attachments: [mkAtt("rohan-resume.pdf", "COMPLETED"), mkAtt("rohan-coverletter.pdf", "COMPLETED")],
      },
      {
        id: "itm_can_4", name: "Unknown",
        metadata: { Email: "j.doe@mail.com", "Full Name": null, "Role Applied": "Product Designer", "Years Exp": null, Applied: "2026-05-20" },
        attachments: [mkAtt("scan_001.pdf", "FAILED")],
      },
    ],
    sec_receipts: [],
    sec_purchase_orders: [
      {
        id: "itm_po_1", name: "PO-7701",
        metadata: { "PO Number": "PO-7701", Supplier: "Northwind Traders", "Issue Date": "2026-02-10", "Delivery Date": "2026-03-05", "Item Description": "Stainless Steel Fasteners (Bulk)", Quantity: 4800, "Unit Price": 0.42, "Total Amount": 2016.0, "Payment Terms": "Net 30" },
        attachments: [mkAtt("northwind-PO-7701.pdf", "COMPLETED")],
      },
      {
        id: "itm_po_2", name: "PO-7702",
        metadata: { "PO Number": "PO-7702", Supplier: "Acme Industrial", "Issue Date": "2026-02-14", "Delivery Date": "2026-03-14", "Item Description": "Industrial-Grade Solvent 20L", Quantity: 60, "Unit Price": 38.5, "Total Amount": 2310.0, "Payment Terms": "Net 15" },
        attachments: [mkAtt("acme-PO-7702.pdf", "COMPLETED")],
      },
      {
        id: "itm_po_3", name: "PO-7703",
        metadata: { "PO Number": "PO-7703", Supplier: "Globex Corp", "Issue Date": "2026-02-18", "Delivery Date": "2026-03-20", "Item Description": "Safety Goggles × 200", Quantity: 200, "Unit Price": 12.75, "Total Amount": 2550.0, "Payment Terms": "Net 30" },
        attachments: [mkAtt("globex-PO-7703.pdf", "COMPLETED"), mkAtt("globex-shipping.pdf", "COMPLETED")],
      },
      {
        id: "itm_po_4", name: "PO-7704",
        metadata: { "PO Number": "PO-7704", Supplier: "Initech", "Issue Date": "2026-03-01", "Delivery Date": null, "Item Description": "Network Patch Panels", Quantity: 12, "Unit Price": 145.0, "Total Amount": 1740.0, "Payment Terms": "Net 45" },
        attachments: [mkAtt("initech-PO-7704.pdf", "FAILED")],
      },
      {
        id: "itm_po_5", name: "PO-7705",
        metadata: { "PO Number": "PO-7705", Supplier: "Umbrella Supplies", "Issue Date": "2026-03-05", "Delivery Date": "2026-03-25", "Item Description": "PPE Kits — Full Body", Quantity: 30, "Unit Price": 87.0, "Total Amount": 2610.0, "Payment Terms": "Net 30" },
        attachments: [mkAtt("umbrella-PO-7705.pdf", "COMPLETED")],
      },
    ],
    sec_papers: [
      {
        id: "itm_pap_1", name: "Attention Routing",
        metadata: { DOI: "10.1101/2025.04.001", Title: "Sparse Attention Routing for Long Documents", "Lead Author": "L. Marchetti", Year: 2025 },
        attachments: [mkAtt("marchetti-2025-routing.pdf", "COMPLETED")],
      },
      {
        id: "itm_pap_2", name: "Retrieval Survey",
        metadata: { DOI: "10.1101/2026.01.118", Title: "A Survey of Retrieval-Augmented Extraction", "Lead Author": "K. Sato", Year: 2026 },
        attachments: [mkAtt("sato-survey.pdf", "PROCESSING")],
      },
    ],
  };

  // 9-attribute section for horizontal scroll testing
  sections.push({
    id: "sec_purchase_orders",
    name: "Purchase Orders",
    icon: "wallet",
    accent: ACCENT.rose,
    attributes: [
      { name: "PO Number",      type: T.STRING, identifier: true  },
      { name: "Supplier",       type: T.STRING, identifier: false },
      { name: "Issue Date",     type: T.DATE,   identifier: false },
      { name: "Delivery Date",  type: T.DATE,   identifier: false },
      { name: "Item Description", type: T.STRING, identifier: false },
      { name: "Quantity",       type: T.NUMBER, identifier: false },
      { name: "Unit Price",     type: T.NUMBER, identifier: false },
      { name: "Total Amount",   type: T.NUMBER, identifier: false },
      { name: "Payment Terms",  type: T.STRING, identifier: false },
    ],
  });

  window.SS = { T, ACCENT, sections, items, uid, mkAtt };
})();
