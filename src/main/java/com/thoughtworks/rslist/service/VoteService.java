package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.*;
import com.thoughtworks.rslist.dto.*;
import com.thoughtworks.rslist.repository.*;
import org.springframework.context.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;

import java.time.*;
import java.util.*;
import java.util.stream.*;

//@Service
public class VoteService {

    private UserRepository userRepository;
    private RsEventRepository rsEventRepository;
    private VoteRepository voteRepository;

    public VoteService(UserRepository userRepository, RsEventRepository rsEventRepository, VoteRepository voteRepository) {
        this.userRepository = userRepository;
        this.rsEventRepository = rsEventRepository;
        this.voteRepository = voteRepository;
    }

    public void vote(int rsEventId, Vote vote) {
        UserDto userDto = userRepository.findById(vote.getUserId()).get();
        RsEventDto rsEventDto = rsEventRepository.findById(rsEventId).get();

        if (this.userCanVote(rsEventId, vote)) {
            VoteDto voteDto = VoteDto.builder()
                    .voteNum(vote.getVoteNum())
                    .rsEvent(rsEventDto)
                    .user(userDto)
                    .time(vote.getTime())
                    .build();
            userDto.setVoteNum(userDto.getVoteNum() - vote.getVoteNum());
            rsEventDto.setVoteNum(vote.getVoteNum());
            userRepository.save(userDto);
            rsEventRepository.save(rsEventDto);
            voteRepository.save(voteDto);
        }
    }

    public boolean userCanVote(int rsEventId, Vote vote) {
        Optional<UserDto> userDtoOptional = userRepository.findById(vote.getUserId());
        if (!userDtoOptional.isPresent()) {
            throw new RuntimeException("user is not present.");
        }

        Optional<RsEventDto> rsEventDtoOptional = rsEventRepository.findById(rsEventId);
        if (!rsEventDtoOptional.isPresent()) {
            throw new RuntimeException("rsEvent is not present.");
        }
        UserDto userDto = userDtoOptional.get();
        return userDto.getVoteNum() >= vote.getVoteNum();
    }

    public List getVoteList(int userId, int rsEventId, int pageIndex) {
        Pageable pageable = PageRequest.of(pageIndex - 1, 5);
        return voteRepository.findAllByUserIdAndRsEventId(userId, rsEventId, pageable).stream().map(
                        item -> Vote.builder().voteNum(item.getVoteNum()).userId(item.getUser().getId())
                                .rsEventId(item.getRsEvent().getId()).time(item.getTime()).build()
                ).collect(Collectors.toList());
    }

    public List getVoteListByTime(String startTimeString, String endTimeString) {
        LocalDateTime startTime = LocalDateTime.parse(startTimeString);
        LocalDateTime endTime = LocalDateTime.parse(endTimeString);
        List<VoteDto> voteDtos = voteRepository.findAll(startTime, endTime);
        return voteDtos;
    }
}
