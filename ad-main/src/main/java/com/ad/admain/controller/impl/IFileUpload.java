package com.ad.admain.controller.impl;

/**
 * @author : wezhyn
 * @date : 2019/09/21
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public interface IFileUpload {

    /**
     * 获取相对于默认文件服务器后的地址，假设文件服务器前缀为 host:8080/res
     * 返回时，返回其后的地址，不包含服务器地址前缀
     * @return relativeName
     */
    String getRelativeName();

    /**
     * 获取文件的hash值
     * @return hash
     */
    String getFileHash();

    /**
     * 获取文件大小
     * @return size()
     */
    long getSize();
}
