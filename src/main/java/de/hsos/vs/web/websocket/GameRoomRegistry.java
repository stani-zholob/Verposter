package de.hsos.vs.web.websocket;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Verwaltung der laufenden Spiele wird von game und voting gemeinsam benutzt
 *
 * @author Lukas
 */
public class GameRoomRegistry {
    private static final Map<Integer, GameRoom> gameRoomsById = new ConcurrentHashMap<>();

    public static GameRoom get(int roomId) {
        return gameRoomsById.get(roomId);
    }

    public synchronized static GameRoom getOrCreate(int roomId) {
        GameRoom gameRoom = gameRoomsById.get(roomId);
        if (gameRoom == null) {
            gameRoom = new GameRoom();
            gameRoomsById.put(roomId, gameRoom);
        }
        return gameRoom;
    }

    public static void remove(int roomId) {
        gameRoomsById.remove(roomId);
    }
}
