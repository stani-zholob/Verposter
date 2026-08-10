//package de.hsos.vs.web.game;
//
//import jakarta.websocket.*;
//import jakarta.websocket.server.ServerEndpoint;
//
//import java.io.PrintWriter;
//import java.util.Map;
//import java.util.Set;
//import java.util.concurrent.ConcurrentHashMap;
//
//@ServerEndpoint("/ws")
//public class Game {
//
//    private static final Map<String, Session> users = new ConcurrentHashMap<>();
//    @OnOpen
//    public void onOpen(Session session) {
//        String userId = session.getId();
//        users.put(userId, session);
//    }
//
//    @OnMessage
//    public void onMessage(String message, Session sender) {
//        String result = "Spieler " + sender.getId() + ": " + message;
//
//        for (Session session : sessions) {
//            if (session.isOpen()) {
//                session.getAsyncRemote().sendText(result);
//            }
//        }
//    }
//
//    @OnClose
//    public void onClose(Session session) {
//        users.remove(session.getId());
//    }
//
//    @OnError
//    public void onError(Session session, Throwable error) {
//        sessions.remove(session);
//        error.printStackTrace();
//    }
//}
