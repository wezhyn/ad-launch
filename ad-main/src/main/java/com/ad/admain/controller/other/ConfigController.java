package com.ad.admain.controller.other;

import com.ad.admain.controller.ImgBedService;
import com.ad.admain.controller.impl.ImgBed;
import com.ad.admain.controller.impl.ImgBedType;
import com.ad.admain.convert.ImgBedConfigMapper;
import com.wezhyn.project.controller.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

;

/**
 * @author wezhyn
 * @date 2019/11/04
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@RequestMapping("/api/config")
@RestController
public class ConfigController {


    @Autowired
    private ImgBedService imgBedService;
    @Autowired
    private ImgBedConfigMapper imgBedConfigMapper;

    @GetMapping("/guideList")
    public ResponseResult getGuideList(@RequestParam(name="limit", defaultValue="10") int limit, @RequestParam(name="page", defaultValue="1") int page) {
        Pageable pageable=PageRequest.of(page - 1, limit);
        Page<ImgBed> imgBeds=imgBedService.getImgBedListByType(ImgBedType.GUIDE, pageable);
        List<ImgBed> content=imgBeds.getContent();
        if (content.size() < limit) {
            List<ImgBed> contentCopy=new ArrayList<>(content);
            for (int i=0; i < limit - content.size(); i++) {
                ImgBed imgBed=ImgBed.forGuide(-1, "", "");
                contentCopy.add(imgBed);
            }
            content=contentCopy;
        }
        return ResponseResult.forSuccessBuilder()
                .withData("items", imgBedConfigMapper.toDtoList(content))
                .withData("total", imgBeds.getTotalElements())
                .build();
    }

    @PostMapping(value={"/deleteShuffing", "/deleteGuide"})
    public ResponseResult deleteShuffing(@RequestBody ConfigDto configDto) {
        imgBedService.delete(configDto.getId());
        return ResponseResult.forSuccessBuilder()
                .withMessage("删除成功").build();
    }

    /**
     * 根据分页数据返回固定的图片数量，少于分页时，添加空
     *
     * @param limit limit
     * @param page  page
     * @return list
     */
    @GetMapping("/shuffingList")
    public ResponseResult getShuffingList(@RequestParam(name="limit", defaultValue="10") int limit, @RequestParam(name="page", defaultValue="1") int page) {
        Pageable pageable=PageRequest.of(page - 1, limit);
        Page<ImgBed> imgBeds=imgBedService.getImgBedListByType(ImgBedType.SHUFFING, pageable);
        return ResponseResult.forSuccessBuilder()
                .withData("items", imgBedConfigMapper.toDtoList(imgBeds.getContent()))
                .withData("total", imgBeds.getTotalElements())
                .build();
    }
}
