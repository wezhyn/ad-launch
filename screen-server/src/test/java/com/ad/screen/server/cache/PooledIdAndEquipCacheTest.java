package com.ad.screen.server.cache;

import com.ad.launch.order.AdEquipment;
import com.ad.screen.server.entity.Task;
import com.ad.screen.server.entity.TaskKey;
import org.junit.Test;

/**
 * @author wezhyn
 * @since 06.25.2020
 */
public class PooledIdAndEquipCacheTest {

    @Test
    public void test() {
        AdEquipment equipment = new AdEquipment();
        equipment.setKey("111");
        PooledIdAndEquipCache cache = new PooledIdAndEquipCache(null, equipment);
        cache.insertTask(Task.TaskBuilder.aTask().deliverUserId(1).longitude(120D).latitude(30D).scope(20D).verticalView(false).repeatNum(20).rate(5).view("").taskKey(new TaskKey(1, 1)).build());
        cache.insertTask(Task.TaskBuilder.aTask().deliverUserId(1).longitude(120D).latitude(30D).scope(20D).verticalView(false).repeatNum(20).rate(5).view("").taskKey(new TaskKey(1, 1)).build());
        cache.insertTask(Task.TaskBuilder.aTask().deliverUserId(1).longitude(120D).latitude(30D).scope(20D).verticalView(false).repeatNum(20).rate(5).view("").taskKey(new TaskKey(1, 1)).build());
        cache.insertTask(Task.TaskBuilder.aTask().deliverUserId(1).longitude(120D).latitude(30D).scope(20D).verticalView(false).repeatNum(20).rate(5).view("").taskKey(new TaskKey(1, 1)).build());
        cache.insertTask(Task.TaskBuilder.aTask().deliverUserId(1).longitude(120D).latitude(30D).scope(20D).verticalView(false).repeatNum(20).rate(5).view("").taskKey(new TaskKey(1, 1)).build());
        cache.insertTask(Task.TaskBuilder.aTask().deliverUserId(1).longitude(120D).latitude(30D).scope(20D).verticalView(false).repeatNum(20).rate(5).view("").taskKey(new TaskKey(1, 1)).build());
        cache.insertTask(Task.TaskBuilder.aTask().deliverUserId(1).longitude(120D).latitude(30D).scope(20D).verticalView(false).repeatNum(20).rate(5).view("").taskKey(new TaskKey(1, 1)).build());
        cache.insertTask(Task.TaskBuilder.aTask().deliverUserId(1).longitude(120D).latitude(30D).scope(20D).verticalView(false).repeatNum(20).rate(5).view("").taskKey(new TaskKey(1, 1)).build());
        System.out.println();
    }

}