package de.hsos.vs.web.entities;

public class User {
    private int id;
    private String name;
    /** Wird in der Spalte "password" abgelegt - dort gehoert ein Hash hinein, kein Klartext. */
    private String passwordHash;

    public User() {
    }

    public User( String name, String passwordHash) {
        this.name = name;
        this.passwordHash = passwordHash;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}