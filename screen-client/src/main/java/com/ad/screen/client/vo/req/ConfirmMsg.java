package com.ad.screen.client.vo.req;

import com.ad.screen.client.vo.FrameType;
import lombok.Data;

/**
 * @ClassName ConfirmMsg
 * @Description TODO
 * @Param
 * @Author ZLB
 * @Date 2020/2/21 19:31
 * @Version 1.0
 */
@Data
public class ConfirmMsg extends BaseScreenRequest<Void> {
    public ConfirmMsg(String equipmentName) {
        super(equipmentName, FrameType.CONFIRM, null);
    }

    public ConfirmMsg() {
    }
}
