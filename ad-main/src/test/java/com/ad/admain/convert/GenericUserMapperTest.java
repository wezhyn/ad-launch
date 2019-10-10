package com.ad.admain.convert;

import com.ad.admain.dto.UserDto;
import com.ad.admain.enumate.AuthenticationEnum;
import com.ad.admain.enumate.SexEnum;
import com.ad.admain.to.GenericUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

/**
 * @author wezhyn
 * @date 2019/10/05
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class GenericUserMapperTest {

    @Autowired
    private GenericUserMapper genericUserMapper;

    @Test
    public void toTo() {
        UserDto userDto=UserDto.builder()
                .username("wezhyn")
                .password("123123")
                .avatar(null)
                .roles(AuthenticationEnum.CUSTOMER.getValue())
                .sex("MALE")
                .birthDay(LocalDate.now())
                .email("sss@vip.com")
                .idCard("1")
                .mobilePhone("1")
                .build();
        System.out.println(genericUserMapper.toTo(userDto));
    }

    @Test
    public void toDto() {
        GenericUser genericUser=GenericUser.builder()
                .username("wezhyn")
                .password("123123")
                .avatar(null)
                .roles(AuthenticationEnum.CUSTOMER)
                .sex(SexEnum.MALE)
                .birthDay(LocalDate.now())
                .email("sss@vip.com")
                .idCard("1")
                .mobilePhone("1")
                .build();
        System.out.println(genericUserMapper.toDto(genericUser));
    }

    @Test
    public void update() {

        GenericUser source=new GenericUser();
        source.setRealName("兆兆");
        source.setUsername("zhaoo");
        source.setPassword("zhaoo");
        source.setSex(SexEnum.MALE);
        source.setRoles(AuthenticationEnum.CUSTOMER);
        log.info("sourceUser: {}", source.toString());

        UserDto target=new UserDto();
        target.setUsername("wezhyn111");
        target.setRealname("dzj");
        target.setSex(SexEnum.FEMALE.getValue());
        target.setPassword("passsword");
        target.setMobilePhone("123123123");
        target.setAvatar("avatar");
        target.setRoles(AuthenticationEnum.DEVELOPER.getValue());
        target.setEmail("fuckyou@dzj.com");
        log.info("targetUser :{}", target);
        genericUserMapper.updateToIgnoreNull(target, source);
        GenericUser targetU=genericUserMapper.toTo(target);
        log.info("source after copy: {}", source);
    }
}