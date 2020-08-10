package com.thoughtworks.rslist;

import com.thoughtworks.rslist.domain.*;
import com.thoughtworks.rslist.dto.*;
import com.thoughtworks.rslist.repository.*;
import com.thoughtworks.rslist.service.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

class RsServiceTest {
    RsService rsService;
    @Mock
    RsEventRepository rsEventRepository;
    @BeforeEach
    void setUp() {
        initMocks(this);
        rsService = new RsService(rsEventRepository);
    }

    @Test
    void shouldCreateEvent() {
        // given
        RsEvent rsEvent = RsEvent.builder().keyWord("keyWord").eventName("eventName").build();
        RsEventDto rsEventDto = RsEventDto.builder()
                .keyWord(rsEvent.getKeyWord())
                .eventName(rsEvent.getEventName())
                .build();
        // when
        rsService.createEvent(rsEvent);
        // then
        verify(rsEventRepository).save(rsEventDto);
    }
}