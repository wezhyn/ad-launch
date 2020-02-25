package com.ad.admain.convert;

/**
 * 序列化时 DateTime -> String
 * 用来标识某个字段返回时需要补全到日期-时间 格式，
 * 若无日期，以当前日期补充
 * 若去时间，以00：00补充
 *
 * @author wezhyn
 * @since 02.25.2020
 */
public @interface DateTimeFormatter {
}
