package com.ad.admain.screen.codec;

import com.ad.admain.screen.handler.ScreenProtocolCheckInboundHandler;
import com.ad.admain.screen.vo.FrameType;
import com.ad.admain.screen.vo.req.ConfirmMsg;
import com.ad.admain.screen.vo.req.GpsMsg;
import com.ad.admain.screen.vo.req.HeartBeatMsg;
import com.ad.admain.screen.vo.req.ScreenRequest;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import javafx.geometry.Point2D;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName Decoder
 * @Description TODO
 * @Param
 * @Author ZLB
 * @Date 2020/2/21 17:35
 * @Version 1.0
 */
@Slf4j
public class ScreenProtocolInDecoder extends ByteToMessageDecoder {
    /**
     * 帧开头: SOF
     */
    private final static ByteBuf BEGIN_FIELD= Unpooled.copiedBuffer("SOF".getBytes());
    /**
     * 帧末尾：EOF
     */
    private final static ByteBuf END_FIELD=Unpooled.wrappedUnmodifiableBuffer(Unpooled.copiedBuffer("EOF".getBytes()));
    /**
     * ,
     */
    private final static byte DELIMITER=',';
    private final static int EQUIPMENT_LENGTH=15;
    /**
     * 客户端帧除净数据外的帧长度
     */

    private final int minLength;
    /**
     * 距离 SOF 的偏移量
     */
    private final int lengthFieldOffset;
    /**
     * 长度字段长度
     */
    private final int lengthFieldLength;

    /**
     * frameLength 是否包括 \r\n 长度
     */
    private final boolean stripDelimiter;

    public ScreenProtocolInDecoder(int minLength, int lengthFieldOffset, int lengthFieldLength, boolean stripLineDelimiter) {
        this.minLength=minLength;
        this.lengthFieldOffset=lengthFieldOffset;
        this.lengthFieldLength=lengthFieldLength;
        stripDelimiter=stripLineDelimiter;
    }

    public ScreenProtocolInDecoder(int minLength, int lengthFieldOffset, int lengthFieldLength) {
        this(minLength, lengthFieldOffset, lengthFieldLength, true);
    }
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        ByteBuf inboundMsg= byteBuf ;
        ScreenRequest request=null;
        for (; ; ) {
            inboundMsg.markReaderIndex();
            final int sof=findBeginOfLine(inboundMsg);
//              判断该信息是否大于等于最小进长度
            if (inboundMsg.readableBytes() < sof + minLength) {
                discardMsg(channelHandlerContext, inboundMsg);
            }
            int actualLengthField=sof + lengthFieldOffset;
            int frameLength=getFrameLength(inboundMsg, actualLengthField, lengthFieldLength);
            if (frameLength <= 0) {
                inboundMsg.skipBytes(sof + BEGIN_FIELD.readableBytes());
                continue;
            }
//             从帧长度推算末尾符号，不匹配，则当前数据违规
            if (!isEndOfLine(inboundMsg, sof, frameLength)) {
                inboundMsg.skipBytes(sof + BEGIN_FIELD.readableBytes());
                continue;
            }
//            解析数据
            try {
                request=readRequest(inboundMsg, sof, frameLength);
                list.add(request);
                break;
            } catch (ScreenProtocolCheckInboundHandler.ParserException e) {
                log.error("解析错误", e);
                inboundMsg.resetReaderIndex();
                inboundMsg.skipBytes(sof + BEGIN_FIELD.readableBytes());
            }
        }
    }


    private ScreenRequest readRequest(ByteBuf msg, int start, int frameLength) throws ScreenProtocolCheckInboundHandler.ParserException {
        final ScreenRequest request=new ScreenRequest();
        msg.skipBytes(start + lengthFieldOffset + lengthFieldLength);
        readRequestEquipmentName(msg, request);
        readType(msg, request);
        readNetData(msg, request);
        int type = request.type();
        switch (type){
            case 1: {
                HeartBeatMsg heartBeatMsg = new HeartBeatMsg();
                BeanUtils.copyProperties(request,heartBeatMsg);
                return heartBeatMsg;
            }
            case 2:{
                ConfirmMsg confirmMsg = new ConfirmMsg();
                BeanUtils.copyProperties(request,confirmMsg);
                return confirmMsg;
            }
            case 3:{
                GpsMsg gpsMsg = new GpsMsg();
                BeanUtils.copyProperties(request,gpsMsg);
                return gpsMsg;
            }
            default: {
                throw new ScreenProtocolCheckInboundHandler.ParserException();
            }

        }
    }

    private void readRequestEquipmentName(ByteBuf msg, ScreenRequest request) throws ScreenProtocolCheckInboundHandler.ParserException {
        checkDelimiter(msg);
        final CharSequence charSequence=msg.readCharSequence(15, StandardCharsets.US_ASCII);
        request.setEquipmentName(charSequence.toString());
    }

    private void readType(ByteBuf msg, ScreenRequest request) throws ScreenProtocolCheckInboundHandler.ParserException {
        checkDelimiter(msg);
        final byte bType=msg.readByte();
        switch (bType) {
            case '1': {
                request.setFrameType(FrameType.HEART_BEAT);
                break;
            }
            case '2': {
                request.setFrameType(FrameType.CONFIRM);
                break;
            }
            case '3': {
                request.setFrameType(FrameType.GPS);
                break;
            }
            default: {
                throw new ScreenProtocolCheckInboundHandler.ParserException();
            }
        }
    }

    private void readNetData(ByteBuf msg, ScreenRequest request) throws ScreenProtocolCheckInboundHandler.ParserException {
        checkDelimiter(msg);
        if (request.getFrameType()==FrameType.GPS) {
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
            request.setNetData(new Point2D(gpsDouble[0]*gpsDouble[1], gpsDouble[2]*gpsDouble[3]));
        }

        //            检验末尾 ','
        checkDelimiter(msg);
    }

    private void checkDelimiter(ByteBuf msg) throws ScreenProtocolCheckInboundHandler.ParserException {
        final byte delimiter=msg.readByte();
        if (!Objects.equals(delimiter, DELIMITER)) {
            throw new ScreenProtocolCheckInboundHandler.ParserException();
        }
    }


    private void discardMsg(ChannelHandlerContext ctx, ByteBuf msg) {
//        无，丢弃无效数据，并打印日志
        log.info(ByteBufUtil.hexDump(msg));
        msg.release();
    }

    private int getFrameLength(ByteBuf buf, int offset, int length) {
        byte[] lengthField=new byte[length];
        buf.getBytes(offset, lengthField);
        int frameLength=Integer.parseInt(new String(lengthField));
        if (stripDelimiter) {
            frameLength-=2;
        }
        return frameLength;
    }

    private boolean isEndOfLine(ByteBuf msg, int start, long frameLength) {
        int endFlagLength=END_FIELD.readableBytes();
        int endFlagOffset=(int) (start + frameLength - endFlagLength);
        byte[] endBytes=new byte[endFlagLength];
        msg.getBytes(endFlagOffset, endBytes);
        return ByteBufUtil.equals(Unpooled.copiedBuffer(endBytes), END_FIELD);
    }

    private int findBeginOfLine(ByteBuf msg) {
        return ByteBufUtil.indexOf(BEGIN_FIELD, msg);
    }


    private static class ParserException extends Exception {
        public ParserException() {
        }
    }
}
