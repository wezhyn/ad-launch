package com.ad.adlaunch.service;

import com.ad.adlaunch.to.IFileUpload;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author : wezhyn
 * @date : 2019/09/21
 * <p>
 *     上传文件的抽象类
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public interface FileUploadService {

    /**
     * 上传文件
     * @param uploadFile  文件流
     * @return 文件储存状态
     */
    IFileUpload uploadFile(MultipartFile uploadFile) throws IOException;

    /**
     * 通过文件名删除 {@link IFileUpload#getRelativeName()}
     * @param iFileUpload relativeAddress
     * @return true: 删除成功
     */
    boolean deleteFile(IFileUpload iFileUpload);


    /**
     * 覆盖原来的旧文件
     * @param uploadFile inputStream
     * @param oldFileUpload 旧文件信息
     * @return fileUpload
     */
    IFileUpload modifyFile(MultipartFile uploadFile, IFileUpload oldFileUpload) throws IOException;


}
