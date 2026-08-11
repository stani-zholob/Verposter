package de.hsos.vs.web.game;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;

import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/ws/game")
public class Game {

    private static final Map<String, Session> users = new ConcurrentHashMap<>();
    @OnOpen
    public void onOpen(Session session) {

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
