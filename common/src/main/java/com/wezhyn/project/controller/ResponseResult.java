package com.wezhyn.project.controller;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : wezhyn
 * @date : 2019/09/19
 */
@Data
public class ResponseResult {
    /*
        /**********************************************************
        /* 基本信息： code,message ,data中封装其他数据
        /**********************************************************
    */

    public static final int SUCCESS_CODE=20000;
    public static final int FAILURE_CODE=50000;
    /**
     * 成功：20000
     */
    private int code;
    private Map<String, Object> data;
    private String message;

    public ResponseResult() {
    }
    /*
        /**********************************************************
        /* 构造器 只能通过builder方法创建对象
        /**********************************************************
    */

    private ResponseResult(Builder builder) {
        this.code=builder.code;
        this.message=builder.message;
//        返回的 data 不允许被修改
        this.data=builder.data;
    }

    public static Builder forSuccessBuilder() {
        return new Builder(SUCCESS_CODE);
    }

    public static Builder forFailureBuilder() {
        return new Builder(FAILURE_CODE);
    }

    public static Builder forFailureBuilder(int failureCode) {
        if (failureCode==0) {
            failureCode=FAILURE_CODE;
        }
        return new Builder(failureCode);
    }


    public static final class Builder {

        private int code;
        private Map<String, Object> data;
        private String message;

        private Builder() {
        }

        private Builder(int code) {
            this.code=code;
        }

        public Builder withCode(int code) {
            this.code=code;
            return this;
        }

        public Builder withList(Object value, long itemNum) {
            return withData("items", value)
                    .withData("total", itemNum);
        }

        public Builder withMessage(String message) {
            this.message=message;
            return this;
        }

        public Builder withData(String key, Object value) {
            if (Builder.this.data==null) {
                Builder.this.data=new HashMap<>(8);
            }
            Builder.this.data.put(key, value);
            return this;
        }

        public ResponseResult build() {
            return new ResponseResult(this);
        }
    }

}
