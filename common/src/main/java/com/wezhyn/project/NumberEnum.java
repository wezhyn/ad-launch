package com.wezhyn.project;

/**
 * 枚举类型的转变只通过其 Number 型字段进行转化
 * 用于 Mybatis，Hibernate 等ORM 框架映射枚举类型
 * number -> NumberEnum; NumberEnum -> number
 *
 * @author wezhyn
 * @since 12.11.2019
 */
@FunctionalInterface
public interface NumberEnum {

    /**
     * 获取当前枚举的内嵌数字
     *
     * @return Number
     */
    Integer getNumber();

}
