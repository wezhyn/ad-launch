package com.ad.admain.controller;

import com.ad.admain.constants.QiNiuProperties;
import com.ad.admain.dto.ResponseResult;
import com.ad.admain.exception.FileUploadException;
import com.ad.admain.service.FileUploadService;
import com.ad.admain.to.IFileUpload;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传相关 api
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

    @PostMapping("/avatar")
    public ResponseResult modifyUserAvatar(@RequestParam(value="img") MultipartFile file) throws FileUploadException {
        IFileUpload fileUpload=fileUploadService.modifyAvatarImg(file);
        return ResponseResult.forSuccessBuilder()
                .withData("address", qiNiuProperties.getHostName() + "/" + fileUpload.getRelativeName())
                .withData("host", qiNiuProperties.getHostName())
                .withData("relativeAddress", fileUpload.getRelativeName())
                .withMessage("上传成功")
                .build();

    }

    @PostMapping("/test/upload")
    public ResponseResult simpleFailureResponseResult(@RequestParam(value="file") MultipartFile multipartFile) throws FileUploadException {
        IFileUpload fileUpload=fileUploadService.uploadFile(multipartFile);
        return ResponseResult.forSuccessBuilder()
                .withData("address", qiNiuProperties.getHostName() + "/" + fileUpload.getRelativeName())
                .withData("host", qiNiuProperties.getHostName())
                .withData("relativeAddress", fileUpload.getRelativeName())
                .build();
    }

    @ExceptionHandler(value={FileUploadException.class})
    public ResponseResult handleFileError(Exception e) {
        return ResponseResult.forFailureBuilder()
                .withMessage("上传文件失败")
                .build();
    }

}
