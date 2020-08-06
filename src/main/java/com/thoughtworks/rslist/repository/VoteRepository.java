package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.dto.*;
import org.springframework.data.repository.*;

import java.util.*;

public interface VoteRepository extends CrudRepository<VoteDto, Integer> {
    @Override
    List<VoteDto> findAll();
}
