package com.ad.adlaunch.service;

import com.ad.adlaunch.exception.DeleteOperateException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : wezhyn
 * @date : 2019/09/24
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public abstract class AbstractBaseService<T,ID> implements BaseService<T, ID> {


    @Override
    public T getById(ID id) {
        return getRepository().findById(id)
                .orElse(getEmpty());
    }

    @Override
    public T save(T object) {
        return getRepository().save(object);
    }

    @Override
    public Page<T> getList(Pageable pageable) {
       return getRepository().findAll(pageable);
    }

    @Override
    public T update(T newObject) {
        return getRepository().save(newObject);
    }

    @Override
    @Transactional(rollbackFor=DeleteOperateException.class)
    public void delete(ID id) {
        getRepository().deleteById(id);
    }

    /**
     * 获取对应的repository
     * @return jpsRepository
     */
    public abstract JpaRepository<T, ID> getRepository() ;

    /**
     * 用 Empty取代null
     * @return Empty对象
     */
    public abstract T getEmpty();

}
