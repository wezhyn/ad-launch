package com.ad.admain.controller.pay;

import com.ad.admain.controller.AbstractBaseController;
import com.ad.admain.controller.pay.dto.BillInfoDto;
import com.ad.admain.controller.pay.to.BillInfo;
import com.ad.admain.convert.AbstractMapper;
import com.ad.admain.convert.BillInfoMapper;
import com.wezhyn.project.controller.ResponseResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

/**
 * @author wezhyn
 * @since 01.01.2020
 */
@RestController
@RequestMapping("/api/billinfo")
public class BillInfoController extends AbstractBaseController<BillInfoDto, Integer, BillInfo> {


    private final BillInfoService billInfoService;
    private final BillInfoMapper billInfoMapper;

    public BillInfoController(BillInfoService billInfoService, BillInfoMapper billInfoMapper) {
        this.billInfoService=billInfoService;
        this.billInfoMapper=billInfoMapper;
    }

    @GetMapping("/list")
    public ResponseResult listBillInfos(
            @RequestParam(name="limit", defaultValue="10") int limit,
            @RequestParam(name="page", defaultValue="1") int page) {
        return listDto(limit, page);
    }

    @GetMapping("/{id}")
    public ResponseResult showCurrentBillInfo(@PathVariable Integer id) {
        return currentIdResult(id);
    }

    @PostMapping("/search/{type}")
    public ResponseResult searchOrder(
            @RequestParam(name="limit", defaultValue="10") int limit,
            @RequestParam(name="page", defaultValue="1") int page,
            @PathVariable("type") BillInfoSearchType searchType,
            @RequestParam("context") String context) {
        Pageable pageable=PageRequest.of(page - 1, limit);
        Page<BillInfo> searchResult=getService().search(searchType, context, pageable);
        return doResponse(searchResult);
    }


    @Override
    public BillInfoService getService() {
        return billInfoService;
    }

    @Override
    public AbstractMapper<BillInfo, BillInfoDto> getConvertMapper() {
        return billInfoMapper;
    }
}
