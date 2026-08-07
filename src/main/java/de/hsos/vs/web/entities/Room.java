package de.hsos.vs.web.entities;

import jakarta.servlet.annotation.WebServlet;

import java.util.ArrayList;
import java.util.List;

/**
 * Einfache Entität für den Raum
 * @author Lukas
 */
public class Room {
    private int id;
    private String name;
    private List<User> users;



    public Room(int id, String name) {
        this.id = id;
        this.name = name;
        this.users = new ArrayList<>();
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    public void removeUser(User user) {
        this.users.remove(user);
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<User> getUser() {
        return users;
    }

    public void setUser(List<User> user) {
        this.users = user;
    }
}