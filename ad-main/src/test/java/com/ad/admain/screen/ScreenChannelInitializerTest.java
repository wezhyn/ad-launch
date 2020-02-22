package com.ad.admain.screen;

import com.ad.admain.screen.codec.ScreenProtocolInDecoder;
import com.ad.admain.screen.codec.ScreenProtocolOutEncoder;
import com.ad.admain.screen.handler.GpsMsgHandler;
import com.ad.admain.screen.handler.HeartBeatMsgHandler;
import com.ad.admain.screen.handler.ScreenProtocolCheckInboundHandler;
import com.ad.admain.screen.handler.TypeHandler;
import com.ad.admain.screen.vo.FrameType;
import com.ad.admain.screen.vo.req.ScreenRequest;
import com.ad.admain.screen.vo.resp.AdScreenResponse;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import javafx.geometry.Point2D;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;
@Slf4j
@SpringBootTest
@ComponentScan("com.ad")
@RunWith(SpringRunner.class)
public class ScreenChannelInitializerTest {
    private final byte[] str1="SOF0058,863987031739406,3,12000.84339,E,3013.18313,N,EOF\r\n".getBytes();
@Autowired
GpsMsgHandler gpsMsgHandler;
@Autowired
TypeHandler typeHandler;
@Autowired
HeartBeatMsgHandler heartBeatMsgHandler;

    @Test
    public void printMsg() {
        EmbeddedChannel channel=new EmbeddedChannel(
                new LineBasedFrameDecoder(60, true, true),
                new ScreenProtocolOutEncoder(),
                new ScreenProtocolCheckInboundHandler(27, 3, 4),
                typeHandler,
                heartBeatMsgHandler,
                gpsMsgHandler
        );
        final ByteBuf buffer=Unpooled.copiedBuffer(str1);
//        派生缓冲区，具有自己的读写索引，在在底层数据上共享
        ByteBuf input=buffer.duplicate();
        channel.writeInbound(input);
        final ScreenRequest result=channel.readInbound();
        ScreenRequest origin=ScreenRequest.builder()
                .equipmentName("863987031739406")
                .frameType(FrameType.GPS)
                .netData(new Point2D(12000.84339, 3013.18313)).build();
        assertNotEquals(origin, result);

        ScreenRequest request=ScreenRequest.builder()
                .equipmentName("863987031739406").build();
        AdScreenResponse data=AdScreenResponse.builder()
                .entryId(1)
                .repeatNum(100)
                .verticalView(false)
                .request(request)
                .viewLength((byte) 12)
                .view("ZUST显示设备").build();

        channel.writeOutbound(data);
        final ByteBuf objectStr=channel.readOutbound();
        final CharSequence charSequence=objectStr.readCharSequence(objectStr.readableBytes(), StandardCharsets.US_ASCII);
        String expectStr="SOF0084,863987031739406,3,1,0100,1,012,5A555354CFD4CABEC9E8B1B8,20200211213040,EOF\r\n";
        Assert.assertEquals(expectStr, charSequence.toString());

}
}