package com.ad.admain.screen.entity;

import com.ad.admain.controller.account.entity.GenericUser;
import com.ad.admain.controller.equipment.entity.Equipment;
import com.ad.admain.controller.pay.to.AdOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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


    AdOrder order;

    Integer uid;

    Long pooledId;

    GenericUser user;

    Equipment equipment;
}
