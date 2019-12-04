package com.ad.admain.controller.pay.to;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "ad_value")
public class Value {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "val")
    String val;


    public Value(String val){
        this.val = val;
    }
}
