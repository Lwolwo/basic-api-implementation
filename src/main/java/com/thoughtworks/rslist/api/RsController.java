package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.thoughtworks.rslist.domain.*;
import com.thoughtworks.rslist.exception.*;
import org.hibernate.annotations.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.*;
import java.util.*;
import java.util.stream.*;


@RestController
public class RsController {
  private List<RsEvent> rsList = new ArrayList<>();
  private Map<String, User> userList = new LinkedHashMap<>();

  @GetMapping("/rs/list")
  public ResponseEntity getRsList(@RequestParam(required = false) Integer start,
                                  @RequestParam(required = false) Integer end) {
    if (start != null && end != null) {
      return ResponseEntity.ok(rsList.subList(start - 1, end));
    }
    return ResponseEntity.ok(rsList);
  }

  @GetMapping("/rs/{index}")
  public ResponseEntity getOneOfEvent(@PathVariable int index) {
    if (index < 1 || index > rsList.size()) {
      throw new RsEventIndexInvalidException("invalid index");
    }
    return ResponseEntity.ok(rsList.get(index - 1));
  }


//  @PostMapping("/rs/addEvent")
//  public void addRsEvent(@RequestBody String rsEvent) throws JsonProcessingException {
//    ObjectMapper objectMapper = new ObjectMapper();
//    RsEvent event = objectMapper.readValue(rsEvent, RsEvent.class);
//    rsList.add(event);
//  }

  @PostMapping("/rs/addEvent")
  public ResponseEntity addRsEvent(@RequestBody @Valid RsEvent rsEvent) {
    rsList.add(rsEvent);

    if (!userList.containsKey(rsEvent.getUser().getUserName())) {
      userList.put(rsEvent.getUser().getUserName(), rsEvent.getUser());
    }

    String index = Integer.toString(rsList.indexOf(rsEvent));
    return ResponseEntity.created(null).header("index", index).build();
  }

  @PatchMapping("/rs/amendEvent")
  public ResponseEntity amendRsEvent(@RequestParam int index, @RequestBody RsEvent rsEvent) {
    if (rsEvent.getEventName() != null) {
      rsList.get(index - 1).setEventName(rsEvent.getEventName());
    }
    if (rsEvent.getKeyWord() != null) {
      rsList.get(index - 1).setKeyWord(rsEvent.getKeyWord());
    }
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/rs/deleteEvent")
  public ResponseEntity deleteRsEvent(@RequestParam int index) {
    rsList.remove(index - 1);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/rs/userList")
  public ResponseEntity getUserList() {
    return ResponseEntity.ok(new ArrayList<>(userList.values()));
  }

}
