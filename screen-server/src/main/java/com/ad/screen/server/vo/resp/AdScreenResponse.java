package com.ad.screen.server.vo.resp;


import com.ad.launch.order.AdStringUtils;
import com.ad.screen.server.vo.FrameType;

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

    private byte viewLength;

    private String view;


    public AdScreenResponse(String imei, Integer entryId, Integer repeatNum, boolean verticalView, String view,byte viewLength) {
        super(imei, FrameType.AD);
        this.entryId=entryId;
        this.repeatNum=repeatNum;
        this.verticalView=verticalView;
        this.view=view;
        this.viewLength = viewLength;
    }

    public static AdScreenResponseBuilder builder() {
        return new AdScreenResponseBuilder();
    }


    @Override
    public String netData() {
        StringBuilder sb=new StringBuilder();
        final String code=AdStringUtils.gb2312Code(view);
        sb.append(entryId)
                .append(",")
                .append(getRepeatNum())
                .append(",")
                .append(getViewMode())
                .append(",")
                .append(code.length())
                .append(",")
                .append(AdStringUtils.gb2312Code(view));
        return sb.toString();

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
        private Integer entryId;
        private Integer repeatNum;
        private boolean verticalView;
        private String view;
        private String imei;
        private byte viewLength;
        private AdScreenResponseBuilder() {
        }

        public static AdScreenResponseBuilder anAdScreenResponse() {
            return new AdScreenResponseBuilder();
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


        public AdScreenResponseBuilder view(String view) {
            this.view=view;
            return this;
        }

        public AdScreenResponseBuilder imei(String imei){
            this.imei=imei;
            return this;
        }

        public AdScreenResponseBuilder viewLength(byte viewLength){
            this.viewLength = viewLength;
            return this;
        }

        public AdScreenResponse build() {
            return new AdScreenResponse(imei, entryId, repeatNum, verticalView, view, viewLength);
        }
    }
}
