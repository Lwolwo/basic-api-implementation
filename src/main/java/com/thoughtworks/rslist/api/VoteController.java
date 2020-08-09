package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.*;
import com.thoughtworks.rslist.repository.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;


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
}
