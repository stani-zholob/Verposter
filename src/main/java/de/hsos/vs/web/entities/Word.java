package de.hsos.vs.web.entities;

public class Word {
    int id;
    int  topicId;
    String name;
    String tip;
    public Word(int id,int topicId, String name, String tip) {
        this.id = id;
        this.topicId = topicId;
        this.name = name;
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
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getTip() {
        return tip;
    }
    public void setTip(String tip) {
        this.tip = tip;
    }

}
