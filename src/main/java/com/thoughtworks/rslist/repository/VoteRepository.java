package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.dto.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.*;

import org.springframework.data.domain.Pageable;

import java.time.*;
import java.util.*;

public interface VoteRepository extends PagingAndSortingRepository<VoteDto, Integer> {
    @Override
    List<VoteDto> findAll();
    List<VoteDto> findAllByUserIdAndRsEventId(int userId, int rsEventId, Pageable pageable);

    @Query(value = "select v from VoteDto v where v.time > ?1 and v.time < ?2")
    List<VoteDto> findAll(LocalDateTime startTime, LocalDateTime endTime);
}
