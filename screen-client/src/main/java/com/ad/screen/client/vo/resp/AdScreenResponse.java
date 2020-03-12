package com.ad.screen.client.vo.resp;


import com.ad.screen.client.AdStringUtils;
import com.ad.screen.client.vo.IScreenFrame;

/**
 * @author wezhyn
 * @since 02.19.2020
 */

public class AdScreenResponse extends AbstractScreenResponse<AdEntry> {


    public AdScreenResponse(AdEntry entry) {
        super();
        setNetData(entry);
    }

    public static AdScreenResponseBuilder createInstance() {
        return new AdScreenResponseBuilder();
    }


    @Override
    public String netData() {
        StringBuilder sb=new StringBuilder();
        sb.append(getNetData().getEntryId())
                .append(",")
                .append(getRepeatNum())
                .append(",")
                .append(getViewMode())
                .append(",")
                .append(getViewLength())
                .append(",")
                .append(AdStringUtils.gb2312Code(getNetData().getView()));
        return sb.toString();

    }

    private String getViewLength() {
        StringBuilder s=new StringBuilder(String.valueOf(getNetData().getViewLength()));
        for (int i=0; i < 3 - s.length(); i++) {
            s.insert(0, "0");
        }
        return s.toString();
    }

    private String getViewMode() {
        if (getNetData().isVerticalView()) {
            return "2";
        } else {
            return "1";
        }
    }

    private String getRepeatNum() {

        StringBuilder s=new StringBuilder(String.valueOf(getNetData().getRepeatNum()));
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
            return new AdScreenResponse(new AdEntry(entryId, repeatNum, verticalView, viewLength, view));
        }
    }
}
