package org.example.stacksagakafka;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.Serializable;

public class SagaAggregatorPayload implements Serializable {

    private JsonNode payload;
    private String rootTopic;
    private String recentTopic;
    private String recentTopicRevet;


    public String getRecentTopic() {
        return recentTopic;
    }

    public void setRecentTopic(String recentTopic) {
        this.recentTopic = recentTopic;
    }

    public String getRecentTopicRevet() {
        return recentTopicRevet;
    }

    public void setRecentTopicRevet(String recentTopicRevet) {
        this.recentTopicRevet = recentTopicRevet;
    }

    public SagaAggregatorPayload() {
    }

    public JsonNode getPayload() {
        return payload;
    }

    public void setPayload(JsonNode payload) {
        this.payload = payload;
    }

    public String getRootTopic() {
        return rootTopic;
    }

    public void setRootTopic(String rootTopic) {
        this.rootTopic = rootTopic;
    }

    @Override
    public String toString() {
        return "SagaAggregatorPayload{" +
                "payload=" + payload +
                ", rootTopic='" + rootTopic + '\'' +
                '}';
    }
}
