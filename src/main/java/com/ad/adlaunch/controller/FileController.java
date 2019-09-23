package com.ad.adlaunch.controller;

import com.ad.adlaunch.constants.QiNiuProperties;
import com.ad.adlaunch.dto.ResponseResult;
import com.ad.adlaunch.dto.SimpleResponseResult;
import com.ad.adlaunch.enumate.FileType;
import com.ad.adlaunch.service.FileUploadService;
import com.ad.adlaunch.to.IFileUpload;
import com.ad.adlaunch.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author : wezhyn
 * @date : 2019/09/20
 */
@RestController
@RequestMapping("/api/file")
public class FileController {

    private final FileUploadService fileUploadService;
    private final QiNiuProperties qiNiuProperties;

    public FileController(FileUploadService fileUploadService, QiNiuProperties qiNiuProperties) {
        this.fileUploadService=fileUploadService;
        this.qiNiuProperties=qiNiuProperties;
    }


    @PostMapping("/upload")
    public ResponseResult simpleFailureResponseResult(@RequestParam(value="file") MultipartFile multipartFile) {
        IFileUpload fileUpload;
        try {
            fileUpload=fileUploadService.uploadFile(multipartFile);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseResult.forFailureBuilder()
                    .withMessage(e.getMessage())
                    .build();
        }
        return ResponseResult.forSuccessBuilder()
                .withMessage("上传图片成功")
                .withData("dir", fileUpload.getRelativeName())
                .withData("host",qiNiuProperties.getHostName())
                .withData("address",qiNiuProperties.getHostName()+"/"+fileUpload.getRelativeName())
                .build();
    }

}
