package de.hsos.vs.services;

import de.hsos.vs.web.inmemory.Member;
import de.hsos.vs.web.inmemory.Room;
import de.hsos.vs.web.entities.User;
import de.hsos.vs.wordservice.db.UserDAO;

import java.util.ArrayList;
/**
 * @author Stanislav
 */
public class LobbyService {
    UserDAO userDao = new UserDAO();
    //ArrayList<User> users = userDao.finAllUsers();
    ArrayList<Member> members = new ArrayList<>();
    ArrayList<Room> rooms = new ArrayList<>();
    public LobbyService() {

    }
    


    public Room getRoomById(int id){
        return rooms.get(id);
    }
    public Member getMemberById(int id){
        return members.get(id);
    }
    public Room joinRoom(int roomId, int userId){
        Room room = getRoomById(roomId);
        Member member = getMemberById(userId);
        room.addMember(member);
        return room;
    }

    public ArrayList<Room> getRooms(){
        return rooms;
    }
    public ArrayList<Member> getMembers(){
        return members;
    }
}
