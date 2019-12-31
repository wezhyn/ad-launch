package com.ad.admain.controller.pay;

import com.ad.admain.controller.AbstractBaseController;
import com.ad.admain.controller.equipment.dto.EquipmentDto;
import com.ad.admain.controller.pay.dto.OrderDto;
import com.ad.admain.controller.pay.to.BillInfo;
import com.ad.admain.controller.pay.to.Order;
import com.ad.admain.controller.pay.to.Value;
import com.ad.admain.convert.OrderMapper;
import com.ad.admain.pay.ZfbPayHolder;
import com.ad.admain.security.AdAuthentication;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.wezhyn.project.controller.ResponseResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author : lb
 * @date : 2019/12/31
 */
@RestController
@RequestMapping("api/order")
@PreAuthorize("isAuthenticated()")
public class OrderController extends AbstractBaseController<OrderDto, Integer, Order> {

    private final OrderMapper orderMapper;

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

    public OrderController(OrderService orderService, OrderInfoService orderInfoService, OrderMapper orderMapper) {
        this.orderService=orderService;
        this.orderInfoService=orderInfoService;
        this.orderMapper=orderMapper;
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
        BillInfo savedOrder=orderInfoService.createOrder(order);

        Order sOrder=orderService.getById(savedOrder.getId()).get();
        savedOrder.setOrder(sOrder);
        String orderInfoSign=ZfbPayHolder.signZfb(savedOrder.getOrder(), ORDER_ALIPAY_MAPPER);
        return ResponseResult.forSuccessBuilder()
                .withCode(20000)
                .withData("order", savedOrder.getOrder())
                .withData("orderInfo", orderInfoSign)
                .withMessage("创建订单成功")
                .build();
    }

    @GetMapping("/list")
    public ResponseResult getList(@RequestParam(name="limit", defaultValue="10") int limit,
                                  @RequestParam(name="page", defaultValue="1") int page) {
        return listDto(limit, page);
    }

    @GetMapping("/mylist")
    public ResponseResult getUserList(@AuthenticationPrincipal AdAuthentication adAuthentication,
                                      @RequestParam(name="limit", defaultValue="10") int limit,
                                      @RequestParam(name="page", defaultValue="1") int page
    ) {
        final Page<Order> search=getService().search(OrderSearchType.USER, adAuthentication.getId().toString(), PageRequest.of(page - 1, limit));
        return doResponse(search);
    }


    @PostMapping("/search/{type}")
    public ResponseResult searchOrder(
            @RequestParam(name="limit", defaultValue="10") int limit,
            @RequestParam(name="page", defaultValue="1") int page,
            @PathVariable("type") OrderSearchType searchType,
            @RequestParam("context") String context) {
        Pageable pageable=PageRequest.of(page - 1, limit);
        Page<Order> searchResult=getService().search(searchType, context, pageable);
        return doResponse(searchResult);
    }

    @PostMapping("/verify")
    public ResponseResult verifyOrder(@RequestBody OrderDto orderDto) {
        Order order=getConvertMapper().toTo(orderDto);
        Order verifyEquipment=getService().update(order);
        return ResponseResult.forSuccessBuilder().withMessage("修改成功").build();
    }

    @PostMapping("/delete")
    public ResponseResult deleteUser(@RequestBody EquipmentDto equipmentDto) {
        getService().delete(equipmentDto.getId());
        return ResponseResult.forSuccessBuilder()
                .withMessage("删除成功").build();
    }

    @Override
    public OrderService getService() {
        return orderService;
    }

    @Override
    public OrderMapper getConvertMapper() {
        return orderMapper;
    }
}
