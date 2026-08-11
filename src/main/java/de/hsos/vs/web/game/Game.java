package de.hsos.vs.web.game;

import de.hsos.vs.web.entities.Topic;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;

import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/ws/game")
public class Game {
    Topic topic;


    private static final Map<String, Session> users = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session) {
        users.put(session.getId(), session);
    }

    @OnMessage
    public void onMessage(String message, Session sender) {

    }

    @OnClose
    public void onClose(Session session) {
        users.remove(session.getId());
    }

    @OnError
    public void onError(Session session, Throwable error) {

    }
}
