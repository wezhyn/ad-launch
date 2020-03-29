package screen;

import com.ad.screen.server.ScreenApplication;
import com.ad.screen.server.handler.GpsMsgMsgHandler;
import com.ad.screen.server.handler.HeartBeatMsgMsgHandler;
import com.ad.screen.server.handler.ScreenProtocolCheckInboundHandler;
import com.ad.screen.server.handler.ScreenProtocolOutEncoder;
import com.ad.screen.server.vo.req.BaseScreenRequest;
import com.ad.screen.server.vo.resp.AdScreenResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.charset.StandardCharsets;

@Slf4j
@SpringBootTest(classes = ScreenApplication.class)
@RunWith(SpringRunner.class)
public class ScreenChannelInitializerTest {
    private final byte[] str1="SOF0058,863987031739406,3,12000.84339,E,3013.18313,N,EOF\r\n".getBytes();
    @Autowired
    GpsMsgMsgHandler gpsMsgHandler;

    @Autowired
    HeartBeatMsgMsgHandler heartBeatMsgHandler;

    @Test
    public void printMsg() {
        EmbeddedChannel channel=new EmbeddedChannel(
                new LineBasedFrameDecoder(60, true, true),
                new ScreenProtocolOutEncoder(),
                new ScreenProtocolCheckInboundHandler(27, 3, 4),
                heartBeatMsgHandler,
                gpsMsgHandler
        );
//        final ByteBuf buffer=Unpooled.copiedBuffer(str1);
//        派生缓冲区，具有自己的读写索引，在在底层数据上共享
//        ByteBuf input=buffer.duplicate();
//        channel.writeInbound(input);
//        final BaseScreenRequest result=channel.readInbound();
//        BaseScreenRequest origin=BaseScreenRequest.builder()
//                .equipmentName("863987031739406")
//                .frameType(FrameType.GPS)
//                .netData(new Point2D(12000.84339, 3013.18313)).build();
//        assertNotEquals(origin, result);

        BaseScreenRequest request=BaseScreenRequest.builder()
                .equipmentName("863987031739406").build();
        AdScreenResponse data=AdScreenResponse.builder()
                .entryId(1)
                .repeatNum(100)
                .verticalView(false)
                .imei(request.getEquipmentName())
                .viewLength((byte) 12)
                .view("ZUST显示设备").build();

        channel.writeAndFlush(data);
        final ByteBuf objectStr=channel.readOutbound();
        final CharSequence charSequence=objectStr.readCharSequence(objectStr.readableBytes(), StandardCharsets.US_ASCII);
        String expectStr="SOF0084,863987031739406,3,1,0100,1,012,5A555354CFD4CABEC9E8B1B8,20200211213040,EOF\r\n";
        Assert.assertEquals(expectStr, charSequence.toString());

    }
}