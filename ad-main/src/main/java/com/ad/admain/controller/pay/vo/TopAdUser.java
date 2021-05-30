package com.ad.admain.controller.pay.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author wezhyn
 * @since 05.30.2021
 */
@Data
@AllArgsConstructor
public class TopAdUser {

    private Integer id;
    private String username;
    private String nickname;
    private String intro;
    private String email;

    private Integer orderNum;
}
