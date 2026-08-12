package de.hsos.vs.web.websocket;

import de.hsos.vs.util.HttpSessionConfigurator;
import de.hsos.vs.entities.Word;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Endpoint für abstimmung und reveal und auch countdown
 *
 * @author Lukas, Stanislav
 */
@ServerEndpoint(value = "/ws/rooms/{roomId}/vote", configurator = HttpSessionConfigurator.class)
public class Voting {
    private static final int ROUND_SECONDS = 120;

    private static final ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();

    public static void startRoundTimer(GameRoom gameRoom) {
        gameRoom.setRoundEndsAt(System.currentTimeMillis() + ROUND_SECONDS * 1000L);
        timer.schedule(() -> reveal(gameRoom), ROUND_SECONDS, TimeUnit.SECONDS);
        broadcastTime(gameRoom);
    }

    private static void broadcastTime(GameRoom gameRoom) {
        String time = "TIME:" + gameRoom.getRemainingSeconds();
        for (Session session : gameRoom.getVoteSessions().values()) {
            session.getAsyncRemote().sendText(time);
        }
    }

    @OnOpen
    public void onOpen(Session wsSession, EndpointConfig config, @PathParam("roomId") int roomId) {
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

        GameRoom gameRoom = GameRoomRegistry.getOrCreate(roomId);
        gameRoom.addVoteSession(wsSession);

        // Die Runde kann schon laufen, wenn dieser Socket aufgeht (der Spiel-Socket
        // startet sie) oder wenn jemand die Seite neu lädt.
        if (gameRoom.isRoundStarted() && !gameRoom.isRevealed()) {
            wsSession.getAsyncRemote().sendText("TIME:" + gameRoom.getRemainingSeconds());
        }
    }

    @OnMessage
    public void onMessage(String message, Session voter) {
        Integer roomId = (Integer) voter.getUserProperties().get("roomId");
        Integer voterUserId = (Integer) voter.getUserProperties().get("userId");

        if (roomId == null || voterUserId == null) {
            return;
        }

        GameRoom gameRoom = GameRoomRegistry.get(roomId);
        if (gameRoom == null || !gameRoom.isRoundStarted()) {
            return;
        }

        // der Client schickt nur die userId des Verdächtigen als Zahl
        int targetUserId;
        try {
            targetUserId = Integer.parseInt(message.trim());
        } catch (NumberFormatException e) {
            return;
        }
        gameRoom.addVote(voterUserId, targetUserId);

        // zweiter Weg zum Ende der Runde: alle haben abgestimmt
        if (gameRoom.allPlayersVoted()) {
            reveal(gameRoom);
        }
    }

    private static synchronized void reveal(GameRoom gameRoom) {
        if (gameRoom.isRevealed()) {
            return;
        }
        gameRoom.setRevealed(true);

        String reveal = "REVEAL:" + buildRevealText(gameRoom);
        for (Session session : gameRoom.getVoteSessions().values()) {
            session.getAsyncRemote().sendText(reveal);
        }
    }

    private static String buildRevealText(GameRoom gameRoom) {
        Word currentWord = gameRoom.getCurrentWord();
        String wordText = currentWord == null ? "unbekannt" : currentWord.getWord();
        return findUsername(gameRoom, gameRoom.getImposterUserId())
                + " war der Verposter. Das Wort war: " + wordText;
    }

    private static String findUsername(GameRoom gameRoom, Integer userId) {
        if (userId == null) {
            return "Niemand";
        }
        for (Session player : gameRoom.getPlayers().values()) {
            if (userId.equals(player.getUserProperties().get("userId"))) {
                return (String) player.getUserProperties().get("username");
            }
        }
        return "Spieler " + userId;
    }

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
        gameRoom.removeVoteSession(session);
    }

    @OnError
    public void onError(Throwable error) {
        error.printStackTrace();
    }
}
