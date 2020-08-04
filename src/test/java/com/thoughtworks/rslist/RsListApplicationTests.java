package com.thoughtworks.rslist;

import com.fasterxml.jackson.databind.*;
import com.thoughtworks.rslist.domain.*;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.web.servlet.*;
import org.springframework.web.bind.annotation.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RsListApplicationTests {
    @Autowired
    MockMvc mockMvc;

    @Test
    void should_return_rs_event_list() throws Exception {
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无标签")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无标签")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord", is("无标签")))
                .andExpect(status().isOk());
    }

    @Test
    void should_return_one_of_event_list() throws Exception {
        mockMvc.perform(get("/rs/1"))
                .andExpect(jsonPath("$.eventName", is("第一条事件")))
                .andExpect(jsonPath("$.keyWord", is("无标签")))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rs/2"))
                .andExpect(jsonPath("$.eventName", is("第二条事件")))
                .andExpect(jsonPath("$.keyWord", is("无标签")))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rs/3"))
                .andExpect(jsonPath("$.eventName", is("第三条事件")))
                .andExpect(jsonPath("$.keyWord", is("无标签")))
                .andExpect(status().isOk());
    }

    @Test
    void should_return_rs_event_list_between() throws Exception {
        mockMvc.perform(get("/rs/list?start=1&end=2"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无标签")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无标签")))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rs/list?start=2&end=3"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无标签")))
                .andExpect(jsonPath("$[1].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无标签")))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rs/list?start=1&end=3"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无标签")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无标签")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord", is("无标签")))
                .andExpect(status().isOk());
    }

    @Test
    void should_add_rs_event() throws Exception {
//        String jsonString = "{\"eventName\":\"猪肉涨价了\",\"keyWord\":\"经济\"}";

        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(
                post("/rs/addEvent").content(jsonString).contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        mockMvc.perform(get("/rs/4"))
                .andExpect(jsonPath("$.eventName", is("猪肉涨价了")))
                .andExpect(jsonPath("$.keyWord", is("经济")))
                .andExpect(status().isOk());
    }

    @Test
    void contextLoads() throws Exception {

    }

}
