package com.ad.admain.controller;

import com.ad.admain.config.QiNiuProperties;
import com.ad.admain.dto.IFileUpload;
import com.ad.admain.enumate.ImgBedType;
import com.ad.admain.exception.FileUploadException;
import com.ad.admain.security.AdAuthentication;
import com.ad.admain.to.ImgBed;
import com.ad.admain.to.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

/**
 * 文件上传相关 api
 *
 * @author : wezhyn
 * @date : 2019/09/20
 */
@RestController
@RequestMapping("/api/file")
@PreAuthorize("isAuthenticated()")
public class FileController {

    private final FileUploadService fileUploadService;
    private final QiNiuProperties qiNiuProperties;

    @Autowired
    private ImgBedService imgBedService;

    public FileController(FileUploadService fileUploadService, QiNiuProperties qiNiuProperties) {
        this.fileUploadService=fileUploadService;
        this.qiNiuProperties=qiNiuProperties;
    }

    @PostMapping("/avatar")
    public ResponseResult modifyUserAvatar(@RequestParam(value="img") MultipartFile file, @AuthenticationPrincipal AdAuthentication adAuthentication) throws FileUploadException {
        IFileUpload fileUpload=fileUploadService.modifyAvatarImg(file, adAuthentication);
        return ResponseResult.forSuccessBuilder()
                .withData("address", qiNiuProperties.getHostName() + "/" + fileUpload.getRelativeName())
                .withData("host", qiNiuProperties.getHostName())
                .withData("relativeAddress", fileUpload.getRelativeName())
                .withMessage("上传成功")
                .build();
    }

    @GetMapping("{type}")
    public ResponseResult getImgBed(@PathVariable("type") ImgBedType type,
                                    @RequestParam(name="limit", defaultValue="10") int limit,
                                    @RequestParam(name="page", defaultValue="1") int page) {
        Pageable pageable=PageRequest.of(page - 1, limit);
        Page<ImgBed> imgBeds=imgBedService.getImgBedListByType(type, pageable);
        return ResponseResult.forSuccessBuilder()
                .withData("items", imgBeds.getContent())
                .withData("total", imgBeds.getTotalElements())
                .withData("host", qiNiuProperties.getHostName())
                .build();
    }

    /**
     * @param file 上传的图片
     * @param i    第几张
     * @return responseResult
     */
    @PostMapping("{type}")
    public ResponseResult uploadGuideImg(
            @PathVariable("type") ImgBedType type,
            @RequestParam(value="img") MultipartFile file,
            @RequestParam(value="key", required=false) Integer i) {
        IFileUpload fileUpload=null;
        try {
            fileUpload=fileUploadService.uploadFile(file);
        } catch (FileUploadException e) {
            e.printStackTrace();
        }
        if (fileUpload==null) {
            return ResponseResult.forFailureBuilder()
                    .withMessage("请重新上传图片").build();
        }
        ImgBed imgBed;
        switch (type) {
            case GUIDE: {
                imgBed=ImgBed.forGuide(i, file.getOriginalFilename(), fileUpload.getRelativeName());
                break;
            }
            case SHUFFING: {
                imgBed=ImgBed.forShuffing(file.getOriginalFilename(), fileUpload.getRelativeName());
                break;
            }
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
        Optional<ImgBed> bed=imgBedService.save(imgBed);
        final IFileUpload savedBed=fileUpload;
        return bed.map(b->ResponseResult.forSuccessBuilder()
                .withMessage("上传：" + type.getValue() + " 成功")
                .withData("address", qiNiuProperties.getHostName() + "/" + savedBed.getRelativeName())
                .withData("host", qiNiuProperties.getHostName())
                .withData("relativeAddress", savedBed.getRelativeName()).build())
                .orElse(ResponseResult.forFailureBuilder()
                        .withMessage("上传: " + type.getValue() + "失败").build());

    }

    @ExceptionHandler(value={FileUploadException.class})
    public ResponseResult handleFileError(Exception e) {
        return ResponseResult.forFailureBuilder()
                .withMessage("上传文件失败")
                .build();
    }

}
