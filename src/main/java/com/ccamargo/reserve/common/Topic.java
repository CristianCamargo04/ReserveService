package com.ccamargo.reserve.common;

public enum Topic {
    BOOK_SEAT("reservations-topic"),
    SUCCESSFUL_BOOK("successful_topic");

    private final String topicName;

    Topic(String topicName) {
        this.topicName = topicName;
    }

    public String getTopicName() {
        return topicName;
    }
}
