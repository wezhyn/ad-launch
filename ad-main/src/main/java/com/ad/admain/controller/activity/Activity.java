package com.ad.admain.controller.activity;

import com.ad.admain.controller.account.entity.Admin;
import com.wezhyn.project.IBaseTo;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author wezhyn
 * @since 12.31.2019
 */
@Entity(name="ad_activity")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
public class Activity implements IBaseTo<Integer> {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column(insertable=false, updatable=false)
    @org.hibernate.annotations.Generated(
            value=GenerationTime.INSERT
    )
    @ColumnDefault("current_timestamp")
    private LocalDateTime publishTime;

    private String content;

    private String title;


    @Column(name="a_id")
    private Integer aid;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="a_id", insertable=false, updatable=false)
    private Admin admin;

    /**
     *
     */
    @ColumnDefault("b'1'")
    private Boolean publish;


}
