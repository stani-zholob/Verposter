package de.hsos.vs.web.entities;

import de.hsos.vs.web.inmemory.Room;
import de.hsos.vs.wordservice.db.UserDAO;

import java.util.ArrayList;
/**
 * @author Stanislav
 */
public class Lobby {
    UserDAO userDao = new UserDAO();
    ArrayList<Room> rooms = new ArrayList<>();

    public Room getRoomById(int id){
        return rooms.get(id);
    }
    public ArrayList<Room> getRooms(){
        return rooms;
    }
}
