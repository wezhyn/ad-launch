package com.ad.admain.controller.pay;

import com.ad.admain.controller.AbstractBaseController;
import com.ad.admain.controller.pay.convert.AdOrderMapper;
import com.ad.admain.controller.pay.dto.OrderDto;
import com.ad.admain.controller.pay.impl.OrderServiceImpl;
import com.ad.admain.controller.pay.to.AdOrder;
import com.wezhyn.project.controller.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wezhyn
 * @since 05.18.2020
 */
@RestController
@RequestMapping("/api/order")
public class AdViewHistoryController extends AbstractBaseController<OrderDto, Integer, AdOrder> {


    @Autowired
    private OrderServiceImpl orderService;
    @Autowired
    private AdOrderMapper orderMapper;

    @RequestMapping("/recent")
    public ResponseResult recentAd(
            @RequestParam(name = "limit", defaultValue = "10") int limit,
            @RequestParam(name = "page", defaultValue = "1") int page) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        final Page<AdOrder> list = orderService.getPayList(PageRequest.of(page - 1, limit, sort));
        return doResponse(list);
    }

    @Override
    public AdOrderService getService() {
        return orderService;
    }

    @Override
    public AdOrderMapper getConvertMapper() {
        return orderMapper;
    }
}
