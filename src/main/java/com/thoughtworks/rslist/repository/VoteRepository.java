package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.dto.*;
import org.springframework.data.repository.*;

import org.springframework.data.domain.Pageable;
import java.util.*;

public interface VoteRepository extends PagingAndSortingRepository<VoteDto, Integer> {
    @Override
    List<VoteDto> findAll();
    List<VoteDto> findAllByUserIdAndRsEventId(int userId, int rsEventId, Pageable pageable);
}
