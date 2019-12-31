package com.ad.admain.controller;

import com.ad.admain.config.QiNiuProperties;
import com.ad.admain.controller.impl.IFileUpload;
import com.ad.admain.controller.impl.ImgBed;
import com.ad.admain.controller.impl.ImgBedType;
import com.ad.admain.exception.FileUploadException;
import com.ad.admain.security.AdAuthentication;
import com.wezhyn.project.controller.ResponseResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    private final ImgBedService imgBedService;

    public FileController(FileUploadService fileUploadService, QiNiuProperties qiNiuProperties, ImgBedService imgBedService) {
        this.fileUploadService=fileUploadService;
        this.qiNiuProperties=qiNiuProperties;
        this.imgBedService=imgBedService;
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
        List<ImgBed> content=imgBeds.getContent();
        if (imgBeds.getSize() < limit && ImgBedType.GUIDE==type) {
            for (int i=0; i < limit - imgBeds.getSize(); i++) {
                ImgBed imgBed=ImgBed.forGuide(-1, "", "");
                content.add(imgBed);
            }
        }
        return ResponseResult.forSuccessBuilder()
                .withData("items", content)
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
        ImgBed bed=imgBedService.save(imgBed);
        final IFileUpload savedBed=fileUpload;
        return ResponseResult.forSuccessBuilder()
                .withMessage("上传：" + type.getValue() + " 成功")
                .withData("address", qiNiuProperties.getHostName() + "/" + savedBed.getRelativeName())
                .withData("host", qiNiuProperties.getHostName())
                .withData("relativeAddress", savedBed.getRelativeName()).build();

    }

    @ExceptionHandler(value={FileUploadException.class})
    public ResponseResult handleFileError(Exception e) {
        return ResponseResult.forFailureBuilder()
                .withMessage("上传文件失败")
                .build();
    }

}
