package com.ad.admain.service.impl;

import com.ad.admain.constants.QiNiuProperties;
import com.ad.admain.dto.GenericUser;
import com.ad.admain.exception.FileUploadException;
import com.ad.admain.repository.GenericUserRepository;
import com.ad.admain.service.FileUploadService;
import com.ad.admain.to.IFileUpload;
import com.ad.admain.to.QiNiuPutSet;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
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
    private GenericUserRepository genericUserRepository;

    public QiNiuFileUploadServiceImpl(QiNiuProperties qiNiuProperties, GenericUserRepository genericUserRepository) {
        Configuration cfg=new Configuration(Zone.zone0());
        this.auth=Auth.create(qiNiuProperties.getAccessKey(), qiNiuProperties.getSecretKey());
        this.bucketName=qiNiuProperties.getBucketName();
        this.uploadManager=new UploadManager(cfg);
        this.bucketManager=new BucketManager(auth, cfg);
        this.genericUserRepository=genericUserRepository;
    }

    @Override
    public IFileUpload uploadFile(MultipartFile uploadFile) throws FileUploadException {
        return upload(uploadFile, null);
    }

    @Override
    public IFileUpload modifyFile(MultipartFile uploadFile, IFileUpload oldFileUpload) throws FileUploadException {
        return upload(uploadFile, oldFileUpload);
    }

    @Override
    public IFileUpload modifyAvatarImg(MultipartFile multipartFile) throws FileUploadException {
//        todo: 使用注解保护该方法，使得认证用户才能访问
        String userName=SecurityContextHolder.getContext().getAuthentication().getName();
//        读取历史头像地址
        String avatarName=genericUserRepository.findGenericUserByUsername(userName)
                .orElse(GenericUser.EMPTY_USER).getAvatar();
        IFileUpload fileUpload;
//       删除旧头像
        if (avatarName!=null && !StringUtils.isEmpty(avatarName)) {
            QiNiuPutSet oldFile=QiNiuPutSet.builder()
                    .key(avatarName)
                    .build();
            deleteFile(oldFile);
        }
//        生成新头像
        fileUpload=upload(multipartFile, null);
//        保存地址
        genericUserRepository.updateUserAvatar(userName, fileUpload.getRelativeName());

        return fileUpload;
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


    /**
     * @param multipartFile 文件流
     * @param oldFileUpload 旧文件信息
     * @return fileUpload
     * @throws FileUploadException 异常
     */
    private IFileUpload upload(MultipartFile multipartFile, IFileUpload oldFileUpload) throws FileUploadException {

        String key=null;
        String defaultUploadToken=null;
        if (oldFileUpload!=null) {
            key=oldFileUpload.getRelativeName();
            defaultUploadToken=auth.uploadToken(bucketName, key);
        } else {
            defaultUploadToken=auth.uploadToken(bucketName);
        }
        IFileUpload iFileUpload=null;
        try {
            Response response=uploadManager.put(multipartFile.getBytes(), key, defaultUploadToken);
            iFileUpload=response.jsonToObject(QiNiuPutSet.class);
        } catch (IOException e) {
            log.error("上传文件失败", e);
            throw new FileUploadException(e.getMessage());
        }
        return iFileUpload;
    }

}
