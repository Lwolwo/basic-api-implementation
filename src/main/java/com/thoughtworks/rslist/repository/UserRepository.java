package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.dto.*;
import org.springframework.data.repository.*;

import java.util.*;

public interface UserRepository extends CrudRepository<UserDto, Integer> {
    @Override
    List<UserDto> findAll();
}
