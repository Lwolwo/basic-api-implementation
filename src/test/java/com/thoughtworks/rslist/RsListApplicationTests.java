package com.thoughtworks.rslist;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.thoughtworks.rslist.domain.*;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.dto.*;
import com.thoughtworks.rslist.repository.*;
import org.apache.tomcat.jni.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.mockito.internal.matchers.Null;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.result.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.*;

import java.time.*;
import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureMockMvc

class RsListApplicationTests {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    RsEventRepository rsEventRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    VoteRepository voteRepository;

    RsEventDto rsEventDto;
    UserDto userDto;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
        rsEventRepository.deleteAll();
        voteRepository.deleteAll();

        userDto = UserDto.builder()
                .userName("xiaowang")
                .gender("female")
                .age(19)
                .email("a@thoughtworks.com")
                .phone("18888888888")
                .voteNum(10)
                .build();

        userRepository.save(userDto);

        rsEventDto = RsEventDto.builder()
                .eventName("猪肉涨价了")
                .keyWord("经济")
                .userId(userDto.getId())
                .build();

        rsEventRepository.save(rsEventDto);
    }

    @Test
    @Order(1)
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
    @Order(2)
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
    @Order(3)
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

    @Order(4)
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
    @Order(6)
    void should_amend_rs_event() throws Exception {
        RsEvent amendEventNameEvent = new RsEvent();
        amendEventNameEvent.setEventName("修改第一条事件");
        ObjectMapper objectMapper = new ObjectMapper();
        String amendEventNameJson = objectMapper.writeValueAsString(amendEventNameEvent);

        mockMvc.perform(patch("/rs/amendEvent")
                    .param("index", "1")
                    .content(amendEventNameJson)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rs/1"))
                .andExpect(jsonPath("$.eventName", is("修改第一条事件")))
                .andExpect(jsonPath("$.keyWord", is("无标签")))
                .andExpect(status().isOk());

        RsEvent amendKeyWordEvent = new RsEvent();
        amendKeyWordEvent.setKeyWord("其它");
        String amendKeyWordJson = objectMapper.writeValueAsString(amendKeyWordEvent);

        mockMvc.perform(patch("/rs/amendEvent")
                .param("index", "2")
                .content(amendKeyWordJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rs/2"))
                .andExpect(jsonPath("$.eventName", is("第二条事件")))
                .andExpect(jsonPath("$.keyWord", is("其它")))
                .andExpect(status().isOk());

        RsEvent amendEvent = new RsEvent("修改第三条事件", "其它");
        String amendEventJson = objectMapper.writeValueAsString(amendEvent);

        mockMvc.perform(patch("/rs/amendEvent")
                .param("index", "3")
                .content(amendEventJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rs/3"))
                .andExpect(jsonPath("$.eventName", is("修改第三条事件")))
                .andExpect(jsonPath("$.keyWord", is("其它")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(5) // 全部一起运行时，删除了添加的热搜。单独运行时，需测试删除第三条热搜，并且测试结果为第三条热搜被删除
    void should_delete_rs_event() throws Exception {
        // 全部运行
        mockMvc.perform(delete("/rs/deleteEvent")
                .param("index", "4")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无标签")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无标签")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord", is("无标签")))
                .andExpect(status().isOk());

        // 单独运行

        mockMvc.perform(post("/rs/deleteEvent")
                .param("index", "3")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无标签")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无标签")))
                .andExpect(status().isOk());

    }

    @Test
    public void should_add_rs_event_with_user() throws Exception {
        User user = new User("xiaowang", "female", 19, "a@thoughtworks.com", "18888888888");
        RsEvent rsEvent = new RsEvent("添加一条热搜", "娱乐", 1);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
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

        mockMvc.perform(get("/rs/userList"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].userName", is("xiaowang")))
                .andExpect(jsonPath("$[0].gender", is("female")))
                .andExpect(jsonPath("$[0].age", is(19)))
                .andExpect(jsonPath("$[0].email", is("a@thoughtworks.com")))
                .andExpect(jsonPath("$[0].phone", is("18888888888")))
                .andExpect(status().isOk());
    }

    @Test
    public void user_keyWord_eventName_should_not_be_null() throws Exception {
        User user = new User("xiaowang", "female", 19, "a@thoughtworks.com", "18888888888");
        RsEvent rsEvent = new RsEvent("添加一条热搜", "娱乐", 1);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/addEvent").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

//        rsEvent.setUser(user);
        rsEvent.setEventName(null);
        mockMvc.perform(post("/rs/addEvent").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        rsEvent.setEventName("添加一条热搜");
        rsEvent.setKeyWord(null);
        mockMvc.perform(post("/rs/addEvent").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void user_property_should_suit_rules() throws Exception {
        User user = new User("xiaowang", "female", 19, "a@thoughtworks.com", "18888888888");
        RsEvent rsEvent = new RsEvent("添加一条热搜", "娱乐", 1);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);

        user.setUserName(null);
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/addEvent").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        user.setUserName("xiaowang1");
        jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/addEvent").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        user.setUserName("xiaowang");
        user.setGender(null);
        jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/addEvent").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        user.setGender("female");
        user.setAge(101);
        jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/addEvent").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        user.setAge(17);
        jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/addEvent").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        user.setAge(20);
        user.setEmail("a.com");
        jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/addEvent").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        user.setEmail("a@thoughtworks.com");
        user.setPhone(null);
        jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/addEvent").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        user.setPhone("123");
        jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/addEvent").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void post_method_should_return_201_with_header() throws Exception {
        User user = new User("xiaowang", "female", 19, "a@thoughtworks.com", "18888888888");
        RsEvent rsEvent = new RsEvent("添加一条热搜", "娱乐", 1);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
        String jsonString = objectMapper.writeValueAsString(rsEvent);

        MvcResult result = mockMvc.perform(post("/rs/addEvent").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        assertEquals("0", result.getResponse().getHeader("index"));
    }

    @Test
    public void list_should_not_contain_user_property() throws Exception {
        User user = new User("xiaowang", "female", 19, "a@thoughtworks.com", "18888888888");
        RsEvent rsEvent = new RsEvent("添加一条热搜", "娱乐", 1);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
        String jsonString = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/addEvent").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].eventName", is("添加一条热搜")))
                .andExpect(jsonPath("$[0].keyWord", is("娱乐")))
                .andExpect(jsonPath("$[0]", not(hasKey("user"))))
                .andExpect(status().isOk());
    }

    @Test
    public void should_get_user_list() throws Exception {
//        User user = new User("xiaowang", "female", 19, "a@thoughtworks.com", "18888888888");
//        RsEvent rsEvent = new RsEvent("添加一条热搜", "娱乐", user);
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
//        String jsonString = objectMapper.writeValueAsString(rsEvent);

        String jsonString = "{\"eventName\":\"添加一条热搜\",\"keyWord\":\"娱乐\",\"user\": {\"user_name\":\"xiaowang\",\"user_age\": 19,\"user_gender\": \"female\",\"user_email\": \"a@thoughtworks.com\",\"user_phone\": \"18888888888\"}}";


        mockMvc.perform(post("/rs/addEvent").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/rs/userList"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].user_name", is("xiaowang")))
                .andExpect(jsonPath("$[0].user_gender", is("female")))
                .andExpect(jsonPath("$[0].user_age", is(19)))
                .andExpect(jsonPath("$[0].user_email", is("a@thoughtworks.com")))
                .andExpect(jsonPath("$[0].user_phone", is("18888888888")))
                .andExpect(status().isOk());

    }

    @Test
    public void should_throw_index_invalid_exception() throws Exception {
        mockMvc.perform(get("/rs/0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid index")));
    }

    @Test
    public void should_throw_param_invalid_exception() throws Exception {
        String jsonString = "{\"eventName\":\"添加一条热搜\",\"keyWord\":\"娱乐\",\"user\": {\"user_name\":\"xiaowang1\",\"user_age\": 19,\"user_gender\": \"female\",\"user_email\": \"a@thoughtworks.com\",\"user_phone\": \"18888888888\"}}";

        mockMvc.perform(post("/rs/addEvent").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid param")));
    }

    @Test
    public void should_verify_start_and_end_in_get_rs_list() throws Exception {
        mockMvc.perform(get("/rs/list?start=0&end=1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid request param")));

        mockMvc.perform(get("/rs/list?start=0&end=1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid request param")));

        mockMvc.perform(get("/rs/list?start=1&end=2"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid request param")));
    }

    @Test
    public void should_add_rs_event_and_user_registered() throws Exception {
        String jsonString = "{\"eventName\":\"添加一条热搜\",\"keyWord\":\"娱乐\",\"userId\": 666}";
        mockMvc.perform(post("/rs/addEvent")
                .content(jsonString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_update_rs_event() throws Exception {
        String jsonString = "{\"eventName\":\"添加一条热搜\",\"keyWord\":\"娱乐\",\"userId\": 666}";
        mockMvc.perform(patch("/rs/update/" + rsEventDto.getId())
                .content(jsonString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        jsonString = "{\"eventName\":\"添加一条热搜\",\"userId\": "+ rsEventDto.getUserId() + "}";
        mockMvc.perform(patch("/rs/update/" + rsEventDto.getId())
                .content(jsonString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        RsEventDto rsEventDtoTemp = rsEventRepository.findById(rsEventDto.getId()).get();
        assertEquals(rsEventDtoTemp.getEventName(), "添加一条热搜");
        assertEquals(rsEventDtoTemp.getKeyWord(), "经济");

        jsonString = "{\"keyWord\":\"其它\",\"userId\": "+ rsEventDto.getUserId() + "}";
        mockMvc.perform(patch("/rs/update/" + rsEventDto.getId())
                .content(jsonString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        rsEventDtoTemp = rsEventRepository.findById(rsEventDto.getId()).get();
        assertEquals(rsEventDtoTemp.getEventName(), "添加一条热搜");
        assertEquals(rsEventDtoTemp.getKeyWord(), "其它");

        jsonString = "{\"eventName\":\"猪肉涨价了\",\"keyWord\":\"经济\",\"userId\": "+ rsEventDto.getUserId() + "}";
        mockMvc.perform(patch("/rs/update/" + rsEventDto.getId())
                .content(jsonString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        rsEventDtoTemp = rsEventRepository.findById(rsEventDto.getId()).get();
        assertEquals(rsEventDtoTemp.getEventName(), "猪肉涨价了");
        assertEquals(rsEventDtoTemp.getKeyWord(), "经济");

    }

    @Test
    public void should_vote_rs_event() throws Exception {
        Vote vote = Vote.builder().voteNum(12).userId(userDto.getId()).time("current time").build();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
        String jsonString = objectMapper.writeValueAsString(vote);

        mockMvc.perform(post("/rs/vote/" + rsEventDto.getId())
                .content(jsonString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());


        vote.setVoteNum(5);
        jsonString = objectMapper.writeValueAsString(vote);
        mockMvc.perform(post("/rs/vote/" + rsEventDto.getId())
                .content(jsonString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertEquals(1, voteRepository.findAll().size());
        assertEquals(5, userRepository.findAll().get(0).getVoteNum());
        assertEquals(5, rsEventRepository.findAll().get(0).getVoteNum());

    }

    @Test
    void contextLoads() throws Exception {
    }

}
