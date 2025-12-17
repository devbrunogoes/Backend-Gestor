-- Rename delivery_data to z_delivery_data to keep LOB column ordered last and avoid index/type confusion
DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name='route_execution_checkpoints' AND column_name='delivery_data'
    ) THEN
        EXECUTE 'ALTER TABLE route_execution_checkpoints RENAME COLUMN delivery_data TO z_delivery_data';
    END IF;
END $$;

