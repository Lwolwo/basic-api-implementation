package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.thoughtworks.rslist.domain.*;
import org.hibernate.annotations.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
public class RsController {
  private List<RsEvent> rsList = initRsList();

  private List<RsEvent> initRsList() {
    List<RsEvent> rsList = new ArrayList<>();
    rsList.add(new RsEvent("第一条事件", "无标签"));
    rsList.add(new RsEvent("第二条事件", "无标签"));
    rsList.add(new RsEvent("第三条事件", "无标签"));
    return rsList;
  }

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
  public void addRsEvent(@RequestBody String rsEvent) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    RsEvent event = objectMapper.readValue(rsEvent, RsEvent.class);
    rsList.add(event);
  }

  @PostMapping("/rs/amendEvent")
  public void amendRsEvent(@RequestParam int index, @RequestParam(required = false) String eventName, @RequestParam (required = false) String keyWord) {
    if (eventName != null) {
      rsList.get(index - 1).setEventName(eventName);
    }
    if (keyWord != null) {
      rsList.get(index - 1).setKeyWord(keyWord);
    }
  }

  @PostMapping("/rs/deleteEvent")
  public void deleteRsEvent(@RequestParam int index) {
    rsList.remove(index - 1);
  }
}
