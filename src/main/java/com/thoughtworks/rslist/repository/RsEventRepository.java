package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.dto.*;
import org.springframework.data.repository.*;

import java.util.*;

public interface RsEventRepository extends CrudRepository<RsEventDto, Integer> {
    @Override
    List<RsEventDto> findAll();

    void deleteAllByUserId(int userId);
}
