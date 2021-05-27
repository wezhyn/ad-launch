package com.ad.admain.controller.pay;

import java.util.Optional;

import javax.validation.Valid;

import com.ad.admain.controller.AbstractBaseController;
import com.ad.admain.controller.pay.convert.AdOrderMapper;
import com.ad.admain.controller.pay.convert.AdOrderWithUserMapper;
import com.ad.admain.controller.pay.convert.ProduceMapper;
import com.ad.admain.controller.pay.dto.AdProduceDto;
import com.ad.admain.controller.pay.dto.OrderDto;
import com.ad.admain.controller.pay.to.AdOrder;
import com.ad.admain.controller.pay.to.AdProduce;
import com.ad.admain.controller.pay.to.OrderStatus;
import com.ad.admain.mq.order.CheckOrderMessage;
import com.ad.admain.mq.order.CheckOrderProduceImpl;
import com.ad.admain.security.AdAuthentication;
import com.ad.launch.order.TaskMessage;
import com.wezhyn.project.controller.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    @Autowired
    private AdOrderWithUserMapper adOrderWithUserMapper;
    @Autowired
    private CheckOrderProduceImpl orderProduce;


    public OrderController(AdOrderService orderService, BillInfoService orderInfoService, AdOrderMapper orderMapper, ProduceMapper produceMapper) {
        this.orderService = orderService;
        this.orderInfoService = orderInfoService;
        this.orderMapper = orderMapper;
        this.produceMapper = produceMapper;
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
        final AdProduce produce = produceMapper.toTo(produceDto);
        if (bindingResult.hasErrors()) {
            return handleBindingResult(bindingResult);
        }
        AdOrder order = new AdOrder(authentication.getId(), produce);
        AdOrder savedOrder = orderService.save(order);
        orderProduce.checkOrder(new CheckOrderMessage(savedOrder.getId(), savedOrder.getUid()));
        if (savedOrder.getId() != null) {
            return ResponseResult.forSuccessBuilder()
                    .withData("id", savedOrder.getId())
                    .withMessage("创建订单成功")
                    .build();
        }

        return ResponseResult.forFailureBuilder()
                .withMessage("系统异常")
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseResult deleteOrder(
            @PathVariable(name = "id") Integer orderId, @AuthenticationPrincipal AdAuthentication authentication) {
        if (!orderService.isUserOrder(orderId, authentication.getId())) {
            throw new RuntimeException("非法访问订单");
        }
        orderService.delete(orderId);
        return ResponseResult.forSuccessBuilder()
                .withMessage("删除订单成功").build();
    }

    @GetMapping("/{id}")
    public ResponseResult userId(@PathVariable Integer id, @AuthenticationPrincipal AdAuthentication adAuthentication) {
        final Optional<AdOrder> userOrder = getService().findUserOrder(id, adAuthentication.getId());
        return doResponse(userOrder, "获取订单成功", "用户无该订单");
    }

    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    public ResponseResult getList(@RequestParam(name = "limit", defaultValue = "10") int limit,
                                  @RequestParam(name = "page", defaultValue = "1") int page,
                                  @AuthenticationPrincipal AdAuthentication authentication) {

        if (authentication.isAdmin()) {
            final Page<AdOrder> orders = getService().listOrdersWithUsername(limit, page);
            return ResponseResult.forSuccessBuilder()
                    .withMessage("获取列表成功")
                    .withData("items", adOrderWithUserMapper.toDtoList(orders.getContent()))
                    .withData("total", orders.getTotalElements())
                    .build();

        } else {
            final Page<AdOrder> adOrders = getService().listUserOrders(authentication.getId(), PageRequest.of(page - 1, limit, Sort.by("id").descending()));
            return doResponse(adOrders);
        }
    }


    @PostMapping("/search/{type}")
    public ResponseResult searchOrder(
            @RequestParam(name = "limit", defaultValue = "10") int limit,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @PathVariable("type") OrderSearchType searchType,
            @RequestParam("context") String context) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<AdOrder> searchResult = getService().search(searchType, context, pageable);
        return doResponse(searchResult);
    }

    @PostMapping("/verify")
    public ResponseResult verifyOrder(@RequestBody OrderDto orderDto) {
        AdOrder order = getConvertMapper().toTo(orderDto);
        AdOrder savedOrder = getService().findById(order.getId());
        if (savedOrder.getOrderStatus().getNumber() <= 0) {
            return ResponseResult.forFailureBuilder().withMessage(savedOrder.getOrderStatus().getValue()).build();
        }
        getService().verifyOrder(order);
        orderService.modifyOrderStatus(order.getId(), OrderStatus.EXECUTING);
        orderProduce.paymentOrder(createTask(savedOrder));
        return ResponseResult.forSuccessBuilder().withMessage("修改成功").build();
    }

    private TaskMessage createTask(AdOrder order) {
        return TaskMessage.builder()
                .deliverNum(order.getDeliverNum())
                .latitude(order.getLatitude())
                .longitude(order.getLongitude())
                .oid(order.getId())
                .produceContext(order.getProduceContext())
                .rate(order.getRate())
                .scope(order.getScope())
                .totalNum(order.getNum())
                .vertical(order.getProduce().getVertical())
                .uid(order.getUid())
                .build();

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
