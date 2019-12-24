package com.wezhyn.project;

import com.wezhyn.project.exception.DeleteOperateException;
import com.wezhyn.project.exception.UpdateOperationException;
import com.wezhyn.project.utils.PropertyUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author : wezhyn
 * @date : 2019/09/24
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public abstract class AbstractBaseService<T extends IBaseTo<ID>, ID> implements BaseService<T, ID> {


    @Override
    public Optional<T> getById(ID id) {
        return getRepository().findById(id);
    }

    @Override
    public Optional<T> save(T object) {
        return Optional.of(getRepository().save(object));
    }

    @Override
    @Transactional(rollbackFor=UpdateOperationException.class)
    public Optional<T> update(T newObject) {
        T oldObject=getRepository().findById(newObject.getId())
                .orElseThrow(()->new UpdateOperationException("无法更新，无目标对象"));
        PropertyUtils.copyProperties(newObject, oldObject);
        return Optional.of(getRepository().save(oldObject));
    }

    @Override
    @Transactional(rollbackFor=DeleteOperateException.class)
    public void delete(ID id) {
        getRepository().deleteById(id);
    }

    @Override
    public Page<T> getList(Pageable pageable) {
        return getRepository().findAll(pageable);
    }

    /**
     * 获取对应的repository
     *
     * @return jpsRepository
     */
    public abstract JpaRepository<T, ID> getRepository();

    /**
     * 用 Empty取代null
     *
     * @return Empty对象
     */
    public Optional<T> getEmpty() {
        return Optional.empty();
    }


}
