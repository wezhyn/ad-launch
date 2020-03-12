package com.ad.screen.client;

import com.ad.screen.client.vo.IScreenFrame;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.UnsupportedEncodingException;
import java.time.format.DateTimeFormatter;

/**
 * @author wezhyn
 * @since 02.19.2020
 */
public class ScreenProtocolOutEncoder extends MessageToByteEncoder<IScreenFrame> {


    private final static int BASE_NUM=47;
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
        out.writeBytes(sb.toString().getBytes());
    }

    private String getResponseNum(IScreenFrame msg) throws UnsupportedEncodingException {
        return AdStringUtils.getNum((msg.netData().getBytes("GBK").length + BASE_NUM), 4);
    }


}
