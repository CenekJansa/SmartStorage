-- Migration Script: Convert metadata_json column from TEXT to JSONB
-- 
-- Purpose: Fix PostgreSQL operator error where TEXT column cannot use JSONB operators like ->>
-- This script converts the metadata_json column in storage_item table from TEXT to JSONB type.
--
-- IMPORTANT: 
-- 1. Backup your database before running this migration!
-- 2. This migration assumes all existing TEXT data is valid JSON (which should be true if JsonMapConverter was used)
-- 3. Run this script manually against your PostgreSQL database
--
-- To run this migration:
--   psql -U user -d securstorage -f src/main/resources/sql/migrate-metadata-to-jsonb.sql
--
-- Or using Docker:
--   docker exec -i securestorage-postgres psql -U user -d securstorage < src/main/resources/sql/migrate-metadata-to-jsonb.sql
--
-- ============================================================================

-- Start transaction for safety
BEGIN;

-- Step 1: Drop the existing GIN index (if it exists)
-- The index was created for JSONB but won't work on TEXT column
DROP INDEX IF EXISTS idx_storage_item_metadata_gin;

-- Step 2: Convert the column type from TEXT to JSONB
-- The USING clause casts the TEXT data to JSONB
-- This will fail if any existing data is not valid JSON
ALTER TABLE storage_item 
ALTER COLUMN metadata_json TYPE JSONB 
USING metadata_json::jsonb;

-- Step 3: Recreate the GIN index on the JSONB column
-- GIN (Generalized Inverted Index) is optimized for JSONB data types in PostgreSQL
-- This enables efficient searches on metadata attributes like VIN, Registration Number, etc.
CREATE INDEX idx_storage_item_metadata_gin 
ON storage_item 
USING GIN (metadata_json);

-- Commit the transaction
COMMIT;

-- ============================================================================
-- ROLLBACK INSTRUCTIONS (if needed):
-- ============================================================================
-- If you need to rollback this migration, run the following:
--
-- BEGIN;
-- DROP INDEX IF EXISTS idx_storage_item_metadata_gin;
-- ALTER TABLE storage_item 
-- ALTER COLUMN metadata_json TYPE TEXT 
-- USING metadata_json::text;
-- COMMIT;
--
-- Note: Rolling back will lose the JSONB benefits and the ->> operator will fail again.
-- ============================================================================

-- Verification query: Check the column type
-- Run this to verify the migration was successful:
-- SELECT column_name, data_type 
-- FROM information_schema.columns 
-- WHERE table_name = 'storage_item' AND column_name = 'metadata_json';
--
-- Expected result: data_type should be 'jsonb'

