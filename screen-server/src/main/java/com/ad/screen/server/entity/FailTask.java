package com.ad.screen.server.entity;

import com.ad.screen.server.mq.TaskKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @ClassName FailTask
 * @Description 熄火导致失败的任务
 * @Author ZLB_KAM
 * @Date 2020/3/9 8:40
 * @Version V1.0
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="fail_task")
public class FailTask {

    @EmbeddedId
    private TaskKey taskKey;

    @Column(name="repeat_num")
    private Integer repeatNum;

    @Column(name = "view")
    private String view;

    @Column(name = "verticalView")
    boolean verticalView;

}

