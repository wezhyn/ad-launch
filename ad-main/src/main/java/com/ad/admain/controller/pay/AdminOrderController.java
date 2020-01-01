package com.ad.admain.controller.pay;

import com.ad.admain.controller.AbstractBaseController;
import com.ad.admain.controller.pay.dto.OrderDto;
import com.ad.admain.controller.pay.to.Order;
import com.ad.admain.convert.AbstractMapper;
import com.ad.admain.convert.OrderMapper;
import com.alibaba.fastjson.JSONObject;
import com.wezhyn.project.controller.ResponseResult;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wezhyn
 * @since 01.01.2020
 */
@RestController
@RequestMapping("/api/admin/order")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminOrderController extends AbstractBaseController<OrderDto, Integer, Order> {

    private final OrderMapper orderMapper;

    private final OrderService orderService;

    public AdminOrderController(OrderService orderService, OrderMapper orderMapper) {
        this.orderService=orderService;
        this.orderMapper=orderMapper;
    }


    /**
     * 批量删除订单接口
     *
     * @param jsonObject idList
     * @return responseResult
     */
    @PostMapping("/queryDelete")
    @SuppressWarnings("unchecked")
    public ResponseResult batchDelete(@RequestBody JSONObject jsonObject) {
        final Object idList=jsonObject.get("idList");
        List<Order> deleteList=new ArrayList<>();
        List<Integer> deleteIdList;
        if (List.class.isAssignableFrom(idList.getClass())) {
            deleteIdList=(List<Integer>) idList;
        } else if (idList.getClass().isArray()) {
            deleteIdList=new ArrayList<>();
            for (Integer i : (int[]) idList) {
                deleteIdList.add(i);
            }
        } else {
            return ResponseResult.forFailureBuilder()
                    .withMessage("无法识别传递的数据").build();
        }

        for (Integer integer : deleteIdList) {
            deleteList.add(new Order().setId(integer));
        }
        getService().batchDelete(deleteList);
        return doResponse("删除成功");
    }


    @Override
    public OrderService getService() {
        return orderService;
    }

    @Override
    public AbstractMapper<Order, OrderDto> getConvertMapper() {
        return orderMapper;
    }
}
