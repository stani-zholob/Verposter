package de.hsos.vs.wordservice;

import de.hsos.vs.web.entities.Topic;
import de.hsos.vs.web.entities.Word;


import de.hsos.vs.wordservice.db.Database;
import de.hsos.vs.wordservice.db.TopicDAO;
import de.hsos.vs.wordservice.db.UserDAO;
import de.hsos.vs.wordservice.db.WordDAO;
//import de.hsos.vs.wordservice.db.WordDAO;

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
            Topic clashRoyale = topicDAO.insert("Clash Royale");
            wordDAO.insert(clashRoyale.getId(), "Prinzessin", "Karte hat weite Entfernung");
            wordDAO.insert(clashRoyale.getId(), "Bowler", "Rollt etwas");
            wordDAO.insert(clashRoyale.getId(), "Barbar", "Klassische Clash Universe Figur");
            wordDAO.insert(clashRoyale.getId(), "Tornado", "Bekannt für King Tower Activation");
            wordDAO.insert(clashRoyale.getId(), "Mega Ritter", "Bekannt für No Skill");


            userDAO.insert("admin", "admin");
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
