#!/bin/bash

# Script to completely reset PostgreSQL database container
# Use this when you change the database schema

echo "�️  Stopping and removing PostgreSQL container..."
docker compose down postgres

echo "🧹 Removing PostgreSQL volume (this deletes all data)..."
docker volume rm securestorage_postgres-data 2>/dev/null || echo "Volume already removed or doesn't exist"
