package de.hsos.vs.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author Stanislav
 */
public class Lobby {
    private final Map<Integer, Room> roomsById = new HashMap<>();
    private int nextRoomId = 1;

    public Room getRoomById(int id) {
        return roomsById.get(id);
    }

    public List<Room> getRooms() {
        return new ArrayList<>(roomsById.values());
    }

    public Room createRoom(String name) {
        Room room = new Room(nextRoomId, name);
        nextRoomId++;
        roomsById.put(room.getId(), room);
        return room;
    }
}
