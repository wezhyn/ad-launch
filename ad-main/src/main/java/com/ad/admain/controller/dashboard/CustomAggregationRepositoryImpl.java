package com.ad.admain.controller.dashboard;

import org.hibernate.query.NativeQuery;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author wezhyn
 * @since 01.24.2020
 */
public class CustomAggregationRepositoryImpl implements CustomAggregationRepository {


    @PersistenceContext
    private EntityManager em;

    @SuppressWarnings("unchecked")
    @Override
    public Optional<BillAggregation> findLast(DateType type, LocalDateTime queryTime) {
        final NativeQuery<BillAggregation> nativeQuery=(NativeQuery<BillAggregation>) em.createNativeQuery("select * from ad_bill_aggregation b where b.bill_scope=?1 and b.record_time < ?2 order by b.id desc limit 1", BillAggregation.class);
        nativeQuery.setParameter(1, type.getValue())
                .setParameter(2, queryTime);
        final BillAggregation result=nativeQuery.getResultList().get(0);
        return Optional.of(result);
    }
}
