package com.thoughtworks.rslist;

import com.fasterxml.jackson.databind.*;
import com.thoughtworks.rslist.domain.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.context.*;
import org.springframework.http.*;
import org.springframework.test.web.servlet.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    public void should_register_new_user() throws Exception {
        User user = new User("Lo", "female", 20, "abc@qq.com", "12345678910");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void userName_should_less_than_8() throws Exception {
        User user = new User("Loooooooo", "female", 18, "abc@qq.com", "12345678910");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void gender_should_not_null() throws Exception {
        User user = new User("Lo", null, 18, "abc@qq.com", "12345678910");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void age_should_between_18_and_100() throws Exception {
        User user = new User("Lo", "female", 10, "abc@qq.com", "12345678910");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void email_should_suit_format() throws Exception {
        User user = new User("Lo", "female", 18, "qq.com", "12345678910");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void phone_should_suit_format() throws Exception {
        User user = new User("Lo", "female", 18, "abc@qq.com", "123");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_add_rs_event() throws Exception {
        User user = new User("xiaowang", "female", 19, "a@thoughtworks.com", "18888888888");
        RsEvent rsEvent = new RsEvent("添加一条热搜", "娱乐", user);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/addEvent").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].eventName", is("添加一条热搜")))
                .andExpect(jsonPath("$[0].keyWord", is("娱乐")))
                .andExpect(jsonPath("$[0].user.userName", is("xiaowang")))
                .andExpect(jsonPath("$[0].user.gender", is("female")))
                .andExpect(jsonPath("$[0].user.age", is(19)))
                .andExpect(jsonPath("$[0].user.email", is("a@thoughtworks.com")))
                .andExpect(jsonPath("$[0].user.phone", is("18888888888")))
                .andExpect(status().isOk());

//        mockMvc.perform(get("/rs/userList"))
//                .andExpect(jsonPath("$", hasSize(1)))
//                .andExpect(jsonPath("$[0].userName", is("xiaowang")))
//                .andExpect(jsonPath("$[0].gender", is("female")))
//                .andExpect(jsonPath("$[0].age", is(19)))
//                .andExpect(jsonPath("$[0].email", is("a@thoughtworks.com")))
//                .andExpect(jsonPath("$[0].phone", is("18888888888")))
//                .andExpect(status().isOk());
    }
}