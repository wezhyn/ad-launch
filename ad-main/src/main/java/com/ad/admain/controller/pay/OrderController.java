package com.ad.admain.controller.pay;

import com.ad.admain.controller.ResponseResult;
import com.ad.admain.controller.equipment.dto.EquipmentDto;
import com.ad.admain.controller.pay.dto.OrderDto;
import com.ad.admain.controller.pay.to.BillInfo;
import com.ad.admain.controller.pay.to.Order;
import com.ad.admain.controller.pay.to.Value;
import com.ad.admain.convert.OrderMapper;
import com.ad.admain.pay.ZfbPayHolder;
import com.ad.admain.security.AdAuthentication;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/order")
//@PreAuthorize("isAuthenticated()")
public class OrderController {

    @Autowired
    private OrderMapper orderMapper;

    private static final Function<Order, AlipayTradeAppPayModel> ORDER_ALIPAY_MAPPER=o->{
        AlipayTradeAppPayModel model=new AlipayTradeAppPayModel();
        String body=o.getValueList().stream()
                .filter(Objects::nonNull)
                .map(Value::getVal)
                .collect(Collectors.joining(","));
        model.setBody(body);
        model.setSubject("ad-order-" + o.getId() + o.getLatitude() + ":" + o.getLongitude());
        model.setTotalAmount(String.valueOf(o.getPrice()));
        model.setOutTradeNo(String.valueOf(o.getId()));
        model.setProductCode("QUICK_MSECURITY_PAY");
        return model;
    };
    private final OrderService orderService;
    private final OrderInfoService orderInfoService;

    public OrderController(OrderService orderService, OrderInfoService orderInfoService) {
        this.orderService=orderService;
        this.orderInfoService=orderInfoService;
    }

    /**
     * 创建订单，并返回订单信息用于支付宝支付：orderInfo
     *
     * @param order 订单创建内容
     * @return orderInfo
     * @throws Exception order
     */
    @RequestMapping("/create")
    public ResponseResult create(@RequestBody Order order, @AuthenticationPrincipal AdAuthentication authentication) throws Exception {
//        设置当前用户id
        order.setUid(authentication.getId());
        Optional<BillInfo> savedOrder=orderInfoService.createOrder(order);
        return savedOrder.map(o->{
            Order sOrder=orderService.getById(o.getId()).get();
            o.setOrder(sOrder);
            String orderInfoSign=ZfbPayHolder.signZfb(o.getOrder(), ORDER_ALIPAY_MAPPER);
            return ResponseResult.forSuccessBuilder()
                    .withCode(20000)
                    .withData("order", o.getOrder())
                    .withData("orderInfo", orderInfoSign)
                    .withMessage("创建订单成功")
                    .build();
        }).orElse(ResponseResult.forFailureBuilder()
                .withCode(50000)
                .withMessage("创建订单失败")
                .build());
    }

    @GetMapping("/list")
    public ResponseResult getList(@RequestParam(name="limit", defaultValue="10") int limit,
                                  @RequestParam(name="page", defaultValue="1") int page) {
        Pageable pageable=PageRequest.of(page - 1, limit);
        Page<Order> equipmentList=getService().getList(pageable);
        return ResponseResult.forSuccessBuilder()
                .withData("items", orderMapper.toDtoList(equipmentList.getContent()))
                .withData("total", equipmentList.getTotalElements())
                .build();
    }


    @PostMapping("/search/{type}")
    public ResponseResult searchOrder(@RequestParam(name="limit", defaultValue="10") int limit,
                                      @RequestParam(name="page", defaultValue="1") int page,
                                      @PathVariable("type") OrderSearchType searchType,
                                      @RequestParam("context") String context) {
        Pageable pageable=PageRequest.of(page - 1, limit);
        Page<Order> searchResult=getService().search(searchType, context, pageable);
        return ResponseResult.forSuccessBuilder()
                .withMessage("searchType: " + searchType.getValue())
                .withData("items", getConvertMapper().toDtoList(searchResult.getContent()))
                .withData("total", searchResult.getTotalElements())
                .build();
    }

    @PostMapping("/verify")
    public ResponseResult verifyOrder(@RequestBody OrderDto orderDto) {
        Order order=getConvertMapper().toTo(orderDto);
        Optional<Order> verifyEquipment=getService().update(order);
        return verifyEquipment.map(
                e->{
                    return ResponseResult.forSuccessBuilder().withMessage("修改成功").build();
                }
        ).orElseGet(()->{
            return ResponseResult.forFailureBuilder().withMessage("修改失败").build();
        });
    }

    @PostMapping("/delete")
    public ResponseResult deleteUser(@RequestBody EquipmentDto equipmentDto) {
        getService().delete(equipmentDto.getId());
        return ResponseResult.forSuccessBuilder()
                .withMessage("删除成功").build();
    }

    public OrderService getService() {
        return orderService;
    }

    public OrderMapper getConvertMapper() {
        return orderMapper;
    }
}
