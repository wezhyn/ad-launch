package com.wezhyn.project.controller;

import com.wezhyn.project.IBaseTo;
import org.springframework.data.domain.Example;

/**
 * @author wezhyn
 * @since 12.31.2019
 */
public interface BaseController<T, ID, U extends IBaseTo<ID>> {

    ResponseResult listDto(int limit, int page);

    ResponseResult listDto(int limit, int page, Example<U> example);

    /**
     * 创建 实体类
     *
     * @param entityDto entityDto
     * @return ResponseResult
     */
    ResponseResult createTo(T entityDto);

    /**
     * 实体Dto
     *
     * @param entityDto 需要更新的信息，与实体Id
     * @return ResponseResult
     */
    ResponseResult update(T entityDto);

    /**
     * 删除dto 对应的实体
     *
     * @param entityDto entityDto
     * @return ResponseResult
     */
    public ResponseResult delete(T entityDto);

}
