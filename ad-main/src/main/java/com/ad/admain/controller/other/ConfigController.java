package com.ad.admain.controller.other;

import com.ad.admain.controller.ImgBedService;
import com.ad.admain.convert.ImgBedConfigMapper;
import com.ad.admain.dto.ConfigDto;
import com.ad.admain.enumate.ImgBedType;
import com.ad.admain.to.ImgBed;
import com.ad.admain.to.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

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
        return ResponseResult.forSuccessBuilder()
                .withData("items", imgBedConfigMapper.toDtoList(imgBeds.getContent()))
                .withData("total", imgBeds.getTotalElements())
                .build();
    }

    @PostMapping(value={"/deleteShuffing", "/deleteGuide"})
    public ResponseResult deleteShuffing(@RequestBody ConfigDto configDto) {
        imgBedService.delete(configDto.getId());
        return ResponseResult.forSuccessBuilder()
                .withMessage("删除成功").build();
    }

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
