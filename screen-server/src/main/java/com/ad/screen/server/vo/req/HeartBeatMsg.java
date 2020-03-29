package com.ad.screen.server.vo.req;

import com.ad.screen.server.vo.FrameType;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * @ClassName HeartBeatMsg
 * @Description TODO
 * @Param
 * @Author ZLB
 * @Date 2020/2/21 19:31
 * @Version 1.0
 */
@Getter
@EqualsAndHashCode(callSuper=false)
public class HeartBeatMsg extends BaseScreenRequest<Void> {
    public HeartBeatMsg(String equipmentName) {
        super(equipmentName, FrameType.HEART_BEAT, null);
    }
}
