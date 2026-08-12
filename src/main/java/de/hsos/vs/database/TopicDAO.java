package de.hsos.vs.database;

import de.hsos.vs.entities.Topic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Datenbankzugriff auf die Tabelle topics.
 *
 * @author Lukas
 */
public class TopicDAO {

    public Topic insert(String name) throws SQLException {
        String sql = "INSERT INTO topics(name) VALUES(?)";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.executeUpdate();

            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid()")) {

                if (rs.next()) {
                    return new Topic(rs.getInt(1), name);
                }
            }
        }
        throw new SQLException("Could not retrieve generated topic ID");
    }

    /** Alle Themen - z.B. fuer die Auswahl beim Spielstart. */
    public List<Topic> findAll() throws SQLException {
        String sql = "SELECT id, name FROM topics ORDER BY name";
        List<Topic> topics = new ArrayList<>();

        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                topics.add(new Topic(rs.getInt("id"), rs.getString("name")));
            }
        }
        return topics;
    }

    public Optional<Topic> findById(int id) throws SQLException {
        String sql = "SELECT id, name FROM topics WHERE id = ?";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Topic(rs.getInt("id"), rs.getString("name")));
                }
                return Optional.empty();
            }
        }
    }
}
