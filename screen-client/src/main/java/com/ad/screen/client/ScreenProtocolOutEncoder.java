package com.ad.screen.client;

import com.ad.screen.client.vo.IScreenFrame;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.time.format.DateTimeFormatter;

/**
 * @author wezhyn
 * @since 02.19.2020
 */
@Slf4j
public class ScreenProtocolOutEncoder extends MessageToByteEncoder<IScreenFrame> {


    private final static int BASE_NUM=32;
    private final static DateTimeFormatter DATA_TIME_FORMATTER=DateTimeFormatter.ofPattern("YYYYMMddHHmmss");

    @Override
    protected void encode(ChannelHandlerContext ctx, IScreenFrame msg, ByteBuf out) throws Exception {
        StringBuilder sb=new StringBuilder();
        sb.append("SOF")
                .append(getResponseNum(msg))
                .append(",")
                .append(msg.equipmentImei())
                .append(",")
                .append(msg.type())
                .append(",")
                .append(msg.netData())
                .append(",")
                .append("EOF\r\n");
        String result=sb.toString();
        log.warn("输出{}帧：{}", msg.type(), result);
        out.writeBytes(result.getBytes());
        ctx.flush();
    }

    private String getResponseNum(IScreenFrame msg) throws UnsupportedEncodingException {
        return AdStringUtils.getNum((msg.netData().getBytes("GBK").length + BASE_NUM), 4);
    }


}
