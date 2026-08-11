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
            //Topics mit Wörterpaaren
            Topic clashRoyale = topicDAO.insert("Clash Royale");
            wordDAO.insert(clashRoyale.getId(), "Prinzessin", "Karte hat weite Entfernung");
            wordDAO.insert(clashRoyale.getId(), "Bowler", "Rollt etwas");
            wordDAO.insert(clashRoyale.getId(), "Barbar", "Klassische Clash Universe Figur");
            wordDAO.insert(clashRoyale.getId(), "Tornado", "Bekannt für King Tower Activation");
            wordDAO.insert(clashRoyale.getId(), "Mega Ritter", "Bekannt für No Skill");

            Topic tiere = topicDAO.insert("Tiere");
            wordDAO.insert(tiere.getId(), "Elefant", "Hat die größten Ohren");
            wordDAO.insert(tiere.getId(), "Pinguin", "Kann nicht fliegen, aber schwimmen");
            wordDAO.insert(tiere.getId(), "Chamäleon", "Ändert seine Farbe");
            wordDAO.insert(tiere.getId(), "Faultier", "Bewegt sich sehr langsam");
            wordDAO.insert(tiere.getId(), "Fledermaus", "Schläft kopfüber");

            Topic essen = topicDAO.insert("Essen");
            wordDAO.insert(essen.getId(), "Pizza", "Kommt rund und wird eckig geschnitten");
            wordDAO.insert(essen.getId(), "Spaghetti", "Wird gern auf die Gabel gedreht");
            wordDAO.insert(essen.getId(), "Döner", "Klassiker nach dem Feiern");
            wordDAO.insert(essen.getId(), "Sushi", "Roh und trotzdem beliebt");
            wordDAO.insert(essen.getId(), "Bratwurst", "Gehört auf jeden Grill");

            Topic sport = topicDAO.insert("Sport");
            wordDAO.insert(sport.getId(), "Fußball", "Elf gegen elf");
            wordDAO.insert(sport.getId(), "Basketball", "Der Korb hängt hoch");
            wordDAO.insert(sport.getId(), "Schwimmen", "Findet im Becken statt");
            wordDAO.insert(sport.getId(), "Boxen", "Drei Minuten pro Runde");
            wordDAO.insert(sport.getId(), "Klettern", "Es geht nach oben");

            Topic berufe = topicDAO.insert("Berufe");
            wordDAO.insert(berufe.getId(), "Arzt", "Trägt oft Weiß");
            wordDAO.insert(berufe.getId(), "Pilot", "Arbeitet weit über dem Boden");
            wordDAO.insert(berufe.getId(), "Lehrer", "Steht vor der Tafel");
            wordDAO.insert(berufe.getId(), "Bäcker", "Fängt mitten in der Nacht an");
            wordDAO.insert(berufe.getId(), "Feuerwehrmann", "Kommt mit Blaulicht");

            //Erste Anmeldedaten
            userDAO.insert("admin", "admin");
            userDAO.insert("lukkas", "lukkas");
            userDAO.insert("stani", "stani");
            userDAO.insert("akim", "akim");

            System.out.println("Beispieldaten wurden eingefuegt.");
        }
    }
}
