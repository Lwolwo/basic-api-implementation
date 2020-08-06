package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.*;
import com.thoughtworks.rslist.dto.*;
import com.thoughtworks.rslist.repository.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.*;

import com.thoughtworks.rslist.exception.Error;

import javax.validation.*;
import javax.validation.constraints.*;
import java.util.*;

@RestController
public class UserController {
    @Autowired
    UserRepository userRepository;

    @PostMapping("/user")
    public ResponseEntity registerUser(@RequestBody @Valid User user) {
        UserDto userDto = UserDto.builder()
                .userName(user.getUserName())
                .gender(user.getGender())
                .age(user.getAge())
                .email(user.getEmail())
                .phone(user.getPhone()).build();

        userRepository.save(userDto);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity getUserById(@PathVariable int id) {
        return ResponseEntity.ok(userRepository.findById(id).get());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity UserExceptionHandler(Exception e) {
        Error error = new Error();
        error.setError("invalid user");

        return ResponseEntity.badRequest().body(error);
    }

}
