package com.ad.admain.controller.pay.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author wezhyn
 * @since 05.07.2020
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderDtoWithUser extends AbstractOrderDto {

    private String username;

    public static OrderDtoWithUserBuilder builder() {
        return new OrderDtoWithUserBuilder();
    }

    public static final class OrderDtoWithUserBuilder {
        private String username;
        private int id;
        private int uid;
        private String verify;
        private int produceId;
        private String tradeOut;
        private List<String> produceContext;
        private Integer deliverNum;
        private Double totalAmount;
        private Integer num;
        private Double latitude;
        private Double longitude;
        private boolean vertical;
        private String orderStatus;

        private Double scope;
        private Integer rate;
        private Double price;
        private String startDate;
        private String endDate;
        private String startTime;
        private String endTime;
        private String createTime;

        private OrderDtoWithUserBuilder() {
        }

        public static OrderDtoWithUserBuilder anOrderDtoWithUser() {
            return new OrderDtoWithUserBuilder();
        }

        public OrderDtoWithUserBuilder username(String username) {
            this.username = username;
            return this;
        }

        public OrderDtoWithUserBuilder id(int id) {
            this.id = id;
            return this;
        }

        public OrderDtoWithUserBuilder uid(int uid) {
            this.uid = uid;
            return this;
        }

        public OrderDtoWithUserBuilder verify(String verify) {
            this.verify = verify;
            return this;
        }

        public OrderDtoWithUserBuilder produceId(int produceId) {
            this.produceId = produceId;
            return this;
        }

        public OrderDtoWithUserBuilder tradeOut(String tradeOut) {
            this.tradeOut = tradeOut;
            return this;
        }

        public OrderDtoWithUserBuilder produceContext(List<String> produceContext) {
            this.produceContext = produceContext;
            return this;
        }

        public OrderDtoWithUserBuilder deliverNum(Integer deliverNum) {
            this.deliverNum = deliverNum;
            return this;
        }

        public OrderDtoWithUserBuilder totalAmount(Double totalAmount) {
            this.totalAmount = totalAmount;
            return this;
        }

        public OrderDtoWithUserBuilder num(Integer num) {
            this.num = num;
            return this;
        }

        public OrderDtoWithUserBuilder latitude(Double latitude) {
            this.latitude = latitude;
            return this;
        }

        public OrderDtoWithUserBuilder longitude(Double longitude) {
            this.longitude = longitude;
            return this;
        }

        public OrderDtoWithUserBuilder vertical(boolean vertical) {
            this.vertical = vertical;
            return this;
        }

        public OrderDtoWithUserBuilder orderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
            return this;
        }

        public OrderDtoWithUserBuilder scope(Double scope) {
            this.scope = scope;
            return this;
        }

        public OrderDtoWithUserBuilder rate(Integer rate) {
            this.rate = rate;
            return this;
        }

        public OrderDtoWithUserBuilder price(Double price) {
            this.price = price;
            return this;
        }

        public OrderDtoWithUserBuilder startDate(String startDate) {
            this.startDate = startDate;
            return this;
        }

        public OrderDtoWithUserBuilder endDate(String endDate) {
            this.endDate = endDate;
            return this;
        }

        public OrderDtoWithUserBuilder startTime(String startTime) {
            this.startTime = startTime;
            return this;
        }

        public OrderDtoWithUserBuilder endTime(String endTime) {
            this.endTime = endTime;
            return this;
        }

        public OrderDtoWithUserBuilder createTime(String createTime) {
            this.createTime = createTime;
            return this;
        }

        public OrderDtoWithUser build() {
            OrderDtoWithUser orderDtoWithUser = new OrderDtoWithUser(username);
            orderDtoWithUser.setId(id);
            orderDtoWithUser.setUid(uid);
            orderDtoWithUser.setVerify(verify);
            orderDtoWithUser.setProduceId(produceId);
            orderDtoWithUser.setTradeOut(tradeOut);
            orderDtoWithUser.setProduceContext(produceContext);
            orderDtoWithUser.setDeliverNum(deliverNum);
            orderDtoWithUser.setTotalAmount(totalAmount);
            orderDtoWithUser.setNum(num);
            orderDtoWithUser.setLatitude(latitude);
            orderDtoWithUser.setLongitude(longitude);
            orderDtoWithUser.setVertical(vertical);
            orderDtoWithUser.setOrderStatus(orderStatus);
            orderDtoWithUser.setScope(scope);
            orderDtoWithUser.setRate(rate);
            orderDtoWithUser.setPrice(price);
            orderDtoWithUser.setStartDate(startDate);
            orderDtoWithUser.setEndDate(endDate);
            orderDtoWithUser.setStartTime(startTime);
            orderDtoWithUser.setEndTime(endTime);
            orderDtoWithUser.setCreateTime(createTime);
            return orderDtoWithUser;
        }
    }
}
