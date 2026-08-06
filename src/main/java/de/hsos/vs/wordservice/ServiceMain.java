package de.hsos.vs.wordservice;

import de.hsos.vs.web.entities.Topic;
import de.hsos.vs.web.entities.Word;
import de.hsos.vs.wordservice.db.Database;
import de.hsos.vs.wordservice.db.TopicDAO;
import de.hsos.vs.wordservice.db.UserDAO;
import de.hsos.vs.wordservice.db.WordDAO;

import java.sql.SQLException;
import java.util.List;

/**
 * Legt das Schema an und befuellt es einmalig mit Beispieldaten.
 * Einmal per Hand starten, danach steht db/verposter.db bereit.
 */
public class ServiceMain {

    public static void main(String[] args) throws SQLException {
        Database.init();
        System.out.println("Schema angelegt.");

        TopicDAO topicDAO = new TopicDAO();
        WordDAO wordDAO = new WordDAO();
        UserDAO userDAO = new UserDAO();

        if (topicDAO.findAll().isEmpty()) {
            Topic tiere = topicDAO.insert("Tiere");
            wordDAO.insert(tiere.getId(), "Elefant", "grosses graues Tier");
            wordDAO.insert(tiere.getId(), "Pinguin", "kann nicht fliegen");

            Topic berufe = topicDAO.insert("Berufe");
            wordDAO.insert(berufe.getId(), "Baecker", "steht frueh auf");

            userDAO.insert("testuser", "nochNichtGehasht");
            System.out.println("Beispieldaten eingefuegt.");
        }

        for (Topic topic : topicDAO.findAll()) {
            List<Word> words = wordDAO.findByTopic(topic.getId());
            System.out.println(topic.getName() + " (" + words.size() + " Woerter)");
            for (Word word : words) {
                System.out.println("  " + word.getWord() + " - " + word.getTip());
            }
        }
    }
}
