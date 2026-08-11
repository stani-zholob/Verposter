package de.hsos.vs.web.game;

import de.hsos.vs.wordservice.db.TopicDAO;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;

import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/ws")
public class ShowCard {

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
    public void onMessage(String message, Session session) {

            sendToUser("0", "You're Imposter");
            sendToUser("1", "Princess");
            sendToUser("2", "Princess");
    }

    @OnClose
    public void onClose(Session session) {
        users.remove(session.getId());
    }

}
