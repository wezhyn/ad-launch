package com.ad.admain.screen.vo.resp;

import com.ad.admain.screen.vo.FrameType;
import com.ad.admain.screen.vo.IScreenFrame;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

/**
 * @author wezhyn
 * @since 02.19.2020
 */
@Component
public class IpScreenResponse extends AbstractScreenResponse {

    private InetSocketAddress inetAddress;

    public IpScreenResponse(IScreenFrame request, InetSocketAddress address) {
        super(request, FrameType.IP);
        this.inetAddress=address;
    }

    @Override
    public String netData() {
        StringBuilder sb=new StringBuilder();
        sb.append("\"")
                .append(inetAddress.getHostString())
                .append("\"")
                .append(",")
                .append("\"")
                .append(inetAddress.getPort())
                .append("\"");
        return sb.toString();
    }

}
