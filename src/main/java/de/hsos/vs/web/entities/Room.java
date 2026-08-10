package de.hsos.vs.web.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Einfache Entität für den Raum
 * @author Lukas
 */
public class Room {
    private int id;
    private String name;
    private List<Member> members;



    public Room(int id, String name) {
        this.id = id;
        this.name = name;
        this.members = new ArrayList<>();
    }

    public void addMember(Member member) {
        this.members.add(member);
    }

    public void removeMember(Member member) {
        this.members.remove(member);
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

    public List<Member> getMembers() {
        return members;
    }

    public void setUser(List<Member> members) {
        this.members = members;
    }
}