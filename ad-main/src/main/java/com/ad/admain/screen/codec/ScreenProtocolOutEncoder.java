package com.ad.admain.screen.codec;

import com.ad.admain.screen.vo.IScreenFrameServer;
import com.ad.admain.utils.AdStringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.time.format.DateTimeFormatter;

/**
 * @author wezhyn
 * @since 02.19.2020
 */
@Component
public class ScreenProtocolOutEncoder extends MessageToByteEncoder<IScreenFrameServer> {


    private final static int BASE_NUM=47;
    private final static DateTimeFormatter DATA_TIME_FORMATTER=DateTimeFormatter.ofPattern("YYYYMMddHHmmss");

    @Override
    protected void encode(ChannelHandlerContext ctx, IScreenFrameServer msg, ByteBuf out) throws Exception {
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
                .append(getServerTime(msg))
                .append(",")
                .append("EOF\r\n");
        out.writeBytes(sb.toString().getBytes());
    }

    private String getResponseNum(IScreenFrameServer msg) throws UnsupportedEncodingException {
        return AdStringUtils.getNum((msg.netData().getBytes("GBK").length + BASE_NUM), 4);
    }

    private String getServerTime(IScreenFrameServer msg) {
        return msg.serverTime().format(DATA_TIME_FORMATTER);
    }


}
