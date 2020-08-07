package com.thoughtworks.rslist.domain;

import lombok.*;

import java.time.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Vote {
    private int userId;
    private int rsEventId;
    private String time;
    private int voteNum;

    public Vote(int userId, String time, int voteNum) {
        this.userId = userId;
        this.time = time;
        this.voteNum = voteNum;
    }
}
