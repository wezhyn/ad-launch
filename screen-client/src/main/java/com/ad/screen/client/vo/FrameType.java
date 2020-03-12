package com.ad.screen.client.vo;

import com.ad.screen.client.AdStringUtils;
import com.ad.screen.client.ScreenProtocolCheckInboundHandler;
import com.ad.screen.client.vo.req.BaseScreenRequest;
import com.ad.screen.client.vo.resp.AdEntry;
import io.netty.buffer.ByteBuf;
import javafx.geometry.Point2D;
import lombok.Getter;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.stream.Stream;

import static com.ad.screen.client.ScreenProtocolCheckInboundHandler.END_FIELD;


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
    }, IP(2), AD(3) {
        @Override
        @SuppressWarnings("unchecked")
        public <U, T extends BaseScreenRequest<U>> void netData(ByteBuf msg, T request) throws Exception {
            final int readableLength=msg.readableBytes() - END_FIELD.readableBytes();
            String adString=msg.readCharSequence(readableLength - 1, StandardCharsets.US_ASCII).toString();
            final String[] adSplits=adString.split(",");
            if (adSplits.length!=5) {
                throw new ScreenProtocolCheckInboundHandler.ParserException();
            }
            U entry=(U) new AdEntry.AdEntryBuilder()
                    .entryId(Integer.parseInt(adSplits[0]))
                    .repeatNum(AdStringUtils.parseNum(adSplits[1]))
                    .verticalView(Integer.parseInt(adSplits[2])!=1)
                    .viewLength((byte) AdStringUtils.parseNum(adSplits[3]))
                    .view(AdStringUtils.code2String(adSplits[4]))
                    .build();
            request.setNetData(entry);
        }
    };


    @Getter
    private int type;

    FrameType(int i) {
        type=i;
    }

    public static FrameType parseClient(char index) {
        return Stream.of(FrameType.values())
                .filter(f->f!=IP && f!=AD)
                .filter(f->Objects.equals(((char) (f.getType() + 48)), index))
                .findFirst()
                .orElseThrow(()->new RuntimeException("无法解析的数据类型"));
    }

    public static FrameType parseServer(char index) {
        return Stream.of(FrameType.values())
                .filter(f->f==IP || f==AD || f==CONFIRM)
                .filter(f->Objects.equals(((char) (f.getType() + 48)), index))
                .findFirst()
                .orElseThrow(()->new RuntimeException("无法解析的数据类型"));
    }


    public <U, T extends BaseScreenRequest<U>> void netData(ByteBuf msg, T request) throws Exception {
    }


}
