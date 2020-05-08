package com.ad.admain.controller.pay.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author wezhyn
 */
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class OrderDto extends AbstractOrderDto {

    private String feedback;

    public static OrderDtoBuilder builder() {
        return new OrderDtoBuilder();
    }

    public static final class OrderDtoBuilder {
        private String feedback;
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

        private OrderDtoBuilder() {
        }

        public static OrderDtoBuilder anOrderDto() {
            return new OrderDtoBuilder();
        }

        public OrderDtoBuilder feedback(String feedback) {
            this.feedback = feedback;
            return this;
        }

        public OrderDtoBuilder id(int id) {
            this.id = id;
            return this;
        }

        public OrderDtoBuilder uid(int uid) {
            this.uid = uid;
            return this;
        }

        public OrderDtoBuilder verify(String verify) {
            this.verify = verify;
            return this;
        }

        public OrderDtoBuilder produceId(int produceId) {
            this.produceId = produceId;
            return this;
        }

        public OrderDtoBuilder tradeOut(String tradeOut) {
            this.tradeOut = tradeOut;
            return this;
        }

        public OrderDtoBuilder produceContext(List<String> produceContext) {
            this.produceContext = produceContext;
            return this;
        }

        public OrderDtoBuilder deliverNum(Integer deliverNum) {
            this.deliverNum = deliverNum;
            return this;
        }

        public OrderDtoBuilder totalAmount(Double totalAmount) {
            this.totalAmount = totalAmount;
            return this;
        }

        public OrderDtoBuilder num(Integer num) {
            this.num = num;
            return this;
        }

        public OrderDtoBuilder latitude(Double latitude) {
            this.latitude = latitude;
            return this;
        }

        public OrderDtoBuilder longitude(Double longitude) {
            this.longitude = longitude;
            return this;
        }

        public OrderDtoBuilder vertical(boolean vertical) {
            this.vertical = vertical;
            return this;
        }

        public OrderDtoBuilder orderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
            return this;
        }

        public OrderDtoBuilder scope(Double scope) {
            this.scope = scope;
            return this;
        }

        public OrderDtoBuilder rate(Integer rate) {
            this.rate = rate;
            return this;
        }

        public OrderDtoBuilder price(Double price) {
            this.price = price;
            return this;
        }

        public OrderDtoBuilder startDate(String startDate) {
            this.startDate = startDate;
            return this;
        }

        public OrderDtoBuilder endDate(String endDate) {
            this.endDate = endDate;
            return this;
        }

        public OrderDtoBuilder startTime(String startTime) {
            this.startTime = startTime;
            return this;
        }

        public OrderDtoBuilder endTime(String endTime) {
            this.endTime = endTime;
            return this;
        }

        public OrderDtoBuilder createTime(String createTime) {
            this.createTime = createTime;
            return this;
        }

        public OrderDto build() {
            OrderDto orderDto = new OrderDto(feedback);
            orderDto.setId(id);
            orderDto.setUid(uid);
            orderDto.setVerify(verify);
            orderDto.setProduceId(produceId);
            orderDto.setTradeOut(tradeOut);
            orderDto.setProduceContext(produceContext);
            orderDto.setDeliverNum(deliverNum);
            orderDto.setTotalAmount(totalAmount);
            orderDto.setNum(num);
            orderDto.setLatitude(latitude);
            orderDto.setLongitude(longitude);
            orderDto.setVertical(vertical);
            orderDto.setOrderStatus(orderStatus);
            orderDto.setScope(scope);
            orderDto.setRate(rate);
            orderDto.setPrice(price);
            orderDto.setStartDate(startDate);
            orderDto.setEndDate(endDate);
            orderDto.setStartTime(startTime);
            orderDto.setEndTime(endTime);
            orderDto.setCreateTime(createTime);
            return orderDto;
        }
    }
}
