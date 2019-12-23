package com.ad.admain.controller.pay;

import com.ad.admain.controller.ResponseResult;
import com.ad.admain.controller.pay.to.BillInfo;
import com.ad.admain.controller.pay.to.Order;
import com.ad.admain.controller.pay.to.Value;
import com.ad.admain.pay.ZfbPayHolder;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/order")
//@PreAuthorize("isAuthenticated()")
public class OrderController {

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
    public ResponseResult create(@RequestBody Order order) throws Exception {
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

}
