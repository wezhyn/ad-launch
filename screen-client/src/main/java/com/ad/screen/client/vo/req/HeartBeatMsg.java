package com.ad.screen.client.vo.req;

import com.ad.screen.client.vo.FrameType;
import lombok.Data;

/**
 * @ClassName HeartBeatMsg
 * @Description TODO
 * @Param
 * @Author ZLB
 * @Date 2020/2/21 19:31
 * @Version 1.0
 */
@Data
public class HeartBeatMsg extends BaseScreenRequest<Void> {

    public HeartBeatMsg(String equipmentName) {
        super(equipmentName, FrameType.HEART_BEAT, null);
    }

}
