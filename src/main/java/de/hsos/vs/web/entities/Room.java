package de.hsos.vs.web.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Einfache Entität für den Raum
 * @author Lukas
 */
public class Room {
    private int id;
    private String name;
    private Map<Integer, Member> membersByUserId;

    public Room(int id, String name) {
        this.id = id;
        this.name = name;
        this.membersByUserId = new HashMap<>();
    }

    public void addMember(Member member) {
        this.membersByUserId.put(member.getId(), member);
    }

    public void removeMember(Member member) {
        this.membersByUserId.remove(member.getId());
    }

    public Member getMemberById(int userId) {
        return membersByUserId.get(userId);
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
        return new ArrayList<>(membersByUserId.values());
    }

    public void setUser(List<Member> members) {
        this.membersByUserId = new HashMap<>();
        for (Member member : members) {
            addMember(member);
        }
    }
}
