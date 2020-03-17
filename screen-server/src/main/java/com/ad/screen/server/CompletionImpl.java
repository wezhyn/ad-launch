package com.ad.screen.server;

import com.ad.launch.order.RemoteAdOrderServiceI;
import com.ad.screen.server.entity.Completion;
import org.springframework.beans.factory.annotation.Autowired;
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
    public CompletionImpl(CompletionRepository completionRepository) {
        this.completionRepository=completionRepository;
    }

    @Override
    public Completion save(Completion completion) {
        return completionRepository.save(completion);
    }

    @Override
    public Completion findByOidAndUid(Integer oid, Integer uid) {
        return completionRepository.findCompletionByAdOrderIdAndUid(oid, uid);
    }
}
