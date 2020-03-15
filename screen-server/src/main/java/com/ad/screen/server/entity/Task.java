package com.ad.screen.server.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @Description //订单拆分小任务表示类
 * @Date 2020/3/16 0:37
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Task {


    //条目编号,相对对于每个设备而言的内部任务编号
    Integer entryId;


    //重复执行次数
    Integer repeatNum;


    //是否已经发送
    Boolean sendIf;


    //订单id号
    Integer oid;


    //接收该任务的用户id
    Integer uid;


    //广告显示方式
    Boolean verticalView;


    //广告显示内容
    String view;


}
