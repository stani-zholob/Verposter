package de.hsos.vs.web.game;

import de.hsos.vs.util.HttpSessionConfigurator;
import de.hsos.vs.web.entities.Topic;
import de.hsos.vs.web.entities.Word;
import de.hsos.vs.wordservice.db.TopicDAO;
import de.hsos.vs.wordservice.db.WordDAO;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;

import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/ws", configurator = HttpSessionConfigurator.class)
public class Game {

    WordDAO wordDAO = new WordDAO();
    static TopicDAO topicDAO = new TopicDAO();
    private static final Map<String, Session> users = new ConcurrentHashMap<>();

    private Word currentWord;
    String imposterSessionId;


    @OnOpen
    public void onOpen(Session wsSession, EndpointConfig config) throws SQLException {
        HttpSession httpSession = (HttpSession) config.getUserProperties().get("httpSession");



        if (httpSession == null) {
            return;
        }
        Integer userId = (Integer) httpSession.getAttribute("userId");
        String username = (String) httpSession.getAttribute("username");
        Integer roomId = (Integer) httpSession.getAttribute("roomId");

        wsSession.getUserProperties().put("userId", userId);
        wsSession.getUserProperties().put("username", username);
        wsSession.getUserProperties().put("roomId", roomId);

        System.out.println(
                "USERID IST " + wsSession.getUserProperties().get("userId") +
                " USERNAME IST " + wsSession.getUserProperties().get("username") +
                " ROOMID IST " + wsSession.getUserProperties().get("roomId")
        );




        users.put(wsSession.getId(), wsSession);


        List<Topic> topics = topicDAO.findAll();

        int max = topics.size();
        int random = (int) (Math.random() * max);
        int topicId = topics.get(random).getId();
        currentWord = wordDAO.findRandomByTopic(topicId).orElse(null);

        List<String> ids = new ArrayList<>(users.keySet());
        imposterSessionId = ids.get((int) (Math.random() * ids.size()));

        for (Session s : users.values()) {
            sendCard(s);
        }
    }

    private void sendCard(Session session) {
        String text;
        if (session.getId().equals(imposterSessionId)) {
            text = "Du bist der Verposter\n";
            text += "Der Tipp lautet: \n";
            text += currentWord.getTip();
        } else if (currentWord != null) {
            text = currentWord.getWord();
        } else {
            text = "Kein Wort gefunden";
        }
        session.getAsyncRemote().sendText(text);
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
