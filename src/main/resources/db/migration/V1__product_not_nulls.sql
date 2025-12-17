-- Ensure non-null and defaults for product type/unit
UPDATE products SET type = 'FABRICADO' WHERE type IS NULL;
UPDATE products SET unit = 'UN' WHERE unit IS NULL;
ALTER TABLE products ALTER COLUMN type SET DEFAULT 'FABRICADO';
ALTER TABLE products ALTER COLUMN type SET NOT NULL;
ALTER TABLE products ALTER COLUMN unit SET DEFAULT 'UN';
ALTER TABLE products ALTER COLUMN unit SET NOT NULL;

