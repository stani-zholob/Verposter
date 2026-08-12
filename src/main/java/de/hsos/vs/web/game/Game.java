package de.hsos.vs.web.game;

import de.hsos.vs.util.HttpSessionConfigurator;
import de.hsos.vs.web.entities.Topic;
import de.hsos.vs.web.entities.Word;
import de.hsos.vs.wordservice.db.TopicDAO;
import de.hsos.vs.wordservice.db.WordDAO;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@ServerEndpoint(value = "/ws/rooms/{roomId}", configurator = HttpSessionConfigurator.class)
public class Game {

    private static final int MIN_PLAYERS = 1;
    private static final Map<Integer, GameRoom> gameRooms = new HashMap<>();
    private static final Random random = new Random();
    private final WordDAO wordDAO = new WordDAO();
    private static final TopicDAO topicDAO = new TopicDAO();


    @OnOpen
    public void onOpen(Session wsSession, EndpointConfig config, @PathParam("roomId") int roomId) throws SQLException {
        HttpSession httpSession = (HttpSession) config.getUserProperties().get("httpSession");

        if (httpSession == null) {
            return;
        }
        Integer userId = (Integer) httpSession.getAttribute("userId");
        String username = (String) httpSession.getAttribute("username");
        Integer sessionRoomId = (Integer) httpSession.getAttribute("roomId");

        if (userId == null || username == null || sessionRoomId == null || !sessionRoomId.equals(roomId)) {
            return;
        }

        wsSession.getUserProperties().put("userId", userId);
        wsSession.getUserProperties().put("username", username);
        wsSession.getUserProperties().put("roomId", roomId);

        System.out.println(
                "USERID IST " + wsSession.getUserProperties().get("userId") +
                " USERNAME IST " + wsSession.getUserProperties().get("username") +
                " ROOMID IST " + wsSession.getUserProperties().get("roomId")
        );

        GameRoom gameRoom = gameRooms.get(roomId);
        if (gameRoom == null) {
            gameRoom = new GameRoom();
            gameRooms.put(roomId, gameRoom);
        }
        gameRoom.addPlayer(wsSession);

        if (!gameRoom.isRoundStarted() && gameRoom.getPlayerCount() >= MIN_PLAYERS) {
            gameRoom.setCurrentWord(chooseGameWord());
            gameRoom.setImposterUserId(chooseImposter(gameRoom));
            gameRoom.setRoundStarted(true);
            for (Session player : gameRoom.getPlayers().values()) {
                sendCard(player, gameRoom);
            }
        } else if (gameRoom.isRoundStarted()) {
            sendCard(wsSession, gameRoom);
        }
    }

    private Integer chooseImposter(GameRoom gameRoom) {
        List<Session> players = new ArrayList<>();
        players.addAll(gameRoom.getPlayers().values());
        Session imposter = players.get(random.nextInt(players.size()));
        return (Integer) imposter.getUserProperties().get("userId");
    }

    private Word chooseGameWord() throws SQLException {
        List<Topic> topics = topicDAO.findAll();
        Topic topic = topics.get(random.nextInt(topics.size()));
        return wordDAO.findRandomByTopic(topic.getId()).orElse(null);
    }

    private void sendCard(Session session, GameRoom gameRoom) {
        Word currentWord = gameRoom.getCurrentWord();
        Integer userId = (Integer) session.getUserProperties().get("userId");
        String text;
        if (currentWord == null) {
            text = "Kein Wort gefunden";
        } else if (userId != null && userId.equals(gameRoom.getImposterUserId())) {
            text = "Du bist der Verposter\n";
            text += "Der Tipp lautet: \n";
            text += currentWord.getTip();
        } else {
            text = currentWord.getWord();
        }
        session.getAsyncRemote().sendText(text);
    }

    @OnMessage
    public void onMessage(String message, Session sender) {
        //los stani ab gehts weiter mit chat jetzt
    }

    @OnClose
    public void onClose(Session session) {
        Integer roomId = (Integer) session.getUserProperties().get("roomId");
        if (roomId == null) {
            return;
        }
        GameRoom gameRoom = gameRooms.get(roomId);
        if (gameRoom == null) {
            return;
        }
        gameRoom.removePlayer(session);
        if (gameRoom.getPlayerCount() == 0) {
            gameRooms.remove(roomId);
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }
}
