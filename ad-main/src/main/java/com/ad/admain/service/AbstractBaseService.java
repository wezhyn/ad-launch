package com.ad.admain.service;

import com.ad.admain.exception.DeleteOperateException;
import com.ad.admain.common.IBaseTo;
import com.ad.admain.exception.UpdateOperationException;
import com.ad.admain.utils.PropertyUtils;
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
public abstract class AbstractBaseService<T extends IBaseTo<ID>,ID> implements BaseService<T, ID> {


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
    @Transactional(rollbackFor = UpdateOperationException.class)
    public T update(T newObject) {
        T oldObject = getRepository().findById(newObject.getId())
                .orElseThrow(() -> new UpdateOperationException("无法更新，无目标对象"));
        PropertyUtils.copyProperties(newObject,oldObject);
        return getRepository().save(oldObject);
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
