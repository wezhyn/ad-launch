package com.ad.admain.controller.pay;

import com.ad.admain.controller.AbstractBaseController;
import com.ad.admain.controller.equipment.dto.EquipmentDto;
import com.ad.admain.controller.pay.dto.AdProduceDto;
import com.ad.admain.controller.pay.dto.OrderDto;
import com.ad.admain.controller.pay.to.AdOrder;
import com.ad.admain.controller.pay.to.AdProduce;
import com.ad.admain.security.AdAuthentication;
import com.wezhyn.project.controller.ResponseResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author : lb
 * @date : 2019/12/31
 */
@RestController
@RequestMapping("api/order")
@PreAuthorize("isAuthenticated()")
public class OrderController extends AbstractBaseController<OrderDto, Integer, AdOrder> {

    private final AdOrderMapper orderMapper;
    private final AdOrderService orderService;
    private final BillInfoService orderInfoService;
    private final ProduceMapper produceMapper;


    public OrderController(AdOrderService orderService, BillInfoService orderInfoService, AdOrderMapper orderMapper, ProduceMapper produceMapper) {
        this.orderService=orderService;
        this.orderInfoService=orderInfoService;
        this.orderMapper=orderMapper;
        this.produceMapper=produceMapper;
    }

    /**
     * 创建订单，并返回订单信息用于支付宝支付：orderInfo
     *
     * @param produceDto 订单创建内容
     * @return orderInfo
     * @throws Exception order
     */
    @RequestMapping("/create")
    public ResponseResult create(@Valid @RequestBody AdProduceDto produceDto, BindingResult bindingResult, @AuthenticationPrincipal AdAuthentication authentication) throws Exception {
//        设置当前用户id
        final AdProduce produce=produceMapper.toTo(produceDto);
        if (bindingResult.hasErrors()) {
            return handleBindingResult(bindingResult);
        }
        AdOrder order=new AdOrder(authentication.getId(), produce);
        AdOrder savedOrder=orderService.save(order);
        if (savedOrder.getId()!=null) {
            return ResponseResult.forSuccessBuilder()
                    .withData("id", savedOrder.getId())
                    .withMessage("创建订单成功")
                    .build();
        }

        return ResponseResult.forFailureBuilder()
                .withMessage("系统异常")
                .build();
    }

    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    public ResponseResult getList(@RequestParam(name="limit", defaultValue="10") int limit,
                                  @RequestParam(name="page", defaultValue="1") int page,
                                  @AuthenticationPrincipal AdAuthentication authentication) {

        if (authentication.isAdmin()) {
            return listDto(limit, page);
        } else {
            final Page<AdOrder> adOrders=getService().listUserOrders(authentication.getId(), PageRequest.of(page - 1, limit));
            return doResponse(adOrders);
        }
    }


    @PostMapping("/search/{type}")
    public ResponseResult searchOrder(
            @RequestParam(name="limit", defaultValue="10") int limit,
            @RequestParam(name="page", defaultValue="1") int page,
            @PathVariable("type") OrderSearchType searchType,
            @RequestParam("context") String context) {
        Pageable pageable=PageRequest.of(page - 1, limit);
        Page<AdOrder> searchResult=getService().search(searchType, context, pageable);
        return doResponse(searchResult);
    }

    @PostMapping("/verify")
    public ResponseResult verifyOrder(@RequestBody OrderDto orderDto) {
        AdOrder order=getConvertMapper().toTo(orderDto);
        AdOrder verifyEquipment=getService().update(order);
        return ResponseResult.forSuccessBuilder().withMessage("修改成功").build();
    }

    @PostMapping("/delete")
    public ResponseResult deleteUser(@RequestBody EquipmentDto equipmentDto) {
        getService().delete(equipmentDto.getId());
        return ResponseResult.forSuccessBuilder()
                .withMessage("删除成功").build();
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
