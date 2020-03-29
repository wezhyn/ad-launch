package com.ad.screen.server.vo.req;

import com.ad.screen.server.vo.FrameType;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * @ClassName ConfirmMsg
 * @Description TODO
 * @Param
 * @Author ZLB
 * @Date 2020/2/21 19:31
 * @Version 1.0
 */
@Getter
@EqualsAndHashCode(callSuper=false)
public class ConfirmMsg extends BaseScreenRequest<Void> {
    public ConfirmMsg(String equipmentName) {
        super(equipmentName, FrameType.CONFIRM, null);
    }
}
