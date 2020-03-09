package com.ad.admain.screen;

import com.ad.admain.screen.entity.Completion;
import org.springframework.stereotype.Service;

/**
 * @ClassName CompletionImpl
 * @Description
 * @Author ZLB_KAM
 * @Date 2020/3/8 20:49
 * @Version V1.0
 **/
@Service
public class CompletionImpl implements CompletionI {
    CompletionRepository completionRepository;

    public CompletionImpl(CompletionRepository completionRepository){
        this.completionRepository = completionRepository;
    }
    @Override
    public Completion save(Completion completion) {
        return completionRepository.save(completion);
    }

    @Override
    public Completion findByOidAndUid(Integer oid, Integer uid) {
        return completionRepository.findCompletionByAdOrderIdAndGenericUserId(oid,uid);
    }
}
