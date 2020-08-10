package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.*;
import com.thoughtworks.rslist.dto.*;
import com.thoughtworks.rslist.exception.*;
import com.thoughtworks.rslist.repository.*;
import com.thoughtworks.rslist.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.*;
import java.util.*;


@RestController
public class RsController {
  RsService rsService;
  UserService userService;

  public RsController(RsService rsService, UserService userService) {
    this.rsService = rsService;
    this.userService = userService;
  }


  @GetMapping("/rs/list")
  public ResponseEntity getRsList(@RequestParam(required = false) Integer start,
                                  @RequestParam(required = false) Integer end) {
    if (start != null && end != null) {
      if (rsService.isIndexValid(start, end)) {
        throw new RsEventInvalidException("invalid request param");
      }
      return ResponseEntity.ok(rsService.getRsEventListBetweenStartAndEnd(start - 1, end));
    }
    return ResponseEntity.ok(rsService.getRsEventList());
  }

  @GetMapping("/rs/{id}")
  public ResponseEntity getOneOfEvent(@PathVariable int id) {
    if (id < 1) {
      throw new RsEventInvalidException("invalid index");
    }
    return ResponseEntity.ok(rsService.getOneOfEvent(id));
  }


  @PostMapping("/rs/addEvent")
  public ResponseEntity addRsEvent(@RequestBody @Valid RsEvent rsEvent) {
    if (!userService.isExistUser(rsEvent.getUserId())) {
      return ResponseEntity.badRequest().build();
    }
    rsService.createEvent(rsEvent);
    return ResponseEntity.created(null).build();
  }


  @PatchMapping("/rs/update/{rsEventId}")
  public ResponseEntity updateEvent(@PathVariable int rsEventId, @RequestBody @Valid RsEvent rsEvent) {

    if (!rsService.isSameUserId(rsEventId, rsEvent.getUserId())) {
      return ResponseEntity.badRequest().build();
    }
    rsService.updateEvent(rsEventId, rsEvent);
    return ResponseEntity.ok().build();
  }


  @DeleteMapping("/rs/deleteEvent")
  public ResponseEntity deleteRsEvent(@RequestParam int id) {
    rsService.deleteRsEvent(id);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/rs/userList")
  public ResponseEntity getUserList() {
    return ResponseEntity.ok(rsService.getRsEventList());
  }


}
