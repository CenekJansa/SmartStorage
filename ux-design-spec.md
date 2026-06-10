# SmartStorage – Web UI Design Spec

## 1. Product summary

SmartStorage lets a user define a **Storage Section** — a named schema with a
list of attributes (fields) — and then upload PDF documents into that
section. An AI pipeline reads each PDF and extracts values for the defined
attributes, producing a **Storage Item** (a row of structured data) linked
back to the original file(s) it was extracted from.

The goal of this UI is to let a non-technical user:
1. See and organize their sections.
2. Browse the structured data extracted into each section, like a spreadsheet.
3. Drill into a single record to see and download the original source
   document(s) it came from.

This document defines the **required navigation structure and core screens**.
Visual design, layout details, component styling, and any screens/flows not
explicitly described below are at the designer's discretion.

---

## 2. Required screens & flows

### 2.1 Home page — Section grid

- This is the landing page of the application.
- All Storage Sections belonging to the user are displayed as **tiles**,
  similar to application icons on an OS desktop (e.g. macOS Launchpad /
  Windows desktop icons).
- Each tile represents one section and should show at minimum the section
  name.
- Clicking a tile navigates to that section's table view (2.2).
- The page needs a clear way to **create a new section** (e.g. an "Add"
  tile, button, or empty-state CTA). Creating a section requires:
  - A section name.
  - A list of attributes, where each attribute has:
    - a name,
    - a type (`STRING`, `DATE`, or `NUMBER`),
    - whether it is an "identifier" field (used for de-duplication —
      worth a subtle visual distinction in the editor, e.g. a badge or icon).
- Empty state: if the user has no sections yet, the home page should
  communicate that and prompt them to create their first section.

### 2.2 Section view — Data table

- Opened by clicking a section tile from the home page.
- Displayed as a **table**:
  - **Columns** = the section's attributes, in the order they were defined.
  - **Rows** = Storage Items belonging to the section; each cell shows the
    value from that item's metadata for the corresponding attribute.
  - If an item has no value for a given attribute, the cell should render an
    empty/placeholder state (not an error).
- Column headers should reflect the attribute's type where useful (e.g.
  right-aligning numbers, formatting dates) — exact treatment is the
  designer's call.
- Attributes marked as "identifier" may be visually distinguished in the
  header (e.g. icon/badge), since they're used to detect duplicate items.
- The view needs a way to **upload a new document** into this section
  (e.g. drag-and-drop area or upload button). After upload, the document is
  processed asynchronously — the UI should communicate that processing is
  happening and not block the user (e.g. a toast/notification, a processing
  indicator, or a pending-items area).
- Provide a way to navigate back to the home page.
- Empty state: if the section has no items yet, prompt the user to upload a
  document.
- Other table affordances (search, filter, sort, pagination, column
  resizing, etc.) are at the designer's discretion — useful for sections
  with many items/attributes, but not mandatory for v1.

### 2.3 Item detail — Source files

- Opened by clicking a row in the section table.
- Shows the full set of metadata for that item (all attribute/value pairs —
  useful as a reference even if some were already visible in the table).
- Lists all **source file attachments** that contributed to this item's
  extracted data. Each attachment has:
  - a file name,
  - a processing status: `PROCESSING`, `COMPLETED`, or `FAILED`.
  - Files that failed processing should be visually distinguishable (they
    contributed no data, or only partial/erroneous data).
- Clicking a file **downloads it** (the original PDF as uploaded).
- Provide a way to navigate back to the section table.

---

## 3. Data model reference (for the designer's context)

```
StorageSection
  - name: string
  - attributes: [ { name: string, type: STRING | DATE | NUMBER, identifier: boolean } ]

StorageItem (belongs to a StorageSection)
  - name: string
  - metadata: { [attributeName]: value }   // keys should align with section attributes
  - attachments: [StorageItemAttachment]

StorageItemAttachment
  - fileName: string
  - status: PROCESSING | COMPLETED | FAILED
```

---

## 4. Out of scope for this spec

Anything not described above — visual style/branding, color palette,
typography, responsive/mobile behavior, search/filter/sort UX, notifications,
error states beyond what's noted, settings, multi-user/auth screens, etc. —
is left entirely to the designer's discretion.
