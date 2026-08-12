package de.hsos.vs.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Zentraler Zugriff auf die SQLite-Datenbank und Anlegen der Tabellen.
 *
 * @author Lukas
 */
public class Database {

    private static final String DB_PATH =
            System.getProperty("verposter.db", "db/verposter.db");

    private Database() {
    }

    // Der Treiber liegt in WEB-INF/lib. Der DriverManager sucht seine Treiber
    // nur einmal beim Start, deshalb muss er hier von Hand geladen werden -
    // sonst kommt zur Laufzeit "No suitable driver found for jdbc:sqlite".
    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("SQLite Treiber nicht gefunden", e);
        }
    }

    /**
     * erstellt die Verbindung
     */
    public static Connection connect() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
        // SQLite prüft Fremdschlüssel nur, wenn es pro Verbindung eingeschaltet wird
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON");
        }
        return conn;
    }

    /**
     * Legt die Tabellen an, falls sie noch nicht existieren.
     */
    public static void init() throws SQLException {
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS users (
                        id       INTEGER PRIMARY KEY AUTOINCREMENT,
                        name     VARCHAR(255) NOT NULL UNIQUE,
                        password VARCHAR(255) NOT NULL
                    )""");

            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS topics (
                        id   INTEGER PRIMARY KEY AUTOINCREMENT,
                        name VARCHAR(255) NOT NULL UNIQUE
                    )""");

            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS words (
                        id      INTEGER PRIMARY KEY AUTOINCREMENT,
                        topicId INTEGER NOT NULL,
                        word    VARCHAR(255) NOT NULL,
                        tip     VARCHAR(255),
                        FOREIGN KEY (topicId) REFERENCES topics(id) ON DELETE CASCADE
                    )""");
        }
    }
}