package de.hsos.vs.entities;

/**
 * Member ist ein User der in einem Raum ist
 *
 * @author Stanislav
 */
public class Member {
    private final int id;
    private final String name;
    private boolean ready;

    public Member(int id, String name) {
        this.id = id;
        this.name = name;
        this.ready = false;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public boolean isReady() {
        return ready;
    }
    public void setReady(boolean ready) {
        this.ready = ready;
    }



}
