package com.ad.launch.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName TaskMessage
 * @Description
 * @Author ZLB_KAM
 * @Date 2020/3/8 0:56
 * @Version V1.0
 **/
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskMessage {

    private Integer oid;

    private Integer uid;

    private List<String> produceContext;

    private Boolean vertical;

    private Integer totalNum;


    private Integer deliverNum;

    private Integer rate;

    private Double longitude;

    private Double latitude;

    private Double scope;

}
