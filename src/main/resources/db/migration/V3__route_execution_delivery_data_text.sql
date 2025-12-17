-- Expand delivery_data to TEXT to allow large JSON payloads in checkpoints
DO $$
BEGIN
    BEGIN
        ALTER TABLE route_execution_checkpoints ALTER COLUMN delivery_data TYPE TEXT;
    EXCEPTION WHEN undefined_column THEN
        BEGIN
            ALTER TABLE route_execution_checkpoints ALTER COLUMN deliverydata TYPE TEXT;
        EXCEPTION WHEN undefined_column THEN
            RAISE NOTICE 'delivery_data/deliverydata column not found in route_execution_checkpoints, skipping';
        END;
    END;
END $$;

