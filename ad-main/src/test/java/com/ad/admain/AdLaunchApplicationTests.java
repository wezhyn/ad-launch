package com.ad.admain;

import com.ad.admain.repository.ToTestRepository;
import com.ad.admain.to.ToTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdLaunchApplicationTests {


    @Autowired
    private ToTestRepository toTestRepository;


    @Test
    public void testHibernateUserTypeGet() {
        Optional<ToTest> byId=toTestRepository.findById(1);
        System.out.println(byId.isPresent());
        System.out.println(byId.get());
    }
}
