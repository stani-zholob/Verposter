package de.hsos.vs.web.websocket;

import de.hsos.vs.entities.Word;
import jakarta.websocket.Session;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Speichert alles für session und game damit alles mit websockets funktioniert
 *
 * @author Stanislav, Lukas
 */
public class GameRoom {
    private static final int ROUND_SECONDS = 120;

    private final Map<String, Session> playersBySessionId = new HashMap<>();
    private final Map<String, Session> voteSessionsBySessionId = new HashMap<>();
    private final Map<Integer, Integer> votesByUserId = new HashMap<>();
    private Word currentWord;
    private Integer imposterUserId;
    private boolean roundStarted;
    private boolean revealed;
    private long roundEndsAt;

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

    public void setRoundEndsAt(long roundEndsAt) {
        this.roundEndsAt = roundEndsAt;
    }

    public long getRemainingSeconds() {
        long remaining = (roundEndsAt - System.currentTimeMillis()) / 1000;
        return Math.max(remaining, 0);
    }

    public boolean isRevealed() {
        return revealed;
    }

    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }

    public Map<String, Session> getVoteSessions() {
        return voteSessionsBySessionId;
    }

    public void addVoteSession(Session session) {
        voteSessionsBySessionId.put(session.getId(), session);
    }

    public void removeVoteSession(Session session) {
        voteSessionsBySessionId.remove(session.getId());
    }

    /**
     * startet die runde und den timer der nach 120s von alleine aufdeckt
     *
     * @author Lukas
     */
    public void startRound() {
        roundStarted = true;
        roundEndsAt = System.currentTimeMillis() + ROUND_SECONDS * 1000;

        System.out.println("RUNDE GESTARTET, ENDE IN " + ROUND_SECONDS + " SEKUNDEN");

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                reveal();
            }
        }, ROUND_SECONDS * 1000);

        // nur die startzeit schicken, runterzaehlen macht der client selber
        for (Session session : voteSessionsBySessionId.values()) {
            session.getAsyncRemote().sendText("TIME:" + getRemainingSeconds());
        }
    }

    /**
     * Deckt auf wer der Verposter war und schickt das an alle im raum.
     * synchronized weil der timer-thread und der websocket-thread gleichzeitig
     * hier reinlaufen koennen, sonst wird evtl 2x aufgedeckt
     *
     * @author Lukas
     */
    public synchronized void reveal() {
        if (revealed) {
            return;
        }
        revealed = true;

        String name = "Niemand";
        for (Session player : playersBySessionId.values()) {
            Integer playerId = (Integer) player.getUserProperties().get("userId");
            if (playerId != null && playerId.equals(imposterUserId)) {
                name = (String) player.getUserProperties().get("username");
            }
        }

        String wort = "unbekannt";
        if (currentWord != null) {
            wort = currentWord.getWord();
        }

        String text = "REVEAL:" + name + " war der Verposter. Das Wort war: " + wort;
        for (Session session : voteSessionsBySessionId.values()) {
            session.getAsyncRemote().sendText(text);
        }
    }

    // pro Spieler nur eine Stimme, eine neue ersetzt die alte
    public void addVote(int voterUserId, int targetUserId) {
        votesByUserId.put(voterUserId, targetUserId);
    }

    /** Der Verposter muss nicht mitwählen, sonst könnte er die Runde
     * aufhalten, indem er einfach nichts anklickt.
     *
     * @author Lukas
     */
    public boolean allPlayersVoted() {
        int votes = 0;
        for (Integer voterUserId : votesByUserId.keySet()) {
            if (!voterUserId.equals(imposterUserId)) {
                votes++;
            }
        }
        return votes >= playersBySessionId.size() - 1;
    }
}
