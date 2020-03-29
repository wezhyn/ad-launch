package com.ad.screen.server.vo;

import com.ad.screen.server.handler.ScreenProtocolCheckInboundHandler;
import com.ad.screen.server.vo.req.Point2D;
import io.netty.buffer.ByteBuf;
import lombok.Getter;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.stream.Stream;

import static com.ad.screen.server.handler.ScreenProtocolCheckInboundHandler.END_FIELD;

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
        public Object netData(ByteBuf msg) throws Exception {
            final int readableLength=msg.readableBytes() - END_FIELD.readableBytes();
            String gpsString=msg.readCharSequence(readableLength - 1, StandardCharsets.US_ASCII).toString();
            final String[] gpsSplit=gpsString.split(",");
            if (gpsSplit.length!=4) {
                throw new ScreenProtocolCheckInboundHandler.ParserException();
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
            return new Point2D(gpsDouble[0]*gpsDouble[1], gpsDouble[2]*gpsDouble[3]);
        }
    }, COMPLETE_NOTIFICATION(4) {
        @Override
        public Object netData(ByteBuf msg) throws Exception {
            return msg.readCharSequence(4, StandardCharsets.US_ASCII).toString();
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

    public Object netData(ByteBuf msgt) throws Exception {
        return null;
    }


}
