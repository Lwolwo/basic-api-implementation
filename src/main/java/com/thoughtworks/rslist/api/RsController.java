package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.thoughtworks.rslist.domain.*;
import org.hibernate.annotations.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.*;


@RestController
public class RsController {
  private List<RsEvent> rsList = new ArrayList<>();
  private HashMap<String, User> userList = new HashMap();

  @GetMapping("/rs/list")
  public List<RsEvent> getRsList(@RequestParam(required = false) Integer start,
                                 @RequestParam(required = false) Integer end) {
    if (start != null && end != null) {
      return rsList.subList(start - 1, end);
    }
    return rsList;
  }

  @GetMapping("/rs/{index}")
  public RsEvent getOneOfEvent(@PathVariable int index) {
    return rsList.get(index - 1);
  }

//  @PostMapping("/rs/addEvent")
//  public void addRsEvent(@RequestBody RsEvent rsEvent) {
//    rsList.add(rsEvent);
//  }

  @PostMapping("/rs/addEvent")
  public ResponseEntity addRsEvent(@RequestBody RsEvent rsEvent) throws JsonProcessingException {
    rsList.add(rsEvent);

    String userName = rsEvent.getUser().getUserName();
    if (!userList.containsKey(userName)) {
      userList.put(userName, rsEvent.getUser());
    }

    return ResponseEntity.created(null).build();
  }

  @PatchMapping("/rs/amendEvent")
  public void amendRsEvent(@RequestParam int index, @RequestBody RsEvent rsEvent) {
    if (rsEvent.getEventName() != null) {
      rsList.get(index - 1).setEventName(rsEvent.getEventName());
    }
    if (rsEvent.getKeyWord() != null) {
      rsList.get(index - 1).setKeyWord(rsEvent.getKeyWord());
    }
  }

  @DeleteMapping("/rs/deleteEvent")
  public void deleteRsEvent(@RequestParam int index) {
    rsList.remove(index - 1);
  }

}
