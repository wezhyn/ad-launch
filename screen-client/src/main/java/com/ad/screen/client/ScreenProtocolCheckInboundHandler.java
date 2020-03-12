package com.ad.screen.client;

import com.ad.screen.client.vo.FrameType;
import com.ad.screen.client.vo.req.BaseScreenRequest;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author wezhyn
 * @since 02.19.2020
 */
@Slf4j
@ChannelHandler.Sharable
public class ScreenProtocolCheckInboundHandler extends ChannelInboundHandlerAdapter {


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

    public ScreenProtocolCheckInboundHandler(int minLength, int lengthFieldOffset, int lengthFieldLength, boolean stripLineDelimiter) {
        this.minLength=minLength;
        this.lengthFieldOffset=lengthFieldOffset;
        this.lengthFieldLength=lengthFieldLength;
        stripDelimiter=stripLineDelimiter;
    }

    public ScreenProtocolCheckInboundHandler(int minLength, int lengthFieldOffset, int lengthFieldLength) {
        this(minLength, lengthFieldOffset, lengthFieldLength, true);
    }

    public ScreenProtocolCheckInboundHandler() {
        this(27, 3, 4, true);
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf inboundMsg=(ByteBuf) msg;
        BaseScreenRequest<?> request=null;
        for (; ; ) {
            inboundMsg.markReaderIndex();
            final int sof=findBeginOfLine(inboundMsg);
//              判断该信息是否大于等于最小进长度
            if (inboundMsg.readableBytes() < sof + minLength) {
                discardMsg(ctx, inboundMsg);
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
                break;
            } catch (ParserException e) {
                log.error("解析错误", e);
                inboundMsg.resetReaderIndex();
                inboundMsg.skipBytes(sof + BEGIN_FIELD.readableBytes());
            }
        }
        ctx.fireChannelRead(request);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {


    }

    @SuppressWarnings("unchecked")
    private <T extends BaseScreenRequest<?>> T readRequest(ByteBuf msg, int start, int frameLength) throws ParserException {
        msg.skipBytes(start + lengthFieldOffset + lengthFieldLength);
        String equipmentName=readRequestEquipmentName(msg);
        FrameType type=readType(msg);
        Object data=readNetData(msg, type);
        final BaseScreenRequest<?> baseScreenRequest=type.generateRequest(data);
        baseScreenRequest.setEquipmentName(equipmentName);
        return (T) baseScreenRequest;

    }


    private String readRequestEquipmentName(ByteBuf msg) throws ParserException {
        checkDelimiter(msg);
        final CharSequence charSequence=msg.readCharSequence(15, StandardCharsets.US_ASCII);
        return charSequence.toString();
    }

    private FrameType readType(ByteBuf msg) throws ParserException {
        checkDelimiter(msg);
        final byte bType=msg.readByte();
        return (FrameType.parseServer((char) bType));
    }

    private Object readNetData(ByteBuf msg, FrameType type) throws ParserException {
        checkDelimiter(msg);
        try {
            Object u=type.netData(msg);
            //            检验末尾 ','
            checkDelimiter(msg);
            return u;
        } catch (Exception e) {
            throw new ParserException(e);
        }
    }

    private void checkDelimiter(ByteBuf msg) throws ParserException {
        final byte delimiter=msg.readByte();
        if (!Objects.equals(delimiter, DELIMITER)) {
            throw new ParserException();
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
