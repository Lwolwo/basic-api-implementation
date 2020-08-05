package com.thoughtworks.rslist.domain;

import javax.validation.*;
import javax.validation.constraints.*;

public class RsEvent {
    @NotNull
    private String eventName;
    @NotNull
    private String keyWord;
    @NotNull
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

    public User getUser() {
        return user;
    }

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