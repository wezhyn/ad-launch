package com.ad.admain.common;

/**
 * to ，dto层均继承此方法，返回一个Id
 *
 * @author ZLB_KAM
 * @date 2019/9/27
 */
public interface IBaseTo<ID> {

    /**
     * 获取对应的主键
     *
     * @return id
     */
    ID getId();


}
