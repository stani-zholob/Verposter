package de.hsos.vs.web.entities;

import java.util.ArrayList;

public class Topic {
    int id;
    String name;
    ArrayList<Word> words;
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

    public ArrayList<Word> getWords() {
        return words;
    }
}
