package com.ad.screen.server.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Task {

    Integer id;


    Integer repeatNum;


    Boolean status;


    Integer adOrderId;

    Boolean verticalView;


    Integer entryId;

    String view;


    Integer orderId;

    Integer uid;

    Long pooledId;

}
