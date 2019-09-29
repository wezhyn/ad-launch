package com.ad.admain.annotation;

import java.lang.annotation.*;

/**
 * @author wezhyn
 * @date 2019/09/27
 * <p>
 * 使用 {@link com.ad.admain.utils.PropertyUtils#copyProperties} 时，
 * 忽略 source中有{@link UpdateIgnore}注解的字段
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface UpdateIgnore {
}
