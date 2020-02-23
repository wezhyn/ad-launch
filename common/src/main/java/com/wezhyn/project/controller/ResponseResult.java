package com.wezhyn.project.controller;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : wezhyn
 * @date : 2019/09/19
 */
@SuppressWarnings("unused")
@Data
public class ResponseResult {
    /*
        /**********************************************************
        /* 基本信息： code,message ,data中封装其他数据
        /**********************************************************
    */

    /**
     * 成功：20000
     */
    private int code;
    private Map<String, Object> data;
    private String message;
    /**
     * 访问路径
     */
    private String path;

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
        this.path=builder.path;
    }

    public static Builder forSuccessBuilder() {
        return new Builder(ResponseType.SUCCESS);
    }

    public static Builder forFailureBuilder() {
        return new Builder(ResponseType.GENERIC_FAIL);
    }

    public static Builder forFailureBuilder(ResponseType type) {
        return new Builder(type);
    }

    public static Builder forFailureBuilder(Integer failureCode) {
        if (failureCode==null || failureCode==0) {
            return forFailureBuilder();
        }
        return new Builder(failureCode);
    }

    public static Builder forHttpStatusCode(Integer statusCode) {
        if (statusCode==null) {
            return forFailureBuilder();
        }
        switch (statusCode) {
            case 200:
                return new Builder(ResponseType.SUCCESS);
            case 401:
            case 403:
                return new Builder(ResponseType.NO_LOGIN_AUTHENTICATION);
            case 404:
                return new Builder(ResponseType.NOT_FOUND);
            case 500:
                return new Builder(ResponseType.SERVER_EXCEPTION);

            default:
                return new Builder(ResponseType.GENERIC_FAIL);
        }

    }


    public static final class Builder {

        private int code;
        private Map<String, Object> data;
        private String message;
        private String path;

        private Builder(ResponseType type) {
            this.code=type.getNumber();
            this.message=type.getValue();
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
            if (message!=null) {
                this.message=message;
            }
            return this;
        }

        public Builder withPath(String path) {
            if (path!=null) {
                this.path=path;
            }
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
