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
    private LocalDateTime time;
    private int voteNum;
}
