package com.ad.admain.controller.pay.to;


import com.wezhyn.project.IBaseTo;
import lombok.Data;

import javax.persistence.*;

/**
 * @author : lb
 * @date : 2019/12/31
 */
@Data
@Entity
@Table(name="ad_value")
public class Value implements IBaseTo<Integer> {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    Integer id;

    @Column(name="val")
    String val;


    public Value(String val) {
        this.val=val;
    }
}
