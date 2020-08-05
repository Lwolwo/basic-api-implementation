package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.*;
import java.util.*;

@RestController
public class UserController {
    private List<User> userList = new ArrayList<>();

    @PostMapping("/user")
    public void registerUser(@RequestBody @Valid User user) {
        userList.add(user);
    }
}
