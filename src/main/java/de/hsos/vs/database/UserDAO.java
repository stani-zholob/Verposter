package de.hsos.vs.database;

import de.hsos.vs.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

/**
 * Datenbankzugriff auf die Tabelle users, wird für Anmeldung
 * und Registrierung benutzt.
 *
 * @author Lukas
 */
public class UserDAO {

    public User insert(String name, String password) throws SQLException {
        String sql = "INSERT INTO users(name, password) VALUES(?, ?)";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, password);
            pstmt.executeUpdate();

            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid()")) {

                if (rs.next()) {
                    return new User(rs.getInt(1), name, password);
                }
            }
        }
        throw new SQLException("Fehler mit User ID");
    }

    public Optional<User> findByName(String name) throws SQLException {
        String sql = "SELECT id, name, password FROM users WHERE name = ?";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(map(rs));
                }
                return Optional.empty();
            }
        }
    }

    public Optional<User> findById(int id) throws SQLException {
        String sql = "SELECT id, name, password FROM users WHERE id = ?";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(map(rs));
                }
                return Optional.empty();
            }
        }
    }

    private User map(ResultSet rs) throws SQLException {
        return new User(rs.getInt("id"), rs.getString("name"), rs.getString("password"));
    }
}
