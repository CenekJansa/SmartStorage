# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Start infrastructure (Postgres, RabbitMQ, MinIO)
docker compose up

# Run the application
./mvnw spring-boot:run

# Build
./mvnw clean package

# Run tests
./mvnw test
```

Before running the app, copy `.env.example` to `.env` and fill in `GEMINI_API_KEY` and `GOOGLE_GEN_AI_PROJECT_ID`.

Access the GraphQL playground at `http://localhost:8080/graphiql` once running.

**Test scripts** in `scripts/`:
- `upload-file.sh` / `upload-toll.sh` — send multipart GraphQL upload requests
- `reset-db-scheme.sh` — tears down and removes the Postgres Docker volume (destructive)

## Architecture

This is a Spring Boot 3 / Java 25 application. The main value proposition is: user defines a **StorageSection** with named attributes, then uploads PDFs — the app uses AI (Gemini 2.5 Flash via Spring AI) to extract those attributes from each document and store them as structured **StorageItem** records.

### Domain model

- **StorageSection** — a user-defined schema: a name, a list of `attributes` (strings), and a list of `uniqueKeys` (subset of attributes used for deduplication). Attributes/uniqueKeys are stored as JSON TEXT columns via `JsonListConverter`.
- **StorageItem** — a structured record with a name and a `metadata` JSONB map (`Map<String, Object>`) extracted from a document. Belongs to one StorageSection.
- **StorageItemAttachment** — links a raw file (stored in MinIO) to a StorageItem. Has an `AttachmentStatus` (PENDING → COMPLETED / FAILED).

### Document upload flow (async)

1. GraphQL mutation `uploadDocument(sectionId, file)` hits `StorageItemController`.
2. The file is saved to MinIO (`MinioService`), a `StorageItemAttachment` is persisted with status PENDING, and a `DocumentProcessingMessage` (containing raw file bytes) is published to RabbitMQ via `DocumentProcessingProducer`.
3. `DocumentProcessingConsumer` picks up the message and delegates to `DocumentProcessingService`.
4. `DocumentProcessingService` orchestrates:
   - PDF → text via `PdfTextExtractionService` (Apache PDFBox)
   - Text + section attributes → AI prompt via `AiGateway` → Gemini API call
   - AI JSON response cleaned (`AiResponseCleaner`), validated (`AiResponseValidator`), and parsed into a `StorageItem` via `StorageItemFactory`
   - Duplicate detection: for each `uniqueKey` in the section, query Postgres JSONB for an existing item with matching value. If found, merge metadata; otherwise create new.
   - Attachment status updated to COMPLETED or FAILED.

### Package layout

```
com.example.SecureStorage/
  commons/         OperationResult<T> — used as return type everywhere instead of exceptions
  config/          Spring beans: RabbitMQ, MinIO, Jackson, AI prompt templates
  domain/
    controller/    GraphQL controllers + *Kit helpers (input mapping/validation)
    entity/        JPA entities + StorageItemFactory
    repository/    Spring Data JPA repositories
    service/       Service interfaces + *Impl + *Kit helpers
  infrastructure/
    ai/            AiGateway, AiGatewayImpl, AiPromptBuilder, AiResponseCleaner, AiResponseValidator
    storage/       MinioService
    PdfTextExtractionService
  messaging/       DocumentProcessingProducer, DocumentProcessingConsumer, DocumentProcessingMessage
  utils/           JPA AttributeConverters for JSON columns
```

### Key patterns

- **`OperationResult<T>`** — every method that can fail returns this instead of throwing. Check `isSuccess()` before accessing `getData()`.
- **`*Kit` classes** — controller and service logic is split: the main class handles the primary concern, Kit helpers handle input mapping, validation, or query logic.
- **AI prompts are fully configurable** via `application.yaml` under `spring.ai.prompt.*` — no code changes needed to tune prompt text.
- **JSONB search** — `StorageItemRepository.findByMetadataAttribute` queries the `metadata_json` JSONB column directly. Performance note: this is slow at scale; Elasticsearch is planned as a replacement.
- **Hibernate DDL** is set to `update` — schema evolves automatically. Manual SQL migrations in `src/main/resources/sql/` are run once at startup via `spring.sql.init`.
- Several Spring AI auto-configurations are explicitly excluded in `application.yaml` (Weaviate vector store, embedding) because those integrations are not yet active.
