package com.ad.admain.controller;

import com.ad.admain.convert.AbstractMapper;
import com.wezhyn.project.BaseService;
import com.wezhyn.project.IBaseTo;
import com.wezhyn.project.controller.BaseController;
import com.wezhyn.project.controller.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.function.Function;

/**
 * T ：Dto
 * ID: 实体ID
 * U: Dto 对应的 Entity
 *
 * @author : wezhyn
 * @date : 2019/09/24
 */
@Slf4j
public abstract class AbstractBaseController<T, ID, U extends IBaseTo<ID>> implements BaseController<T, ID> {


    private final Function<ID, ResponseResult> deleteByIdMapper=id->{
        getService().delete(id);
        return ResponseResult.forSuccessBuilder()
                .withMessage("删除成功")
                .build();
    };


    /**
     * 获取分页列表：无条件查询
     *
     * @param limit 每页数量
     * @param page  第几页：从1开始
     * @return Response
     */
    @Override
    public ResponseResult listDto(int limit, int page) {
        final PageRequest pageable=PageRequest.of(page - 1, limit);
        final Page<U> list=getService().getList(pageable);
        return doResponse(list);
    }

    @Override
    public ResponseResult createTo(T entityDto) {
        U to=getConvertMapper().toTo(entityDto);
        to=preSave(to);
        U savedTo=getService().save(to);
        savedTo=afterSave(savedTo);
        return doResponse(savedTo, "创建成功");
    }

    @Override
    public ResponseResult update(T entityDto) {
        U to=getConvertMapper().toTo(entityDto);
        U updateTo=doUpdate(to);
        return doResponse(updateTo, "修改成功");
    }


    /**
     * 默认实现：根据Id删除实体类
     *
     * @param entityDto entityDto
     * @return ResponseResult
     */
    @Override

    public ResponseResult delete(T entityDto) {
        final Optional<ID> dtoId=getDtoId(entityDto);
        return dtoId.map(deleteByIdMapper)
                .orElseGet(()->ResponseResult.forFailureBuilder().withMessage("无对应实体主键").build());
    }

    /**
     * 条件查询
     *
     * @param limit         每页数量
     * @param page          第几页：从1开始
     * @param searchExample 查询的条件，至保存要查询的字段
     * @return response
     */
    public ResponseResult listsDto(int limit, int page, T searchExample) {
        final Page<U> list=getService().getList(PageRequest.of(page - 1, limit), getConvertMapper().toTo(searchExample));
        return doResponse(list);
    }

    public ResponseResult currentIdResult(ID currentId) {
        final Optional<U> idResult=getService().getById(currentId);
        return doResponse(idResult, "获取成功", "获取失败");
    }

    /**
     * 执行一些 在 save 之后的一些操作
     *
     * @param savedTo 要存储的实体类
     * @return U
     */
    protected U afterSave(U savedTo) {
        return savedTo;
    }

    /**
     * 执行一些 在 save 之前的一些操作
     *
     * @param to 要存储的实体类
     * @return U
     */
    protected U preSave(U to) {
        return to;
    }

    protected U doUpdate(U entity) {
        entity=preUpdate(entity);
        U updateTo=getService().update(entity);
        updateTo=afterUpdate(updateTo);
        return updateTo;
    }

    /**
     * 执行一些 在 update 之后的一些操作
     *
     * @param savedTo 要存储的实体类
     * @return U
     */
    protected U afterUpdate(U savedTo) {
        return savedTo;
    }

    /**
     * 执行一些 在 update 之后的一些操作
     *
     * @param to 要存储的实体类
     * @return U
     */
    protected U preUpdate(U to) {
        return to;
    }

    /**
     * @return dto id , 不一定有
     */
    @SuppressWarnings("unchecked")
    public Optional<ID> getDtoId(T entityDto) {
        final Class<?> dtoClass=entityDto.getClass();
        final Field idField=ReflectionUtils.findField(dtoClass, "id");
        if (idField==null) {
            log.error("{} 无主键id列 ", dtoClass);
            return Optional.empty();
        }
        idField.setAccessible(true);
        final Object id=ReflectionUtils.getField(idField, entityDto);
        return id==null ? Optional.empty() : (Optional<ID>) Optional.of(id);
    }

    protected ResponseResult doResponse(Page<U> toList) {
        return ResponseResult.forSuccessBuilder()
                .withMessage("获取列表成功")
                .withData("items", getConvertMapper().toDtoList(toList.getContent()))
                .withData("total", toList.getTotalElements())
                .build();
    }

    protected ResponseResult doResponse(Optional<U> entity, String successMsg, String failMessage) {
        return entity.map(u->doResponse(u, successMsg))
                .orElseGet(()->ResponseResult.forFailureBuilder()
                        .withMessage(failMessage)
                        .build());
    }

    /**
     * 成功返回信息
     *
     * @param entity  entity
     * @param message 成功信息
     * @return response
     */
    protected ResponseResult doResponse(U entity, String message) {
        return ResponseResult.forSuccessBuilder()
                .withMessage(message)
                .withData("to", getConvertMapper().toDto(entity))
                .build();
    }

    protected ResponseResult doResponse(String message) {
        return ResponseResult.forSuccessBuilder()
                .withMessage(message)
                .build();
    }

    /**
     * get service
     *
     * @return baseService
     */
    public abstract BaseService<U, ID> getService();

    /**
     * 获取 dto -> to 之间的转化类
     *
     * @return mapper
     */
    public abstract AbstractMapper<U, T> getConvertMapper();
}
