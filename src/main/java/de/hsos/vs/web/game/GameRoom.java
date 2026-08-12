package de.hsos.vs.web.game;

import de.hsos.vs.web.entities.Word;
import jakarta.websocket.Session;

import java.util.HashMap;
import java.util.Map;

public class GameRoom {
    private final Map<String, Session> playersBySessionId = new HashMap<>();
    private Word currentWord;
    private Integer imposterUserId;
    private boolean roundStarted;

    public Map<String, Session> getPlayers() {
        return playersBySessionId;
    }

    public void addPlayer(Session session) {
        playersBySessionId.put(session.getId(), session);
    }

    public void removePlayer(Session session) {
        playersBySessionId.remove(session.getId());
    }

    public int getPlayerCount() {
        return playersBySessionId.size();
    }

    public Word getCurrentWord() {
        return currentWord;
    }

    public void setCurrentWord(Word currentWord) {
        this.currentWord = currentWord;
    }

    public Integer getImposterUserId() {
        return imposterUserId;
    }

    public void setImposterUserId(Integer imposterUserId) {
        this.imposterUserId = imposterUserId;
    }

    public boolean isRoundStarted() {
        return roundStarted;
    }

    public void setRoundStarted(boolean roundStarted) {
        this.roundStarted = roundStarted;
    }
}
