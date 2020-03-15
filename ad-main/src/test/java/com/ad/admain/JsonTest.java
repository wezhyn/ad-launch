package com.ad.admain;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName JsonTest
 * @Description
 * @Author ZLB_KAM
 * @Date 2020/3/14 15:33
 * @Version V1.0
 **/

public class JsonTest {
    @Test
    public void jsonTest(){
        List<String> list = new ArrayList<>();
        list.add("fuck");
        list.add("off");
        String json = JSON.toJSONString(list);
        System.out.println(json);
    }


}
