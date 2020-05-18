package com.ad.screen.server.entity;


import com.ad.screen.server.mq.PrepareTaskMessage;
import com.ad.screen.server.server.ScreenChannelInitializer;
import lombok.Data;

/**
 * 具体分配给每个设备的任务, 即服务器内的任务, 但Task 是对应某个分配设备帧总任务，继续划分成 n FixedTask
 * 规定： 每个设备 Channel 中的Task 列表中rate 之和小于 25
 * 1 EquipTask : n Task
 * 1 Task : n FixedTask
 * n Task: repeatNum 相加 = equipTask.totalNum
 *
 * @author zlb
 */
@Data
public class Task implements PrepareTaskMessage {


    private final Integer equipTaskId;

    private final int initRepeatNum;
    /**
     * 对于 repeatNum 的保护
     */
    private final Object repeatLock=new Object();

    //    不可达
    public static final int MAX_ROUTE=FixedTask.EQUIP_MAX_ENTRY_ID/ScreenChannelInitializer.SCHEDULE_NUM;
    /**
     * 与 EquipTask 相同: 订单id，发布设备的用户
     */
    private TaskKey taskKey;
    /**
     * 当前帧次数
     * 发布 Fixed 时修改  {@code com.ad.screen.server.server.ScreenChannelInitializer#initChannel}
     */
    private int repeatNum;
    /**
     * 条目编号,相对对于每个设备而言的内部任务编号
     * 插入后回调设置
     */
    private Integer entryId;
    /**
     * 第几次发布当前帧信息
     * 发布 Fixed 时修改  {@code com.ad.screen.server.server.ScreenChannelInitializer#initChannel}
     */
    private volatile int repeatRoute;


    /**
     * 接收该任务的用户id
     */
    private volatile Integer deliverUserId;

    /**
     * 以下三个字段用于设备熄火时寻找替换设备
     */
    private Double longitude;
    private Double latitude;
    private Double scope;
    private Integer rate;


    /**
     * 广告显示方式
     */
    private Boolean verticalView;

    /**
     * 广告显示内容
     */
    private String view;


    /**
     * 保存之前发送的 FixedTask
     * 因为一个Task只属于一个Channel，单线程情况下，已下的修改均是可见，无并发修改
     * {@code com.ad.screen.server.server.ScreenChannelInitializer#initChannel} 出创建
     * {@code com.ad.screen.server.handler.CompleteMsgHandler#channelRead0} 处清空
     */
    private volatile FixedTask preTask=null;

    public Task(Integer equipTaskId, TaskKey taskKey, int rate, int repeatNum, Integer deliverUserId, Double longitude, Double latitude, Double scope, Boolean verticalView, String view) {
        this.equipTaskId = equipTaskId;
        this.taskKey = taskKey;
        this.rate = rate;
        this.initRepeatNum = repeatNum;
        this.repeatNum = repeatNum;
        this.deliverUserId = deliverUserId;
        this.longitude = longitude;
        this.latitude = latitude;
        this.scope = scope;
        this.verticalView = verticalView;
        this.view=view;
        this.repeatRoute=0;
        this.preTask=null;
    }

    @Override
    public Integer getAvailableAllocateNum() {
        return getRepeatNum();
    }

    @Override
    public Integer getDeliverNum() {
        return 1;
    }

    @Override
    public Integer getRate() {
        return rate;
    }

    @Override
    public Double getLongitude() {
        return longitude;
    }

    @Override
    public Double getLatitude() {
        return latitude;
    }

    @Override
    public Double getScope() {
        return scope;
    }

    public int repeatReduce(int reduce) {
        synchronized (repeatLock) {
            repeatRoute=(++repeatRoute)%MAX_ROUTE;
            if (repeatNum - reduce > 0) {
                repeatNum-=reduce;
                return reduce;
            } else if (repeatNum==0) {
                throw new RuntimeException("当前帧已经分配完成");
            } else {
                int oldValue=repeatNum;
                repeatNum=0;
                return oldValue;
            }
        }
    }

    public int getRepeatNum() {
        synchronized (repeatLock) {
            return repeatNum;
        }
    }

    public Integer getOrderId() {
        return this.taskKey.getOid();
    }

    public static final class TaskBuilder {
        private Integer equipTaskId;
        private TaskKey taskKey;
        private int repeatNum;
        private volatile Integer deliverUserId;
        private Double longitude;
        private Double latitude;
        private Double scope;
        private Boolean verticalView;
        private String view;
        private Integer rate;

        private TaskBuilder() {
        }

        public static TaskBuilder aTask() {
            return new TaskBuilder();
        }

        public TaskBuilder taskKey(TaskKey taskKey) {
            this.taskKey=taskKey;
            return this;
        }


        public TaskBuilder repeatNum(int repeatNum) {
            this.repeatNum=repeatNum;
            return this;
        }

        public TaskBuilder equipTaskId(int equipTaskId) {
            this.equipTaskId=equipTaskId;
            return this;
        }

        public TaskBuilder rate(int rate) {
            this.rate=rate;
            return this;
        }

        public TaskBuilder deliverUserId(Integer deliverUserId) {
            this.deliverUserId=deliverUserId;
            return this;
        }

        public TaskBuilder longitude(Double longitude) {
            this.longitude=longitude;
            return this;
        }

        public TaskBuilder latitude(Double latitude) {
            this.latitude=latitude;
            return this;
        }

        public TaskBuilder scope(Double scope) {
            this.scope=scope;
            return this;
        }

        public TaskBuilder verticalView(Boolean verticalView) {
            this.verticalView=verticalView;
            return this;
        }

        public TaskBuilder view(String view) {
            this.view=view;
            return this;
        }

        public Task build() {
            return new Task(equipTaskId, taskKey, rate, repeatNum, deliverUserId, longitude, latitude, scope, verticalView, view);
        }
    }
}
