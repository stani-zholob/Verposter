package de.hsos.vs.services;

import de.hsos.vs.web.entities.Room;
import de.hsos.vs.web.entities.User;

import java.util.ArrayList;
/**
 * @author Stanislav
 */
public class LobbyService {
    //UserDAO userDao = new UserDAO();
    //ArrayList<User> users = userDao.finAllUsers();
    ArrayList<User> users = new ArrayList<>();
    ArrayList<Room> rooms = new ArrayList<>();

    public Room getRoomById(int id){
        return rooms.get(id);
    }
    public User getUserById(int id){
        return users.get(id);
    }
    public Room joinRoom(int roomId, int userId){
        Room room = getRoomById(roomId);
        User user = getUserById(userId);
        room.addUser(user);
        return room;
    }

    public ArrayList<Room> getRooms(){
        return rooms;
    }
    public ArrayList<User> getUsers(){
        return users;
    }
}
