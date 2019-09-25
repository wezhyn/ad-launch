package com.ad.admain.enumate;

/**
 * @author : wezhyn
 * @date : 2019/09/20
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public enum  FileType implements StringEnum {

    /**
     * 图片类型
     */
    IMAGE("image"),FILE("file");
    private String value;


    FileType(String value) {
        this.value=value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
