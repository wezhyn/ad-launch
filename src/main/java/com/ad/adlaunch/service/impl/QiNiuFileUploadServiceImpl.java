package com.ad.adlaunch.service.impl;

import com.ad.adlaunch.constants.QiNiuProperties;
import com.ad.adlaunch.service.FileUploadService;
import com.ad.adlaunch.to.IFileUpload;
import com.ad.adlaunch.to.QiNiuPutSet;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author : wezhyn
 * @date : 2019/09/21
 * <p>
 * 七牛文件上传
 * </p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Slf4j
public class QiNiuFileUploadServiceImpl implements FileUploadService {

    private final String bucketName;
    private final BucketManager bucketManager;
    private final UploadManager uploadManager;
    private final Auth auth;
    private final String defaultUploadToken;

    public QiNiuFileUploadServiceImpl(QiNiuProperties qiNiuProperties) {
        Configuration cfg=new Configuration(Zone.zone0());
        this.auth=Auth.create(qiNiuProperties.getAccessKey(), qiNiuProperties.getSecretKey());
        this.bucketName=qiNiuProperties.getBucketName();
        this.uploadManager=new UploadManager(cfg);
        this.bucketManager=new BucketManager(auth, cfg);
        this.defaultUploadToken=auth.uploadToken(bucketName);
    }

    @Override
    public IFileUpload uploadFile(MultipartFile uploadFile) throws IOException {
        return upload(uploadFile, null);
    }

    @Override
    public IFileUpload modifyFile(MultipartFile uploadFile, IFileUpload oldFileUpload) throws IOException {
        return upload(uploadFile, oldFileUpload);
    }

    @Override
    public boolean deleteFile(IFileUpload iFileUpload) {
        try {
            bucketManager.delete(this.bucketName, iFileUpload.getRelativeName());
        } catch (QiniuException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    private IFileUpload upload(MultipartFile multipartFile, IFileUpload oldFileUpload) throws IOException {

        String key=null;
        if (oldFileUpload!=null) {
            key=oldFileUpload.getRelativeName();
        }
        IFileUpload iFileUpload=null;
            Response response=uploadManager.put(multipartFile.getBytes(), key, defaultUploadToken);
            iFileUpload=response.jsonToObject(QiNiuPutSet.class);
        return iFileUpload;
    }

}
