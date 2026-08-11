package de.hsos.vs.web.game;

import de.hsos.vs.web.entities.Member;
import de.hsos.vs.web.entities.Topic;
import de.hsos.vs.web.entities.Word;
import de.hsos.vs.wordservice.db.TopicDAO;
import de.hsos.vs.wordservice.db.WordDAO;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;

import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/ws")
public class ShowCard {
    WordDAO wordDAO = new WordDAO();
    static TopicDAO topicDAO = new TopicDAO();


    private static final Map<String, Session> users = new ConcurrentHashMap<>();
    @OnOpen
    public void onOpen(Session session) {
        String userId = session.getId();
        users.put(userId, session);
    }

    public static void sendToUser(String userId, String message) {

        Session session = users.get(userId);
        if (session != null) {
            session.getAsyncRemote().sendText(message);
        }
    }
    @OnMessage
    public void onMessage(String message, Session session) throws SQLException {
        Word word = wordDAO.findRandomByTopic(0).orElse(null);
        if (word != null) {
            session.getAsyncRemote().sendText(word.toString());
        } else {
            session.getAsyncRemote().sendText("No word found");
        }

        sendToUser("0", message);
            sendToUser("1", message);
            sendToUser("2", message);
    }

    @OnClose
    public void onClose(Session session) {
        users.remove(session.getId());
    }

}
