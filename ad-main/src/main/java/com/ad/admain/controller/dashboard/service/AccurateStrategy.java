package com.ad.admain.controller.dashboard.service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 用于判断某个时间是否是精确查询
 *
 * @author wezhyn
 * @since 02.21.2020
 */
public interface AccurateStrategy {


    AccurateStrategy ACCURATE_INSTANCE=new CalculateAccurate();

    /**
     * 判断要处理的时间是否是精确的
     *
     * @param handleTime 处理时间
     * @return true: 精确
     */
    boolean isAccurate(LocalDateTime handleTime);


    /**
     * 手动计算要处理的时间是否是精确的
     */
    class CalculateAccurate implements AccurateStrategy {

        @Override
        public boolean isAccurate(LocalDateTime handleTime) {

            LocalDateTime nowDate=LocalDateTime.now();
            LocalDateTime minTime=LocalDateTime.of(nowDate.toLocalDate().minusDays(1), LocalTime.of(nowDate.getHour(), 0));
            LocalDateTime maxTime=LocalDateTime.now();
            if (handleTime.isAfter(maxTime)) {
                return false;
            }
            return minTime.isAfter(handleTime);
        }

    }


    class MysqlCalculateAccurate implements AccurateStrategy {

        private EntityManager entityManager;

        public MysqlCalculateAccurate(EntityManager entityManager) {
            this.entityManager=entityManager;

        }

        @Override
        public boolean isAccurate(LocalDateTime handleTime) {
            LocalDateTime time=handleTime.withMinute(0);
            final Query nativeQuery=entityManager.createNativeQuery("select accurate from ad_bill_aggregation where record_time =?1 ");
            nativeQuery.setParameter(1, time);
            return (boolean) nativeQuery.getSingleResult();
        }
    }

}
