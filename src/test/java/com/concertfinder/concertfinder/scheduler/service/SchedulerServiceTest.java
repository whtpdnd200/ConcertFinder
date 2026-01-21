package com.concertfinder.concertfinder.scheduler.service;

import com.concertfinder.concertfinder.post.service.PostService;
import com.concertfinder.concertfinder.user.service.UserService;
import groovy.util.logging.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@lombok.extern.slf4j.Slf4j
@Slf4j
@SpringBootTest
@Transactional
public class SchedulerServiceTest {

    @Autowired
    private SchedulerService schedulerService;

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Test
    @DisplayName("회원 연관 정보 삭제 테스트")
    public void deleteTest() {

        List<Long> postUserIdList = postService.getUserIdList();

        for(long id : postUserIdList) {

            if(userService.isExistsUser(id)) {

                log.info("존재 함");
            }
        }


    }
}
