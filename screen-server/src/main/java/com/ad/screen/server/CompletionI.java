package com.ad.screen.server;

import com.ad.screen.server.entity.Completion;

/**
 * @ClassName CompletionI
 * @Description
 * @Author ZLB_KAM
 * @Date 2020/3/8 20:47
 * @Version V1.0
 **/
public interface CompletionI {
    Completion save(Completion completion);

    Completion findByOidAndUid(Integer oid, Integer uid);
}
