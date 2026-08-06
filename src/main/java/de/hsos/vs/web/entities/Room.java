package de.hsos.vs.web.entities;

import jakarta.servlet.annotation.WebServlet;

import java.util.ArrayList;
import java.util.List;

/**
 * Einfache Entität für den Raum
 * @author Lukas
 */
@WebServlet("/api/room/*")
public class Room {
    private int id;
    private List<User> user;



    public Room(int id) {
        this.id = id;
        this.user = new ArrayList<>();
    }

    public void addUser(User user) {
        this.user.add(user);
    }

    public void removeUser(User user) {
        this.user.remove(user);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<User> getUser() {
        return user;
    }

    public void setUser(List<User> user) {
        this.user = user;
    }
}