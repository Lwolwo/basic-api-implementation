package com.thoughtworks.rslist.dto;

import lombok.*;

import javax.persistence.*;
import java.time.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vote")
public class VoteDto {
    @Id
    @GeneratedValue
    private int id;
    private int userId;
    private int rsEventId;
    private LocalDateTime time;
    private int voteNum;
}