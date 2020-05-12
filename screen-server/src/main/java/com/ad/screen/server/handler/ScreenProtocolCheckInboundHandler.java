package com.ad.screen.server.handler;

import com.ad.screen.server.ChannelFirstReadEvent;
import com.ad.screen.server.cache.PooledIdAndEquipCache;
import com.ad.screen.server.cache.PooledIdAndEquipCacheService;
import com.ad.screen.server.server.ScreenChannelInitializer;
import com.ad.screen.server.vo.FrameType;
import com.ad.screen.server.vo.req.BaseScreenRequest;
import com.ad.screen.server.vo.req.ScreenRequestFactory;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author wezhyn
 * @since 02.19.2020
 */
@Slf4j
@ChannelHandler.Sharable
@Component
public class ScreenProtocolCheckInboundHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    private PooledIdAndEquipCacheService equipCacheService;

    /**
     * 帧末尾：EOF
     */
    public final static ByteBuf END_FIELD = Unpooled.copiedBuffer("EOF".getBytes());

    /**
     * 帧开头: SOF
     */
    private final static String BEGIN_FIELD = "SOF";


    /**
     * ,
     */
    private final static byte DELIMITER = ',';
    private final static int EQUIPMENT_LENGTH = 15;
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
        this.minLength = minLength;
        this.lengthFieldOffset = lengthFieldOffset;
        this.lengthFieldLength = lengthFieldLength;
        stripDelimiter = stripLineDelimiter;
    }

    public ScreenProtocolCheckInboundHandler(int minLength, int lengthFieldOffset, int lengthFieldLength) {
        this(minLength, lengthFieldOffset, lengthFieldLength, true);
    }

    public ScreenProtocolCheckInboundHandler() {
        this(27, 3, 4, true);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        BaseScreenRequest<?> request = null;
        try {
            ByteBuf inboundMsg = (ByteBuf) msg;
            inboundMsg.markReaderIndex();
            for (; ; ) {
                final int sof = findBeginOfLine(inboundMsg);
//              判断该信息是否大于等于最小进长度
                if (sof == -1 || inboundMsg.readableBytes() < sof + minLength) {
                    discardMsg(ctx, inboundMsg);
                    break;
                }
                int actualLengthField = sof + lengthFieldOffset;
                int frameLength = getFrameLength(inboundMsg, actualLengthField, lengthFieldLength);
//             从帧长度推算末尾符号，不匹配，则当前数据违规
                if (frameLength < minLength || !isEndOfLine(inboundMsg, sof, frameLength)) {
                    inboundMsg.skipBytes(sof + BEGIN_FIELD.length());
                    continue;
                }
//            解析数据
                try {
                    request = readRequest(inboundMsg, sof, frameLength);
                    if (request != null && !ctx.channel().attr(ScreenChannelInitializer.FIRST_READ_CHANNEL).get().get()) {
//                    第一次初始化
                        String iemi = request.getEquipmentName();
                        if (iemi.length() != EQUIPMENT_LENGTH) {
                            log.debug("error message: {}", inboundMsg.getCharSequence(sof, inboundMsg.readableBytes(), StandardCharsets.ISO_8859_1));
                            continue;
                        }
                        ctx.channel().attr(ScreenChannelInitializer.IEMI).set(iemi);
                        final PooledIdAndEquipCache equipCache = equipCacheService.getOrInit(request.getEquipmentName(), ctx.channel());
                        if (equipCache == null) {
                            log.error("无该认证设备：{}", iemi);
                        } else {
                            ctx.channel().attr(ScreenChannelInitializer.POOLED_EQUIP_CACHE).set(equipCache);
                            ctx.fireUserEventTriggered(ChannelFirstReadEvent.INSTANCE);
                        }
                    }
                    break;
                } catch (ParserException e) {
                    log.error("解析错误");
                    inboundMsg.skipBytes(sof + BEGIN_FIELD.length());
                }
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
        if (request != null) {
            ctx.fireChannelRead(request);
        }
    }

    private BaseScreenRequest<?> readRequest(ByteBuf msg, int start, int frameLength) throws ParserException {
        msg.skipBytes(start + lengthFieldOffset + lengthFieldLength);
        final String equipmentName = readRequestEquipmentName(msg);
        if (equipmentName.length() != EQUIPMENT_LENGTH) {
            return null;
        }
        final FrameType frameType = readType(msg);
        final Object netData = readNetData(msg, frameType);
        return ScreenRequestFactory.createRequest(equipmentName, frameType, netData);
    }

    private String readRequestEquipmentName(ByteBuf msg) throws ParserException {
        checkDelimiter(msg);
        final CharSequence charSequence = msg.readCharSequence(15, StandardCharsets.US_ASCII);
        return charSequence.toString();
    }

    private FrameType readType(ByteBuf msg) throws ParserException {
        checkDelimiter(msg);
        final byte bType = msg.readByte();
        return FrameType.parse((char) bType);
    }

    private Object readNetData(ByteBuf msg, FrameType type) throws ParserException {
        checkDelimiter(msg);
        try {
            Object data = type.netData(msg);
            //            检验末尾 ','
            checkDelimiter(msg);
            return data;
        } catch (Exception e) {
            throw new ParserException(e);
        }
    }

    private void checkDelimiter(ByteBuf msg) throws ParserException {
        final byte delimiter = msg.readByte();
        if (!Objects.equals(delimiter, DELIMITER)) {
            throw new ParserException();
        }
    }


    private void discardMsg(ChannelHandlerContext ctx, ByteBuf msg) {
//        无，丢弃无效数据，并打印日志
        if (log.isDebugEnabled()) {
            msg.resetReaderIndex();
            log.debug("丢弃：{}", msg.toString(StandardCharsets.ISO_8859_1));
        }
    }

    private int getFrameLength(ByteBuf buf, int offset, int length) {
        byte[] lengthField = new byte[length];
        buf.getBytes(offset, lengthField);
        int frameLength = Integer.parseInt(new String(lengthField));
        if (stripDelimiter) {
            frameLength -= 2;
        }
        return frameLength;
    }

    private boolean isEndOfLine(ByteBuf msg, int start, long frameLength) {
        int endFlagLength = END_FIELD.readableBytes();
        int endFlagOffset = (int) (start + frameLength - endFlagLength);
        byte[] endBytes = new byte[endFlagLength];
        msg.getBytes(endFlagOffset, endBytes);
        return ByteBufUtil.equals(Unpooled.copiedBuffer(endBytes), END_FIELD);
    }

    private int findBeginOfLine(ByteBuf msg) {
        int attempts = msg.readableBytes() - BEGIN_FIELD.length() + 1;
        for (int i = msg.readerIndex(); i < attempts; i++) {
            if (BEGIN_FIELD.equals(msg.getCharSequence(i, BEGIN_FIELD.length(), StandardCharsets.ISO_8859_1).toString())) {
                return i;
            }
        }
        return -1;
    }


    public static class ParserException extends Exception {
        public ParserException() {
        }

        public ParserException(Throwable cause) {
            super(cause);
        }
    }


}
