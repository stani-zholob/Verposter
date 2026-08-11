package de.hsos.vs.web.entities;

import java.util.ArrayList;

public class Topic {
    int id;
    String name;
    ArrayList<Word> words;
    boolean selected;

    public Topic(int id, String name) {
        this.id = id;
        this.name = name;
        this.selected = false;
        this.words = new ArrayList<>();
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

    public void addWord(Word word) {
        words.add(word);
    }
}