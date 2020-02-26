package com.ad.admain.convert;


import com.wezhyn.project.utils.PropertyUtils;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.MappingTarget;

import java.util.Collection;
import java.util.List;

/**
 * @author wezhyn
 * @date 2019/10/05
 * <p>
 * SOURCE -> to: mysql端数据
 * TARGET -> dto: 前台数据
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public interface AbstractMapper<SOURCE, TARGET> {

    /**
     * 实现 to -> dto 的转变
     * 子类覆盖该方法，添加{@link org.mapstruct.Mappings} 来添加不同名的匹配
     *
     * @param source mysql端对象
     * @return 前端对象
*/
    TARGET toDto(SOURCE source);

    /**
     * 实现 dto -> to 的转变
     * {@link InheritInverseConfiguration} 可以继承 toDto子类自定义的@Mapping属性
     *
     * @param target 前端对象
     * @return mysql端对象
     */
    @InheritInverseConfiguration(name="toDto")
    SOURCE toTo(TARGET target);

    /**
     * to dto list
     *
     * @param sources sources list
     * @return target list
     */
    List<TARGET> toDtoList(Collection<SOURCE> sources);

    /**
     * update
     *
     * @param target target
     * @param source source
     */
    default void updateToIgnoreNull(TARGET target, @MappingTarget SOURCE source) {
        SOURCE targetSource=toTo(target);
        PropertyUtils.copyProperties(targetSource, source);
    }

    /**
     * to to list
     *
     * @param targets targets
     * @return sources
     */
    List<SOURCE> toToList(Collection<TARGET> targets);
}
