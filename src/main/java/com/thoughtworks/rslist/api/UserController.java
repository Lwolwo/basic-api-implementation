package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.*;
import java.util.*;

@RestController
public class UserController {
    private List<User> userList = new ArrayList<>();

    @PostMapping("/user")
    public ResponseEntity registerUser(@RequestBody @Valid User user) {
        userList.add(user);
        String index = Integer.toString(userList.indexOf(user));
        return ResponseEntity.created(null).header("index", index).build();
    }
}
