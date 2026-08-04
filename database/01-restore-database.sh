#!/bin/bash
set -e

echo "Restoring train_booking.dump into ${POSTGRES_DB}..."

pg_restore \
  --username="${POSTGRES_USER}" \
  --dbname="${POSTGRES_DB}" \
  --no-owner \
  --no-privileges \
  --verbose \
  /docker-entrypoint-initdb.d/train_booking.dump

touch /var/lib/postgresql/data/.restore-complete

echo "Database restore completed."