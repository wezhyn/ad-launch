package com.ad.admain.screen.vo.req;

import com.ad.admain.screen.vo.FrameType;
import com.ad.admain.screen.vo.IScreenFrame;
import javafx.geometry.Point2D;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author wezhyn
 * @since 02.19.2020
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ScreenRequest implements IScreenFrame {


    /**
     * IMEI
     */
    private String equipmentName;

    /**
     * 帧类型
     */
    private FrameType frameType;

    /**
     * 前一个数表示经度
     * 后一个数表示维度：+
     */
    private Point2D netData;

    @Override
    public int type() {
        return frameType.getType();
    }

    @Override
    public String equipmentImei() {
        return equipmentName;
    }

    @Override
    public String netData() {
        return "";
    }
}
