package com.ad.admain.screen.vo;

import com.ad.admain.screen.codec.ScreenProtocolInDecoder;
import com.ad.admain.screen.vo.req.BaseScreenRequest;
import io.netty.buffer.ByteBuf;
import javafx.geometry.Point2D;
import lombok.Getter;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.stream.Stream;

import static com.ad.admain.screen.codec.ScreenProtocolInDecoder.END_FIELD;

/**
 * @author wezhyn
 * @since 02.19.2020
 */
public enum FrameType {

    /**
     * 帧类型
     */
    HEART_BEAT(2), CONFIRM(1), GPS(3) {
        @Override
        @SuppressWarnings("unchecked")
        public <U, T extends BaseScreenRequest<U>> void netData(ByteBuf msg, T request) throws Exception {
            final int readableLength=msg.readableBytes() - END_FIELD.readableBytes();
            String gpsString=msg.readCharSequence(readableLength - 1, StandardCharsets.US_ASCII).toString();
            final String[] gpsSplit=gpsString.split(",");
            if (gpsSplit.length!=4) {
                throw new ScreenProtocolInDecoder.ParserException();
            }
            final double[] gpsDouble=new double[gpsSplit.length];
            for (int i=0; i < gpsSplit.length; i++) {
                switch (gpsSplit[i]) {
                    case "N":
                    case "E": {
                        gpsDouble[i]=1;
                        break;
                    }
                    case "W":
                    case "S": {
                        gpsDouble[i]=-1;
                        break;
                    }
                    default: {
                        gpsDouble[i]=Double.parseDouble(gpsSplit[i]);
                    }
                }
            }
            U data=(U) new Point2D(gpsDouble[0]*gpsDouble[1], gpsDouble[2]*gpsDouble[3]);
            request.setNetData(data);
        }
    }, COMPLETE_NOTIFICATION(4) {
        @Override
        @SuppressWarnings("unchecked")
        public <U, T extends BaseScreenRequest<U>> void netData(ByteBuf msg, T request) throws Exception {
            U data=(U) msg.readCharSequence(4, StandardCharsets.US_ASCII).toString();
            request.setNetData(data);
        }
    }, IP(2), AD(3);


    @Getter
    private int type;

    FrameType(int i) {
        type=i;
    }

    public static FrameType parse(char index) {
        return Stream.of(FrameType.values())
                .filter(f->f!=IP && f!=AD)
                .filter(f->Objects.equals(((char) (f.getType() + 48)), index))
                .findFirst()
                .orElseThrow(()->new RuntimeException("无法解析的数据类型"));
    }

    public <U, T extends BaseScreenRequest<U>> void netData(ByteBuf msg, T request) throws Exception {
    }


}
