package org.contoso.kafkachatapplication.infra;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DynamicTopicsProvider {
    private final Set<String> topics = new HashSet<>() {
        {
            add("default-topic");
        }
    };

    public String[] getTopics() {
        return topics.toArray(new String[0]);
    }

    public void updateTopics(Set<String> newTopics) {
        topics.clear();
        topics.addAll(newTopics);
    }
}