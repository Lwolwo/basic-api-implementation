package com.thoughtworks.rslist.domain;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import javax.validation.*;
import javax.validation.constraints.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RsEvent {
    private String eventName;
    private String keyWord;
    @NotNull
    private int userId;

    public RsEvent(String eventName, String keyWord) {
        this.eventName = eventName;
        this.keyWord = keyWord;
    }

    public RsEvent(String eventName, @NotNull int userId) {
        this.eventName = eventName;
        this.userId = userId;
    }

    public RsEvent(@NotNull int userId, String keyWord) {
        this.keyWord = keyWord;
        this.userId = userId;
    }
}
