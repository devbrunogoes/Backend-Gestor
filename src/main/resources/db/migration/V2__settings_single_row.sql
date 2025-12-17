-- Enforce single-row constraint for settings using a unique index on a constant expression
CREATE UNIQUE INDEX IF NOT EXISTS settings_one_row ON settings ((1));

