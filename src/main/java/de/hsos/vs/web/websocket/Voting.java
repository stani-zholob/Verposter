package de.hsos.vs.web.websocket;

import de.hsos.vs.util.HttpSessionConfigurator;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;

@ServerEndpoint(value = "/ws/rooms/{roomId}/vote", configurator = HttpSessionConfigurator.class)
public class Voting {


    /**
     * prüft die http-session, verbindet den Spieler mit der Abstimmung
     * und sendet bei laufenden Runde die verbleibende Zeit
     *
     * @author Lukas
     */
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

        // runde läuft evtl schon, z.b. wenn einer F5 drueckt
        if (gameRoom.isRoundStarted() && !gameRoom.isRevealed()) {
            wsSession.getAsyncRemote().sendText("TIME:" + gameRoom.getRemainingSeconds());
        }
    }

    /**
     * liest die gewählte Spieler ID, speichert die Stimme
     * und beendet die Runde sobald alle notwendigen Stimmen vorliegen
     *
     * @author Lukas
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        Integer roomId = (Integer) session.getUserProperties().get("roomId");
        Integer voterUserId = (Integer) session.getUserProperties().get("userId");

        if (roomId == null || voterUserId == null) {
            return;
        }

        GameRoom gameRoom = GameRoomRegistry.get(roomId);
        if (gameRoom == null || !gameRoom.isRoundStarted()) {
            return;
        }

        // Client schickt nur die userId von dem den er verdächtigt
        int targetUserId;
        try {
            targetUserId = Integer.parseInt(message.trim());
        } catch (NumberFormatException e) {
            return;
        }
        gameRoom.addVote(voterUserId, targetUserId);

        System.out.println("VOTE VON " + voterUserId + " AUF " + targetUserId);

        if (gameRoom.allPlayersVoted()) {
            gameRoom.reveal();
        }
    }

    /**
     * entfernt die Verbindung aus dem Raum
     *
     * @author Lukas
     */
    @OnClose
    public void onClose(Session session) throws IOException {
        Integer roomId = (Integer) session.getUserProperties().get("roomId");
        if (roomId == null) {
            return;
        }
        GameRoom gameRoom = GameRoomRegistry.get(roomId);
        if (gameRoom == null) {
            return;
        }
        gameRoom.removeVoteSession(session);
        session.close();
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }
}
