package de.hsos.vs.entities;

/**
 * Einfache Entität für einen Topic
 *
 * @author Lukas
 */
public class Topic {
    private final int id;
    private final String name;

    public Topic(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
