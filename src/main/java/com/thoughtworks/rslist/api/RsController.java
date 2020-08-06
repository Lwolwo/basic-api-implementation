package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.*;
import com.thoughtworks.rslist.dto.*;
import com.thoughtworks.rslist.exception.*;
import com.thoughtworks.rslist.repository.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.*;
import java.util.*;


@RestController
public class RsController {
  @Autowired
  RsEventRepository rsEventRepository;
  @Autowired
  UserRepository userRepository;
  @Autowired
  VoteRepository voteRepository;

  @GetMapping("/rs/list")
  public ResponseEntity getRsList(@RequestParam(required = false) Integer start,
                                  @RequestParam(required = false) Integer end) {
    if (start != null && end != null) {
      if (start <= 0 || end > rsEventRepository.findAll().size() || start > end) {
        throw new RsEventInvalidException("invalid request param");
      }
      return ResponseEntity.ok(rsEventRepository.findAll().subList(start - 1, end));
    }
    return ResponseEntity.ok(rsEventRepository.findAll());
  }

  @GetMapping("/rs/{id}")
  public ResponseEntity getOneOfEvent(@PathVariable int id) {
    if (id < 1 || id > rsEventRepository.findAll().size()) {
      throw new RsEventInvalidException("invalid index");
    }
    return ResponseEntity.ok(rsEventRepository.findById(id));
  }


  @PostMapping("/rs/addEvent")
  public ResponseEntity addRsEvent(@RequestBody @Valid RsEvent rsEvent) {
    if (!userRepository.findById(rsEvent.getUserId()).isPresent()) {
      return ResponseEntity.badRequest().build();
    }
    RsEventDto rsEventDto = RsEventDto.builder()
            .keyWord(rsEvent.getKeyWord())
            .eventName(rsEvent.getEventName())
            .userId(rsEvent.getUserId())
            .build();
    rsEventRepository.save(rsEventDto);

    return ResponseEntity.created(null).build();
  }


  @PatchMapping("/rs/update/{rsEventId}")
  public ResponseEntity updateEvent(@PathVariable int rsEventId, @RequestBody @Valid RsEvent rsEvent) {
    RsEventDto rsEventDto = rsEventRepository.findById(rsEventId).get();
    if (rsEventDto.getUserId() != rsEvent.getUserId()) {
      return ResponseEntity.badRequest().build();
    }
    if (rsEvent.getKeyWord() != null) {
      rsEventDto.setKeyWord(rsEvent.getKeyWord());
    }
    if (rsEvent.getEventName() != null) {
      rsEventDto.setEventName(rsEvent.getEventName());
    }

    rsEventRepository.save(rsEventDto);
    return ResponseEntity.ok().build();
  }


  @DeleteMapping("/rs/deleteEvent")
  public ResponseEntity deleteRsEvent(@RequestParam int id) {
    rsEventRepository.deleteById(id);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/rs/userList")
  public ResponseEntity getUserList() {
    return ResponseEntity.ok(userRepository.findAll());
  }

  @PostMapping("/rs/vote/{rsEventId}")
  public ResponseEntity voteRsEvent(@PathVariable int rsEventId, @RequestBody Vote vote) {
    UserDto userDto = userRepository.findById(vote.getUserId()).get();
    RsEventDto rsEventDto = rsEventRepository.findById(rsEventId).get();
    if (userDto.getVoteNum() >= vote.getVoteNum()) {
      VoteDto voteDto = VoteDto.builder()
              .voteNum(vote.getVoteNum())
              .rsEventId(rsEventId)
              .userId(vote.getUserId())
              .time(vote.getTime())
              .build();
      userDto.setVoteNum(userDto.getVoteNum() - vote.getVoteNum());
      rsEventDto.setVoteNum(vote.getVoteNum());
      userRepository.save(userDto);
      rsEventRepository.save(rsEventDto);
      voteRepository.save(voteDto);
      return ResponseEntity.ok().build();
    }
    return ResponseEntity.badRequest().build();
  }

}
