package de.hsos.vs.web.entities;

public class Member {
    private int id;
    private String name;
    private boolean ready;

    public Member(int id, String name) {
        this.id = id;
        this.name = name;
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
    public boolean isReady() {
        return ready;
    }
    public void setReady(boolean ready) {
        this.ready = ready;
    }



}
