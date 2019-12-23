package com.ad.admain.controller.impl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author : wezhyn
 * @date : 2019/09/21
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Data
@Builder
@AllArgsConstructor
public class QiNiuPutSet implements IFileUpload {
    private String key;
    private String hash;
    private String bucket;
    private long fsize;

    public QiNiuPutSet(String key) {
        this.key=key;
    }

    @Override
    public String getRelativeName() {
       return key;
    }

    @Override
    public String getFileHash() {
       return hash;
    }

    @Override
    public long getSize() {
        return fsize;
    }
}
