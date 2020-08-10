package com.thoughtworks.rslist;

import com.thoughtworks.rslist.repository.*;
import com.thoughtworks.rslist.service.*;
import org.springframework.context.annotation.*;

@Configuration
public class AppConfig {
    @Bean
    public VoteService voteService(UserRepository userRepository, RsEventRepository rsEventRepository, VoteRepository voteRepository) {
        return new VoteService(userRepository, rsEventRepository, voteRepository);
    }

    @Bean
    public RsService rsService(RsEventRepository rsEventRepository) {
        return new RsService(rsEventRepository);
    }

    @Bean
    public UserService userService(UserRepository userRepository) {
        return new UserService(userRepository);
    }
}
