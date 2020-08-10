package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.*;
import com.thoughtworks.rslist.dto.*;
import com.thoughtworks.rslist.repository.*;
import com.thoughtworks.rslist.service.*;
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
    VoteService voteService;

    @GetMapping("/voteRecord")
    public ResponseEntity getVoteRecord(@RequestParam int userId, @RequestParam int rsEventId, @RequestParam int pageIndex) {
        return ResponseEntity.ok(voteService.getVoteList(userId, rsEventId, pageIndex));
    }

    @GetMapping("/voteRecordTime")
    public ResponseEntity getVoteRecordBetweenGivenTime(@RequestParam String startTimeString, @RequestParam String endTimeString) {
        return ResponseEntity.ok(voteService.getVoteListByTime(startTimeString, endTimeString));
    }

    @PostMapping("/rs/vote/{rsEventId}")
    public ResponseEntity voteRsEvent(@PathVariable int rsEventId, @RequestBody Vote vote) {
        if (voteService.userCanVote(rsEventId, vote)) {
            voteService.vote(rsEventId, vote);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();

    }
}
