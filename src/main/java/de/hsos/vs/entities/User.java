package de.hsos.vs.entities;

/**
 * Ein registrierter benutzer so wie er in der Tabelle users steht.
 *
 * @author Lukas
 */
public class User {
    private int id;
    private String name;
    private String password;

    public User(int id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return id + " " + name + " ";
    }
}