package com.ad.screen.server.entity;

import com.ad.screen.server.mq.PrepareTaskMessage;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

/**
 * @ClassName FailTask
 * @Description 熄火导致失败的任务
 * @Author ZLB_KAM
 * @Date 2020/3/9 8:40
 * @Version V1.0
 **/
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name="fail_task")
public class FailTask implements PrepareTaskMessage {

    @EmbeddedId
    private TaskKey id;

    @Column(name="repeat_num")
    private Integer repeatNum;

    @Column(name="verticalView")
    boolean verticalView;
    @Column(name="rate")
    Integer rate;
    @Column(name="longitude")
    Double longitude;
    @Column(name="latitude")
    Double latitude;
    @Column(name="scope")
    Double scope;
    @Column(name="view")
    private String view;

    @Override
    public boolean equals(Object o) {
        if (this==o) return true;
        if (o==null || getClass()!=o.getClass()) return false;
        FailTask failTask=(FailTask) o;
        return verticalView==failTask.verticalView &&
                id.equals(failTask.id) &&
                repeatNum.equals(failTask.repeatNum) &&
                view.equals(failTask.view);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, repeatNum, view, verticalView);
    }

    @Override
    public Integer getDeliverNum() {
        return 1;
    }
}

