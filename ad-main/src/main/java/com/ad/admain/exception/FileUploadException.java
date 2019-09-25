package com.ad.admain.exception;

/**
 *
 * 文件上传失败
 * @author : wezhyn
 * @date : 2019/09/23
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public class FileUploadException extends Exception {

    public FileUploadException() {
    }

    public FileUploadException(String message) {
        super(message);
    }

    public FileUploadException(String message, Throwable cause) {
        super(message, cause);
    }
}
