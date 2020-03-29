package com.ad.admain;

import com.ad.admain.controller.account.GenericUserService;
import com.ad.admain.controller.account.user.GenericUser;
import com.ad.admain.controller.equipment.EquipmentRepository;
import com.ad.admain.controller.equipment.entity.Equipment;
import com.ad.admain.controller.equipment.entity.EquipmentVerify;
import com.ad.launch.user.AuthenticationEnum;
import com.wezhyn.project.account.SexEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wezhyn
 * @since 03.29.2020
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestDataCreate {

    @Autowired
    private GenericUserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EquipmentRepository equipmentService;

    private static String createEquipName(Integer count) {
        String c=count.toString();
        StringBuilder sb=new StringBuilder();
        for (int i=0; i < 15 - c.length(); i++) {
            sb.append("0");
        }
        sb.append(c);
        return sb.toString();
    }

    @Test
    public void createEquips() {
        for (int count=0; count < 1000; count++) {
            List<Equipment> equipment=new ArrayList<>();
            for (int i=0; i < 9; i++) {
                int id=9*count + i + 1000;
                Equipment feed=Equipment.builder()
                        .uid(id)
                        .feedback("feed-" + id)
                        .intro("测试")
                        .createTime(LocalDateTime.of(2020, 1, 1, 1, 1))
                        .startTime(LocalDateTime.of(2020, 1, 1, 1, 1))
                        .endTime(LocalDateTime.of(2029, 12, 30, 23, 59))
                        .verify(EquipmentVerify.PASSING_VERIFY)
                        .name("test-equp-" + id)
                        .key(createEquipName(id))
                        .remain(0)
                        .status(false)
                        .build();
                equipment.add(feed);
            }
            equipmentService.saveAll(equipment);
        }

    }

    @Test
    public void createUser() {
        String name="wezhyn-";
        for (int count=0; count < 10000; count++) {
            GenericUser user=GenericUser.builder()
                    .username(name + count)
                    .nickName(name + count)
                    .realName(name + count)
                    .sex(SexEnum.MALE)
                    .roles(AuthenticationEnum.USER)
                    .enable(GenericUser.UserEnable.NORMAL)
                    .password(passwordEncoder.encode("wezhyn"))
                    .build();
            userService.save(user);
        }

    }
}
