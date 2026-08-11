package de.hsos.vs.web.entities;

import java.util.ArrayList;
import java.util.List;

public class Topics {
    private List<Topic> topics;

    public Topics(List<Topic> all) {
        this.topics = new ArrayList<>();
        topics.addAll(all);
    }

    public void addTopic(Topic topic) {
        this.topics.add(topic);
    }

    public List<Topic> getTopics() {
        return this.topics;
    }
}
