//package de.hsos.vs.web.game;
//
//import de.hsos.vs.web.entities.Member;
//import de.hsos.vs.web.entities.Topic;
//import de.hsos.vs.web.entities.Topics;
//import de.hsos.vs.web.entities.Word;
//import de.hsos.vs.wordservice.db.TopicDAO;
//import de.hsos.vs.wordservice.db.WordDAO;
//import jakarta.websocket.*;
//import jakarta.websocket.server.ServerEndpoint;
//
//import java.io.PrintWriter;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.concurrent.ConcurrentHashMap;
//
//@ServerEndpoint("/ws")
//public class ShowCard {
//
//    private static Word currentWord;
//    private static String imposterSessionId;
//
//
//
//    @OnOpen
//    public void onOpen(Session session) throws SQLException {
//        List<Topic> topics = topicDAO.findAll();
//        String userId = session.getId();
//        users.put(userId, session);
//
//        int max = topics.size();
//        int random = (int) (Math.random() * max);
//        int topicId = topics.get(random).getId();
//        currentWord = wordDAO.findRandomByTopic(topicId).orElse(null);
//
//        List<String> ids = new ArrayList<>(users.keySet());
//        imposterSessionId = ids.get((int) (Math.random() * ids.size()));
//
//        for (Session s : users.values()) {
//            sendCard(s);
//        }
//    }
//
//    private void sendCard(Session session) {
//        String text;
//        if (session.getId().equals(imposterSessionId)) {
//            text = "Du bist der Verposter\n";
//            text += "Der Tipp lautet: \n";
//            text += currentWord.getTip();
//        } else if (currentWord != null) {
//            text = currentWord.getWord();
//        } else {
//            text = "Kein Wort gefunden";
//        }
//        session.getAsyncRemote().sendText(text);
//    }
//
//    private static void sendToUser(String userId, String message) {
//        Session session = users.get(userId);
//        if (session != null) {
//            session.getAsyncRemote().sendText(message);
//        }
//    }
//
//    @OnMessage
//    public void onMessage(String message, Session session) throws SQLException {
//
//        //Hier kommt Chat hin
//    }
//
//    @OnClose
//    public void onClose(Session session) {
//        users.remove(session.getId());
//    }
//
//}
