package de.hsos.vs.wordservice.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Zentraler Zugriff auf die SQLite-Datenbank.
 * Der Pfad laesst sich ueber -Dverposter.db=/pfad/zur/datei.db ueberschreiben,
 * weil das Arbeitsverzeichnis unter Tomcat nicht die Projektwurzel ist.
 */
public class Database {

    private static final String DB_PATH =
            System.getProperty("verposter.db", "db/verposter.db");

    private Database() {
    }
    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("SQLite JDBC driver not found", e);
        }
    }

    public static Connection connect() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
        // SQLite prueft Fremdschluessel nur, wenn es pro Verbindung eingeschaltet wird
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON");
        }
        return conn;
    }

    /** Legt die Tabellen an, falls sie noch nicht existieren. */
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

            stmt.execute("CREATE INDEX IF NOT EXISTS idx_words_topic ON words(topicId)");
        }
    }
}