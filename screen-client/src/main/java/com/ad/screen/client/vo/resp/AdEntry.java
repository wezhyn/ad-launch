package com.ad.screen.client.vo.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wezhyn
 * @since 03.12.2020
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdEntry {
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


    public static final class AdEntryBuilder {
        private Integer entryId;
        private Integer repeatNum;
        private boolean verticalView;
        private byte viewLength;
        private String view;

        public AdEntryBuilder() {
        }

        public static AdEntryBuilder anAdEntry() {
            return new AdEntryBuilder();
        }

        public AdEntryBuilder entryId(Integer entryId) {
            this.entryId=entryId;
            return this;
        }

        public AdEntryBuilder repeatNum(Integer repeatNum) {
            this.repeatNum=repeatNum;
            return this;
        }

        public AdEntryBuilder verticalView(boolean verticalView) {
            this.verticalView=verticalView;
            return this;
        }

        public AdEntryBuilder viewLength(byte viewLength) {
            this.viewLength=viewLength;
            return this;
        }

        public AdEntryBuilder view(String view) {
            this.view=view;
            return this;
        }

        public AdEntry build() {
            AdEntry adEntry=new AdEntry();
            adEntry.setEntryId(entryId);
            adEntry.setRepeatNum(repeatNum);
            adEntry.setVerticalView(verticalView);
            adEntry.setViewLength(viewLength);
            adEntry.setView(view);
            return adEntry;
        }
    }
}
