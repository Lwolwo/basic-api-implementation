package com.thoughtworks.rslist;

import com.thoughtworks.rslist.domain.*;
import com.thoughtworks.rslist.dto.*;
import com.thoughtworks.rslist.repository.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.context.*;
import org.springframework.test.web.servlet.*;

import java.time.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class VoteControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    RsEventRepository rsEventRepository;
    RsEventDto rsEventDto;

    @Autowired
    UserRepository userRepository;
    UserDto userDto;

    @Autowired
    VoteRepository voteRepository;
    VoteDto voteDto;

    LocalDateTime localDateTime;

    @BeforeEach
    public void setup() {
        rsEventRepository.deleteAll();
        userRepository.deleteAll();
        voteRepository.deleteAll();

        userDto = UserDto.builder()
                .userName("xiaowang")
                .gender("female")
                .age(19)
                .email("a@thoughtworks.com")
                .phone("18888888888")
                .build();
        userRepository.save(userDto);

        rsEventDto = RsEventDto.builder()
                .eventName("猪肉涨价了")
                .keyWord("经济")
                .userId(userDto.getId())
                .voteNum(0)
                .build();
        rsEventRepository.save(rsEventDto);

        localDateTime = LocalDateTime.now();

        voteDto = VoteDto.builder()
                .user(userDto)
                .rsEvent(rsEventDto)
                .voteNum(3)
                .time(localDateTime)
                .build();
        voteRepository.save(voteDto);
    }

    @Test
    public void should_get_vote_record() throws Exception {
        mockMvc.perform(get("/voteRecord")
        .param("userId", String.valueOf(userDto.getId()))
        .param("rsEventId", String.valueOf(rsEventDto.getId())))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].userId", is(userDto.getId())))
                .andExpect(jsonPath("$[0].rsEventId", is(rsEventDto.getId())))
                .andExpect(jsonPath("$[0].voteNum", is(3)))
                .andExpect(status().isOk());
    }

    @Test
    public void should_get_vote_record_pageable() throws Exception {
        voteRepository.deleteAll();
        for (int i = 0; i < 8; i++) {
            VoteDto voteDto = VoteDto.builder()
                    .time(localDateTime)
                    .user(userDto)
                    .rsEvent(rsEventDto)
                    .voteNum(i + 1)
                    .build();
            voteRepository.save(voteDto);
        }

        mockMvc.perform(get("/voteRecord")
                .param("userId", String.valueOf(userDto.getId()))
                .param("rsEventId", String.valueOf(rsEventDto.getId()))
                .param("pageIndex", "1"))
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].userId", is(userDto.getId())))
                .andExpect(jsonPath("$[0].rsEventId", is(rsEventDto.getId())))
                .andExpect(jsonPath("$[0].voteNum", is(1)))
                .andExpect(jsonPath("$[1].voteNum", is(2)))
                .andExpect(jsonPath("$[2].voteNum", is(3)))
                .andExpect(jsonPath("$[3].voteNum", is(4)))
                .andExpect(jsonPath("$[4].voteNum", is(5)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/voteRecord")
                .param("userId", String.valueOf(userDto.getId()))
                .param("rsEventId", String.valueOf(rsEventDto.getId()))
                .param("pageIndex", "2"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].userId", is(userDto.getId())))
                .andExpect(jsonPath("$[0].rsEventId", is(rsEventDto.getId())))
                .andExpect(jsonPath("$[0].voteNum", is(6)))
                .andExpect(jsonPath("$[1].voteNum", is(7)))
                .andExpect(jsonPath("$[2].voteNum", is(8)))
                .andExpect(status().isOk());
    }
}
