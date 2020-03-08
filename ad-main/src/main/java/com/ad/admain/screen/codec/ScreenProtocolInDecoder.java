package com.ad.admain.screen.codec;

import com.ad.admain.screen.vo.FrameType;
import com.ad.admain.screen.vo.req.BaseScreenRequest;
import com.ad.admain.screen.vo.req.ConfirmMsg;
import com.ad.admain.screen.vo.req.GpsMsg;
import com.ad.admain.screen.vo.req.HeartBeatMsg;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

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
    public final static ByteBuf BEGIN_FIELD=Unpooled.copiedBuffer("SOF".getBytes());
    /**
     * 帧末尾：EOF
     */
    public final static ByteBuf END_FIELD=Unpooled.wrappedUnmodifiableBuffer(Unpooled.copiedBuffer("EOF".getBytes()));
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
        ByteBuf inboundMsg=byteBuf;
        BaseScreenRequest request=null;
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
                channelHandlerContext.fireChannelRead(request);
                break;
            } catch (ScreenProtocolInDecoder.ParserException e) {
                log.error("解析错误", e);
                inboundMsg.resetReaderIndex();
                inboundMsg.skipBytes(sof + BEGIN_FIELD.readableBytes());
            }
        }
    }


    private BaseScreenRequest<?> readRequest(ByteBuf msg, int start, int frameLength) throws ParserException {
        final BaseScreenRequest<?> request=new BaseScreenRequest<>();
        msg.skipBytes(start + lengthFieldOffset + lengthFieldLength);
        readRequestEquipmentName(msg, request);
        readType(msg, request);
        readNetData(msg, request);
        int type=request.type();
        switch (type) {
            case 1: {
                HeartBeatMsg heartBeatMsg=new HeartBeatMsg();
                BeanUtils.copyProperties(request, heartBeatMsg);
                return heartBeatMsg;
            }
            case 2: {
                ConfirmMsg confirmMsg=new ConfirmMsg();
                BeanUtils.copyProperties(request, confirmMsg);
                return confirmMsg;
            }
            case 3: {
                GpsMsg gpsMsg=new GpsMsg();
                BeanUtils.copyProperties(request, gpsMsg);
                return gpsMsg;
            }
            default: {
                throw new ScreenProtocolInDecoder.ParserException();
            }

        }
    }

    private void readRequestEquipmentName(ByteBuf msg, BaseScreenRequest<?> request) throws ParserException {
        checkDelimiter(msg);
        final CharSequence charSequence=msg.readCharSequence(15, StandardCharsets.US_ASCII);
        request.setEquipmentName(charSequence.toString());
    }

    private void readType(ByteBuf msg, BaseScreenRequest<?> request) throws ParserException {
        checkDelimiter(msg);
        final byte bType=msg.readByte();
        try {
            request.setFrameType(FrameType.parse((char) bType));
        } catch (Exception e) {
            throw new ParserException(e);
        }
    }

    private void readNetData(ByteBuf msg, BaseScreenRequest<?> request) throws ParserException {
        checkDelimiter(msg);
        try {
            request.getFrameType().netData(msg, request);
        } catch (Exception e) {
            throw new ParserException(e);
        }
        //            检验末尾 ','
        checkDelimiter(msg);
    }

    private void checkDelimiter(ByteBuf msg) throws ParserException {
        final byte delimiter=msg.readByte();
        if (!Objects.equals(delimiter, DELIMITER)) {
            throw new ScreenProtocolInDecoder.ParserException();
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


    public static class ParserException extends Exception {
        public ParserException() {
        }

        public ParserException(Throwable cause) {
            super(cause);
        }
    }
}
