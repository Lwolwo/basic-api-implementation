package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.*;
import com.thoughtworks.rslist.dto.*;
import com.thoughtworks.rslist.repository.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;


import java.time.*;
import java.time.format.*;
import java.util.*;
import java.util.stream.*;

@RestController
public class VoteController {
    @Autowired
    VoteRepository voteRepository;

    @GetMapping("/voteRecord")
    public ResponseEntity getVoteRecord(@RequestParam int userId, @RequestParam int rsEventId, @RequestParam int pageIndex) {
        Pageable pageable = PageRequest.of(pageIndex - 1, 5);
        return ResponseEntity.ok(
                voteRepository.findAllByUserIdAndRsEventId(userId, rsEventId, pageable).stream().map(
                        item -> Vote.builder().voteNum(item.getVoteNum()).userId(item.getUser().getId())
                        .rsEventId(item.getRsEvent().getId()).time(item.getTime()).build()
                ).collect(Collectors.toList())
        );
    }

    @GetMapping("/voteRecordTime")
    public ResponseEntity getVoteRecordBetweenGivenTime(@RequestParam String startTimeString, @RequestParam String endTimeString) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startTime = LocalDateTime.parse(startTimeString, df);
        LocalDateTime endTime = LocalDateTime.parse(endTimeString, df);
        List<VoteDto> voteDtos = voteRepository.findAll();

        return ResponseEntity.ok(
                voteDtos.stream().filter(
                        item -> startTime.isBefore(item.getTime()) && endTime.isAfter(item.getTime())
                ).collect(Collectors.toList())
        );
    }
}
