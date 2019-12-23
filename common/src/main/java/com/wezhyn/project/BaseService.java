package com.wezhyn.project;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * @author : wezhyn
 * @date : 2019/09/24
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public interface BaseService<T,ID> {

    /**
     * 通过账号获取用户信息
     * @param id userName @NotNull ,应当确保传入的对象非空
     * @return T id对应的对象
     */
    Optional<T> getById(ID id);

    /**
     * 储存目标对象
     * @param object 储存的对象
     * @return 储存后的对象
     */
    Optional<T> save(T object);


    /**
     * 更新目标对象，根据主键
     * @param newObject 新的完整参数
     * @return T
     */
    Optional<T> update(T newObject);

    /**
     * 根据主键删除对象
     * @param id 主键
     */
    void delete(ID id);




    /**
     * 根据传进来 limit 和 page组成 pageable的进行分页
     * @param pageable pageable {@link new Pageable}
     * @return 分页
     */
    Page<T> getList(Pageable pageable);



}
