package com.ad.admain.controller;

import com.ad.admain.service.OrderService;
import com.ad.admain.to.Order;
import com.ad.admain.to.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("api/order")
public class OrderController {


    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @RequestMapping("/create")
    public ResponseResult create(@RequestBody Order order) throws Exception {
        Optional<Order> optional = orderService.save(order);
        Order result = optional.orElse(null);
        if (result!=null){
            return new ResponseResult().forSuccessBuilder()
                    .withCode(20000)
                    .withData("order",result)
                    .withMessage("创建订单成功")
                    .build();
        }
        else
            return new ResponseResult().forFailureBuilder()
                    .withCode(50000)
                    .withData("error",result)
                    .withMessage("创建订单失败")
                    .build();

    }
}
