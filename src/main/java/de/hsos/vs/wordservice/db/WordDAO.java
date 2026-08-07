//package de.hsos.vs.wordservice.db;
//
//import de.hsos.vs.web.entities.Word;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//public class WordDAO {
//    public Word insert(int topicId, String word, String tip) throws SQLException {
//        String sql = "INSERT INTO words(topicId, word, tip) VALUES(?, ?, ?)";
//
//        try (Connection conn = Database.connect();
//             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
//
//            pstmt.setInt(1, topicId);
//            pstmt.setString(2, word);
//            pstmt.setString(3, tip);
//            pstmt.executeUpdate();
//
//            try (ResultSet keys = pstmt.getGeneratedKeys()) {
//                keys.next();
//                return new Word(keys.getInt(1), topicId, word, tip);
//            }
//        }
//    }
//    /** Die n-Seite der Beziehung: alle Woerter eines Themas. */
//    public List<Word> findByTopic(int topicId) throws SQLException {
//        String sql = "SELECT id, topicId, word, tip FROM words WHERE topicId = ?";
//        List<Word> words = new ArrayList<>();
//
//        try (Connection conn = Database.connect();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//
//            pstmt.setInt(1, topicId);
//            try (ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    words.add(map(rs));
//                }
//            }
//        }
//        return words;
//    }
//
//    /** Zieht ein zufaelliges Wort - das braucht das Spiel beim Rundenstart. */
//    public Optional<Word> findRandomByTopic(int topicId) throws SQLException {
//        String sql = "SELECT id, topicId, word, tip FROM words WHERE topicId = ? "
//                + "ORDER BY RANDOM() LIMIT 1";
//
//        try (Connection conn = Database.connect();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//
//            pstmt.setInt(1, topicId);
//            try (ResultSet rs = pstmt.executeQuery()) {
//                if (rs.next()) {
//                    return Optional.of(map(rs));
//                }
//                return Optional.empty();
//            }
//        }
//    }
//
//    private Word map(ResultSet rs) throws SQLException {
//        return new Word(rs.getInt("id"), rs.getInt("topicId"),
//                rs.getString("word"), rs.getString("tip"));
//    }
//}
