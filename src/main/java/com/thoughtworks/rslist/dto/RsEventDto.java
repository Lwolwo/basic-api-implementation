package com.thoughtworks.rslist.dto;

import com.fasterxml.jackson.annotation.*;
import com.thoughtworks.rslist.domain.*;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "rsEvent")
public class RsEventDto {
    @Id
    @GeneratedValue
    private int id;
    private String eventName;
    private String keyWord;
    private int userId;
    private int voteNum = 0;

    @ManyToOne
    private UserDto userDto;

    @JsonIgnore
    public int getUserId() {
        return userId;
    }

    @JsonProperty
    public void setUserId(int userId) {
        this.userId = userId;
    }
}
