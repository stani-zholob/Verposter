package de.hsos.vs.web.entities;

import de.hsos.vs.wordservice.db.UserDAO;

import java.util.ArrayList;
/**
 * @author Stanislav
 */
public class Lobby {
    UserDAO userDao = new UserDAO();
    ArrayList<Room> rooms = new ArrayList<>();

    public Room getRoomById(int id) {
        for (Room room : rooms) {
            if (room.getId() == id) {
                return room;
            }
        }
        return null;
    }

    public ArrayList<Room> getRooms(){
        return rooms;
    }
}
