package com.thoughtworks.rslist;

import com.thoughtworks.rslist.repository.*;
import com.thoughtworks.rslist.service.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.*;

@SpringBootApplication
public class RsListApplication {

    public static void main(String[] args) {
        SpringApplication.run(RsListApplication.class, args);
    }

}
