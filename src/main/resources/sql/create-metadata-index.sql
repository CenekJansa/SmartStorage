-- Create GIN index on metadata_json column for fast JSONB queries
-- This index enables efficient searches on metadata attributes like VIN, Registration Number, etc.
-- GIN (Generalized Inverted Index) is optimized for JSONB data types in PostgreSQL

CREATE INDEX IF NOT EXISTS idx_storage_item_metadata_gin 
ON storage_item 
USING GIN (metadata_json);

