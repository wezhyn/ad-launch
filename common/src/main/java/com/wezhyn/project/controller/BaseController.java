package com.wezhyn.project.controller;

/**
 * @author wezhyn
 * @since 12.31.2019
 */
public interface BaseController<T, ID> {


    ResponseResult listDto(int limit, int page);

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
