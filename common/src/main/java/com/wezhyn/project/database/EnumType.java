package com.wezhyn.project.database;

/**
 * 定义枚举类型具体转变类型, 类似与 1 <-> NumberEnum; "1" <-> StringEnum
 *
 * @author wezhyn
 * @since 12.11.2019
 */
public enum EnumType {
    /**
     * 采用何种类型的数据持久化枚举类型
     */
    NUMBER,

    STRING,

    /**
     * 默认策略
     */
    TO_STRING,


}
