package de.hsos.vs.entities;

/**
 * Wort ist das was Spieler am Ende bekommt. Der Player bekommt das Wort, der Imposter bekommt statt das Wort der Hint
 * @author Stanislav
 */
public class Word {
    private final int id;
    private final int topicId;
    private final String word; // bekommen die normalen Spieler
    private final String hint; // bekommt der verposter

    public Word(int id, int topicId, String word, String hint) {
        this.id = id;
        this.topicId = topicId;
        this.word = word;
        this.hint = hint;
    }

    public int getId() {
        return id;
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
