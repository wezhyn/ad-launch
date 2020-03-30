package com.ad.screen.server.entity;

import com.wezhyn.project.IBaseTo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * @author wezhyn
 * @since 03.27.2020
 */
@Entity
@Data
public class ResumeRecord implements IBaseTo<String> {

    @Id
    @Column(name="word_identity", columnDefinition="varchar(20) not null")
    private String wordIdentity;

    /**
     * 记录点：记录点之前的id都是已经正常完成的
     */
    private Integer lastResumeId;

    @Column(name="record_time", columnDefinition="timestamp  null  default current_timestamp  ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime recordTime;


    public ResumeRecord() {
    }


    @Override
    public String getId() {
        return wordIdentity;
    }
}
