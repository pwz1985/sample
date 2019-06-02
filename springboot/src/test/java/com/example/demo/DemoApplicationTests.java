package com.example.demo;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(value = "test")
public class DemoApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void contextLoads() {
    }

    @Test
    public void test() {
        System.out.println(1);

        Optional<User> byId = userRepository.findById(100L);

        if (byId.isPresent()) {
            log.info("byId={}", byId);
        }

        List<User> all = userRepository.findAll();
        Comparator<? super User> xxx = (Comparator<User>) (o1, o2) -> {
            if (o1.getId() < o2.getId()) {
                return 1;
            }
            if (o1.getId() > o2.getId()) {
                return -1;
            }
            return 0;
        };
        List<User> collect = all.stream().sorted(xxx).limit(5).collect(Collectors.toList());

        log.info("collect ={}", collect);

    }

}
