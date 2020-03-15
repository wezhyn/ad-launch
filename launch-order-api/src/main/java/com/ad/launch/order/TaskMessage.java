package com.ad.launch.order;

import lombok.Getter;

import java.io.StringWriter;
import java.util.List;

/**
 * @ClassName TaskMessage
 * @Description
 * @Author ZLB_KAM
 * @Date 2020/3/8 0:56
 * @Version V1.0
 **/
@Getter

public class TaskMessage {
    public static final String TOPIC_TAG="task_topic";

    private Integer oid;

    private Integer uid;

    private List<String> produceContext;

    private Boolean vertical;

    private Double totalAmount;

    private Integer numPerEquip;

    private Integer deliverNum;

    private Integer rate;

    private Double longitude;

    private Double latitude;

    private Double scope;

}
