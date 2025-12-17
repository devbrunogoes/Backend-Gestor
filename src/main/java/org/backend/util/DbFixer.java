package org.backend.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DbFixer {
    public static void main(String[] args) {
        // permite override via variáveis de ambiente
        String url = System.getenv().getOrDefault("DATABASE_URL", "jdbc:postgresql://localhost:5432/sistema_gestor");
        String user = System.getenv().getOrDefault("DATABASE_USERNAME", "postgres");
        String pass = System.getenv().getOrDefault("DATABASE_PASSWORD", "123456");

        System.out.println("Conectando a: " + url + " como " + user);
        try (Connection c = DriverManager.getConnection(url, user, pass)) {
            c.setAutoCommit(false);
            try (Statement s = c.createStatement()) {
                System.out.println("Aplicando alterações no schema da tabela users...");
                s.executeUpdate("ALTER TABLE IF EXISTS users ADD COLUMN IF NOT EXISTS active boolean");
                s.executeUpdate("UPDATE users SET active = true WHERE active IS NULL");
                s.executeUpdate("ALTER TABLE IF EXISTS users ALTER COLUMN active SET NOT NULL");
                s.executeUpdate("ALTER TABLE IF EXISTS users ALTER COLUMN active SET DEFAULT true");

                System.out.println("Aplicando alterações no schema da tabela products (type/unit)...");
                s.executeUpdate("ALTER TABLE IF EXISTS products ADD COLUMN IF NOT EXISTS type varchar(255)");
                s.executeUpdate("ALTER TABLE IF EXISTS products ADD COLUMN IF NOT EXISTS unit varchar(255)");
                s.executeUpdate("UPDATE products SET type = 'FABRICADO' WHERE type IS NULL");
                s.executeUpdate("UPDATE products SET unit = 'UN' WHERE unit IS NULL");
                s.executeUpdate("ALTER TABLE IF EXISTS products ALTER COLUMN type SET NOT NULL");
                s.executeUpdate("ALTER TABLE IF EXISTS products ALTER COLUMN type SET DEFAULT 'FABRICADO'");
                s.executeUpdate("ALTER TABLE IF EXISTS products ALTER COLUMN unit SET NOT NULL");
                s.executeUpdate("ALTER TABLE IF EXISTS products ALTER COLUMN unit SET DEFAULT 'UN'");

                System.out.println("Aumentando capacidade de route_execution_checkpoints.delivery_data para TEXT...");
                s.executeUpdate("ALTER TABLE IF EXISTS route_execution_checkpoints ALTER COLUMN delivery_data TYPE TEXT");

                System.out.println("Renomeando delivery_data para z_delivery_data em route_execution_checkpoints (se existir)...");
                s.executeUpdate("DO $$ BEGIN IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='route_execution_checkpoints' AND column_name='delivery_data') THEN EXECUTE 'ALTER TABLE route_execution_checkpoints RENAME COLUMN delivery_data TO z_delivery_data'; END IF; END $$;");

                c.commit();
                System.out.println("Alterações aplicadas com sucesso.");
            } catch (SQLException ex) {
                System.err.println("Erro ao aplicar alterações: " + ex.getMessage());
                try { c.rollback(); } catch (SQLException e) { /* ignore */ }
                System.exit(2);
            }
        } catch (SQLException e) {
            System.err.println("Falha ao conectar ou executar SQL: " + e.getMessage());
            System.exit(1);
        }
        System.out.println("Pronto.");
    }
}
