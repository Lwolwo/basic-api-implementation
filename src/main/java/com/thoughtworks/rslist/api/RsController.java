package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.thoughtworks.rslist.domain.*;
import org.hibernate.annotations.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.*;


@RestController
public class RsController {
  private List<RsEvent> rsList = new ArrayList<>();
  private Map<String, User> userList = new LinkedHashMap<>();

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
//  public void addRsEvent(@RequestBody String rsEvent) throws JsonProcessingException {
//    ObjectMapper objectMapper = new ObjectMapper();
//    RsEvent event = objectMapper.readValue(rsEvent, RsEvent.class);
//    rsList.add(event);
//  }

  @PostMapping("/rs/addEvent")
  public void addRsEvent(@RequestBody RsEvent rsEvent) {
    rsList.add(rsEvent);

    if (!userList.containsKey(rsEvent.getUser().getUserName())) {
      userList.put(rsEvent.getUser().getUserName(), rsEvent.getUser());
    }
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

  @GetMapping("/rs/userList")
  public List getUserList() {
    return userList.entrySet().stream().collect(Collectors.toList());
  }

}
