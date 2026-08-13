package de.hsos.vs.web.websocket;

import de.hsos.vs.util.HttpSessionConfigurator;
import de.hsos.vs.entities.Topic;
import de.hsos.vs.entities.Word;
import de.hsos.vs.database.TopicDAO;
import de.hsos.vs.database.WordDAO;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@ServerEndpoint(value = "/ws/rooms/{roomId}", configurator = HttpSessionConfigurator.class)
public class Game {
    private static final int MIN_PLAYERS = 3;
    private static final Random random = new Random();
    private final WordDAO wordDAO = new WordDAO();
    private static final TopicDAO topicDAO = new TopicDAO();

    /**
     * todo doku
     *
     * @author Stanislav
     */
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

        GameRoom gameRoom = GameRoomRegistry.getOrCreate(roomId);
        gameRoom.addPlayer(wsSession);

        if (!gameRoom.isRoundStarted() && gameRoom.getPlayerCount() >= MIN_PLAYERS) {
            gameRoom.setCurrentWord(chooseGameWord());
            gameRoom.setImposterUserId(chooseImposter(gameRoom));
            gameRoom.setRoundStarted(true);
            Voting.startRoundTimer(gameRoom);
            for (Session player : gameRoom.getPlayers().values()) {
                sendCard(player, gameRoom);
            }
        } else if (gameRoom.isRoundStarted()) {
            sendCard(wsSession, gameRoom);
        }
    }

    /**
     * Wählt einen Verposter aus. Dafür wird die Liste der Player im Raum geholt und einer von diesen zufällig ausgewählt
     * @return userId des Verposters
     *
     * @author Lukas
     */
    private Integer chooseImposter(GameRoom gameRoom) {
        List<Session> players = new ArrayList<>();
        players.addAll(gameRoom.getPlayers().values());
        Session imposter = players.get(random.nextInt(players.size()));
        return (Integer) imposter.getUserProperties().get("userId");
    }

    /**
     * Wählt zufällig ein Topic aus. Wählt zufällig ein Wort aus diesem Topic aus
     * @return Word wo Wort und Hinweis drin steht
     *
     * @author Lukas
     */
    private Word chooseGameWord() throws SQLException {
        List<Topic> topics = topicDAO.findAll();
        Topic topic = topics.get(random.nextInt(topics.size()));
        return wordDAO.findRandomByTopic(topic.getId()).orElse(null);
    }

    /**
     * zeigt jedem Nutzer (Session) das Wort dieses Spiels an oder zeigt ihm an das er der Verposter ist
     *
     * @author Lukas
     */
    private void sendCard(Session session, GameRoom gameRoom) {
        Word currentWord = gameRoom.getCurrentWord();
        Integer userId = (Integer) session.getUserProperties().get("userId");
        String text;
        if (currentWord == null) {
            text = "Kein Wort gefunden";
        } else if (userId != null && userId.equals(gameRoom.getImposterUserId())) {
            text = "Du bist der Verposter\n";
            text += "Der Tipp lautet: \n";
            text += currentWord.getHint();
        } else {
            text = currentWord.getWord();
        }
        //AsyncRemote macht das Aufruf wartet nicht auf Ankommen der Nachricht
        session.getAsyncRemote().sendText("CARD:" + text);
    }

    /**
     * todo doku
     *
     * @author Stanislav
     */
    @OnMessage
    public void onMessage(String message, Session sender) {
        Integer roomId = (Integer) sender.getUserProperties().get("roomId");
        String username = (String) sender.getUserProperties().get("username");

        if (roomId == null || username == null || message.isBlank()) {
            return;
        }

        GameRoom gameRoom = GameRoomRegistry.get(roomId);
        if (gameRoom == null) {
            return;
        }

            String chatMessage = "CHAT:" + username + ": " + message;

        for (Session player : gameRoom.getPlayers().values()) {
            if (message.equalsIgnoreCase(gameRoom.getCurrentWord().getWord().trim())) { // damit niemals das wort geschrieben werden kann
                chatMessage = "CHAT:" + username + ": " + "###########";
            }
            player.getAsyncRemote().sendText(chatMessage);
        }
    }

    /**
     * todo doku
     *
     * @author Stanislav
     */
    @OnClose
    public void onClose(Session session) {
        Integer roomId = (Integer) session.getUserProperties().get("roomId");
        if (roomId == null) {
            return;
        }
        GameRoom gameRoom = GameRoomRegistry.get(roomId);
        if (gameRoom == null) {
            return;
        }
        gameRoom.removePlayer(session);
        if (gameRoom.getPlayerCount() == 0) {
            GameRoomRegistry.remove(roomId);
        }
    }

    /**
     * todo doku
     *
     * @author Stanislav
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }
}