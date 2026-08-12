package de.hsos.vs.entities;

/**
 *
 * @author Stanislav
 */
public class Word {
    private int id;
    private int topicId;
    private String word; // bekommen die normalen Spieler
    private String hint; // bekommt der verposter

    public Word(int id, int topicId, String word, String hint) {
        this.id = id;
        this.topicId = topicId;
        this.word = word;
        this.hint = hint;
    }

    public int getId() {
        return id;
    }

    public int getTopicId() {
        return topicId;
    }

    public String getWord() {
        return word;
    }

    public String getHint() {
        return hint;
    }

    @Override
    public String toString() {
        return word;
    }
}
