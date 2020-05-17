package com.ad.admain.controller.account.user;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author wezhyn
 * @since 05.16.2020
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class SocialUserRepositoryTest {


    @Autowired
    private SocialUserRepository socialUserRepository;
    private Integer id;

    @Before
    public void ins() {
        SocialUser user = SocialUser.builder()
                .socialType(SocialType.ALIPAY)
                .socialAccountId("test-ali")
                .uid(7)
                .build();
        user = socialUserRepository.save(user);
        id = user.getId();
    }

    @Test
    public void test() {
        System.out.println(socialUserRepository.getOne(id));
    }


}