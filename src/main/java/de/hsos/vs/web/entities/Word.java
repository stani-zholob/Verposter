package de.hsos.vs.web.entities;

public class Word {
    private int id;
    private int topicId;
    private String word;
    private String tip;

    public Word() {
    }

    public Word(int id, int topicId, String word, String tip) {
        this.id = id;
        this.topicId = topicId;
        this.word = word;
        this.tip = tip;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }
}
