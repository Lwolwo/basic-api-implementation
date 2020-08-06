package com.thoughtworks.rslist.dto;

import com.thoughtworks.rslist.domain.*;
import lombok.*;

import javax.persistence.*;

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

    @ManyToOne
    private UserDto userDto;
}
