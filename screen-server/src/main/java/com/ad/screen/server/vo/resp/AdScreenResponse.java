package com.ad.screen.server.vo.resp;


import com.ad.launch.order.AdStringUtils;
import com.ad.screen.server.vo.FrameType;
import com.ad.screen.server.vo.IScreenFrame;

/**
 * @author wezhyn
 * @since 02.19.2020
 */

public class AdScreenResponse extends AbstractScreenResponse {


    /**
     * 条目编号,相对于每个设备而言的唯一标识
     */
    private Integer entryId;

    /**
     * 该条目广告播放多少次
     */
    private Integer repeatNum;

    /**
     * 是否垂直播放：true：上下滚动 返回2  false，左右滚动，返回1
     */
    private boolean verticalView;

    /**
     * 显示字符长度（3个字节，表示显示数据字节数，最大112)
     * 每个汉字为两个字节，其它字母及数字为一个字节
     * UTF-8 编码？
     */
    private byte viewLength;

    private String view;


    public AdScreenResponse(IScreenFrame request, Integer entryId, Integer repeatNum, boolean verticalView, byte viewLength, String view) {
        super(request, FrameType.AD);
        this.entryId=entryId;
        this.repeatNum=repeatNum;
        this.verticalView=verticalView;
        this.viewLength=viewLength;
        this.view=view;
    }

    public static AdScreenResponseBuilder builder() {
        return new AdScreenResponseBuilder();
    }


    @Override
    public String netData() {
        StringBuilder sb=new StringBuilder();
        sb.append(entryId)
                .append(",")
                .append(getRepeatNum())
                .append(",")
                .append(getViewMode())
                .append(",")
                .append(getViewLength())
                .append(",")
                .append(AdStringUtils.gb2312Code(view));
        return sb.toString();

    }

    private String getViewLength() {
        StringBuilder s=new StringBuilder(String.valueOf(viewLength));
        for (int i=0; i < 3 - s.length(); i++) {
            s.insert(0, "0");
        }
        return s.toString();
    }

    private String getViewMode() {
        if (verticalView) {
            return "2";
        } else {
            return "1";
        }
    }

    private String getRepeatNum() {

        StringBuilder s=new StringBuilder(String.valueOf(repeatNum));
        for (int i=0; i < 4 - s.length(); i++) {
            s.insert(0, "0");
        }
        return s.toString();
    }

    public static final class AdScreenResponseBuilder {
        private IScreenFrame request;
        private Integer entryId;
        private Integer repeatNum;
        private boolean verticalView;
        private byte viewLength;
        private String view;

        private AdScreenResponseBuilder() {
        }

        public static AdScreenResponseBuilder anAdScreenResponse() {
            return new AdScreenResponseBuilder();
        }

        public AdScreenResponseBuilder request(IScreenFrame request) {
            this.request=request;
            return this;
        }

        public AdScreenResponseBuilder entryId(Integer entryId) {
            this.entryId=entryId;
            return this;
        }

        public AdScreenResponseBuilder repeatNum(Integer repeatNum) {
            this.repeatNum=repeatNum;
            return this;
        }

        public AdScreenResponseBuilder verticalView(boolean verticalView) {
            this.verticalView=verticalView;
            return this;
        }

        public AdScreenResponseBuilder viewLength(byte viewLength) {
            this.viewLength=viewLength;
            return this;
        }

        public AdScreenResponseBuilder view(String view) {
            this.view=view;
            return this;
        }

        public AdScreenResponse build() {
            return new AdScreenResponse(request, entryId, repeatNum, verticalView, viewLength, view);
        }
    }
}
