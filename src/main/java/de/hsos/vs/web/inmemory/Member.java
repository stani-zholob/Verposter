package de.hsos.vs.web.inmemory;

public class Member {
    private int id;
    private String name;
    private String role;
    private boolean ready;

    public Member(int id, String name, String role) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.ready = false;
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
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public boolean isReady() {
        return ready;
    }
    public void setReady(boolean ready) {
        this.ready = ready;
    }



}
