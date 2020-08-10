package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.*;
import com.thoughtworks.rslist.dto.*;
import com.thoughtworks.rslist.repository.*;
import org.springframework.stereotype.*;

import java.util.*;

//@Service
public class RsService {
    RsEventRepository rsEventRepository;

    public RsService(RsEventRepository rsEventRepository) {
        this.rsEventRepository = rsEventRepository;
    }

    public void createEvent(RsEvent rsEvent) {
        RsEventDto rsEventDto = RsEventDto.builder()
                .keyWord(rsEvent.getKeyWord())
                .eventName(rsEvent.getEventName())
                .userId(rsEvent.getUserId())
                .build();
        rsEventRepository.save(rsEventDto);
    }

    public List getRsEventList() {
        return rsEventRepository.findAll();
    }

    public List getRsEventListBetweenStartAndEnd(int start, int end) {
        return rsEventRepository.findAll().subList(start, end);
    }

    public boolean isIndexValid(int start, int end) {
        return start <= 0 || end > rsEventRepository.findAll().size() || start > end;
    }

    public RsEventDto getOneOfEvent(int id) {
        return rsEventRepository.findById(id).get();
    }

    public void updateEvent(int rsEventId, RsEvent rsEvent) {
        RsEventDto rsEventDto = getOneOfEvent(rsEventId);
        if (rsEvent.getKeyWord() != null) {
            rsEventDto.setKeyWord(rsEvent.getKeyWord());
        }
        if (rsEvent.getEventName() != null) {
            rsEventDto.setEventName(rsEvent.getEventName());
        }
        rsEventRepository.save(rsEventDto);
    }

    public boolean isSameUserId(int rsEventId, int userId) {
        RsEventDto rsEventDto = this.getOneOfEvent(rsEventId);
        return rsEventDto.getUserId() == userId;
    }

    public void deleteRsEvent(int id) {
        rsEventRepository.deleteById(id);
    }
}
