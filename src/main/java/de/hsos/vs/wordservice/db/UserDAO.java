package de.hsos.vs.wordservice.db;

import de.hsos.vs.web.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;

public class UserDAO {

    /** Legt einen Benutzer an und gibt ihn mit der vergebenen id zurueck. */
    public User insert(String name, String passwordHash) throws SQLException {
        String sql = "INSERT INTO users(name, password) VALUES(?, ?)";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, name);
            pstmt.setString(2, passwordHash);
            pstmt.executeUpdate();

            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid()")) {

                if (rs.next()) {
                    return new User(rs.getInt(1), name, passwordHash);
                }
            }
        }
        throw new SQLException("Could not retrieve generated user ID");
    }
    /** Fuer den Login: Benutzer per Name suchen. */
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
    //public ArrayList<User> findAll() throws SQLException {

    }

    private User map(ResultSet rs) throws SQLException {
        return new User(rs.getInt("id"), rs.getString("name"), rs.getString("password"));
    }
}
