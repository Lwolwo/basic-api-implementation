package com.thoughtworks.rslist.domain;

import com.fasterxml.jackson.annotation.*;

import javax.validation.*;

public class RsEvent {
    private String eventName;
    private String keyWord;
    @Valid
    private User user;

    public RsEvent() {
    }

    public RsEvent(String eventName, String keyWord) {
        this.eventName = eventName;
        this.keyWord = keyWord;
    }

    public RsEvent(String eventName, String keyWord, User user) {
        this.eventName = eventName;
        this.keyWord = keyWord;
        this.user = user;
    }

    @JsonIgnore
    public User getUser() {
        return user;
    }

    @JsonProperty
    public void setUser(User user) {
        this.user = user;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }
}
