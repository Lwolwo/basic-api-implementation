package com.thoughtworks.rslist.dto;

import com.thoughtworks.rslist.domain.*;
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
    private LocalDateTime time;
    private int voteNum;

    @ManyToOne @JoinColumn(name = "user_id")
    private UserDto user;
    @ManyToOne @JoinColumn(name = "rs_event_id")
    private RsEventDto rsEvent;
}