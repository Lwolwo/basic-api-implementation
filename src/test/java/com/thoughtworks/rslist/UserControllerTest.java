package com.thoughtworks.rslist;

import com.fasterxml.jackson.databind.*;
import com.thoughtworks.rslist.domain.*;
import com.thoughtworks.rslist.dto.*;
import com.thoughtworks.rslist.repository.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.context.*;
import org.springframework.http.*;
import org.springframework.test.web.servlet.*;

import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;
    UserDto userDto;

    @BeforeEach
    public void setup() {
        userDto = UserDto.builder()
                .userName("xiaowang")
                .gender("female")
                .age(19)
                .email("a@thoughtworks.com")
                .phone("18888888888")
                .build();
    }

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
    public void should_verify_user_suit_rules() throws Exception {
        String jsonString = "{\"user_name\":\"xiaowang1\",\"user_age\": 19,\"user_gender\": \"female\",\"user_email\": \"a@thoughtworks.com\",\"user_phone\": \"18888888888\"}";

        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid user")));

    }

    @Test
    public void should_register_new_user_into_database() throws Exception {
        User user = new User("xiaowang", "female", 19, "a@thoughtworks.com", "18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
        String jsonString = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        List<UserDto> userDtoList = userRepository.findAll();
        assertEquals(1, userDtoList.size());
        assertEquals("xiaowang", userDtoList.get(0).getUserName());
        assertEquals("female", userDtoList.get(0).getGender());
        assertEquals(19, userDtoList.get(0).getAge());
        assertEquals("a@thoughtworks.com", userDtoList.get(0).getEmail());
        assertEquals("18888888888", userDtoList.get(0).getPhone());

    }

    @Test
    public void should_get_user_info_by_id() throws Exception {
        userRepository.save(userDto);
        mockMvc.perform(get("/user/1"))
                .andExpect(jsonPath("$.userName", is("xiaowang")))
                .andExpect(jsonPath("$.gender", is("female")))
                .andExpect(jsonPath("$.age", is(19)))
                .andExpect(jsonPath("$.email", is("a@thoughtworks.com")))
                .andExpect(jsonPath("$.phone", is("18888888888")))
                .andExpect(status().isOk());
    }
}