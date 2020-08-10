package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.*;
import com.thoughtworks.rslist.dto.*;
import com.thoughtworks.rslist.repository.*;
import org.springframework.stereotype.*;

//@Service
public class UserService {
    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isExistUser(int userId) {
        return userRepository.findById(userId).isPresent();
    }

    public void addUser(User user) {
        UserDto userDto = UserDto.builder()
                .userName(user.getUserName())
                .gender(user.getGender())
                .age(user.getAge())
                .email(user.getEmail())
                .phone(user.getPhone()).build();

        userRepository.save(userDto);
    }

    public UserDto getUserById(int id) {
        return userRepository.findById(id).get();
    }

    public void deleteUserById(int id) {
        userRepository.deleteById(id);
    }
}
