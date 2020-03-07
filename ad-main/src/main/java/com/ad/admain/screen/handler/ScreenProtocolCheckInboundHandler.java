package com.ad.admain.screen.handler;

import com.ad.admain.cache.*;
import com.ad.admain.controller.equipment.EquipmentService;
import com.ad.admain.controller.equipment.entity.Equipment;
import com.ad.admain.screen.server.ScreenChannelInitializer;
import com.ad.admain.screen.vo.FrameType;
import com.ad.admain.screen.vo.req.ScreenRequest;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import javafx.geometry.Point2D;
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
@Component
public class ScreenProtocolCheckInboundHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    EquipmentService equipmentService;

    @Autowired
    EquipmentCacheService equipmentCacheService;

    @Autowired
    PooledIdAndEquipCacheService pooledIdAndEquipCacheService;
    /*
     * @Description //设备attr的key
     * @Date 2020/3/7 21:09
     * @param null
     *@return
     **/
    private final static AttributeKey<Equipment> EQUIPMENT = AttributeKey.valueOf("EQUIPMENT");

    /**
     * 帧开头: SOF
     */
    private final static ByteBuf BEGIN_FIELD=Unpooled.copiedBuffer("SOF".getBytes());
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
        this(27,3,4,true);
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf inboundMsg=(ByteBuf) msg;
        ScreenRequest request=null;
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
                String imei = request.getEquipmentName();
                Equipment equipment = equipmentCacheService.get(imei);
                //如果设备存在，则在channel中保存当前设备信息
                    if (equipment!=null) {
                        equipment.setStatus(true);
                        ctx.channel().attr(EQUIPMENT).set(equipment);
                        Long pooledId =  ctx.channel().attr(ScreenChannelInitializer.REGISTERED_ID).get();
                        PooledIdAndEquipCache pooledIdAndEquipCache = new PooledIdAndEquipCache(pooledId,equipment);
                        pooledIdAndEquipCacheService.getCache().put(imei,pooledIdAndEquipCache);
                    }
                break;

            } catch (ParserException e) {
                log.error("解析错误", e);
                inboundMsg.resetReaderIndex();
                inboundMsg.skipBytes(sof + BEGIN_FIELD.readableBytes());
            }
        }
        ctx.fireChannelRead(request);
    }

    private ScreenRequest readRequest(ByteBuf msg, int start, int frameLength) throws ParserException {
        final ScreenRequest request=new ScreenRequest();
        msg.skipBytes(start + lengthFieldOffset + lengthFieldLength);
        readRequestEquipmentName(msg, request);
        readType(msg, request);
        readNetData(msg, request);
        return request;
    }

    private void readRequestEquipmentName(ByteBuf msg, ScreenRequest request) throws ParserException {
        checkDelimiter(msg);
        final CharSequence charSequence=msg.readCharSequence(15, StandardCharsets.US_ASCII);
        request.setEquipmentName(charSequence.toString());
    }

    private void readType(ByteBuf msg, ScreenRequest request) throws ParserException {
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
                throw new ParserException();
            }
        }
    }

    private void readNetData(ByteBuf msg, ScreenRequest request) throws ParserException {
        checkDelimiter(msg);
        if (request.getFrameType()==FrameType.GPS) {
            final int readableLength=msg.readableBytes() - END_FIELD.readableBytes();
            String gpsString=msg.readCharSequence(readableLength - 1, StandardCharsets.US_ASCII).toString();
            final String[] gpsSplit=gpsString.split(",");
            if (gpsSplit.length!=4) {
                throw new ParserException();
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


    private static class ParserException extends Exception {
        public ParserException() {
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {


    }


}
