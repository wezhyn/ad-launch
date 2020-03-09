package com.ad.admain.screen;

import com.ad.admain.screen.entity.Completion;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @ClassName CompletionRepository
 * @Description
 * @Author ZLB_KAM
 * @Date 2020/3/8 20:48
 * @Version V1.0
 **/
public interface CompletionRepository extends JpaRepository<Completion,Integer> {
    Completion findCompletionByAdOrderIdAndGenericUserId(Integer orderId,Integer uid);
}
