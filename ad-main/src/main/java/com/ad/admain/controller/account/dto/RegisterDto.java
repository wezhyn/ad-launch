package com.ad.admain.controller.account.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wezhyn
 * @since 02.22.2020
 */
@EqualsAndHashCode(callSuper=true)
@Data
public class RegisterDto extends UserDto {


    private String code;

}
