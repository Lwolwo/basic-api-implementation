package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.*;
import com.thoughtworks.rslist.dto.*;
import com.thoughtworks.rslist.repository.*;
import com.thoughtworks.rslist.service.*;
import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.repository.query.*;
import org.springframework.http.*;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.*;

import com.thoughtworks.rslist.exception.Error;

import javax.validation.*;
import javax.validation.constraints.*;
import java.util.*;

@RestController
public class UserController {
    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/user")
    public ResponseEntity registerUser(@RequestBody @Valid User user) {
        userService.addUser(user);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity getUserById(@PathVariable int id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @DeleteMapping("/user/delete")
    public ResponseEntity deleteUserById(@RequestParam int id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity UserExceptionHandler(Exception e) {
        Error error = new Error();
        error.setError("invalid user");

        return ResponseEntity.badRequest().body(error);
    }

}
