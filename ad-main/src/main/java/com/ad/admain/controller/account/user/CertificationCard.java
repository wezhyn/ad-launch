package com.ad.admain.controller.account.user;

import com.wezhyn.project.annotation.UpdateIgnore;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author wezhyn
 * @since 05.16.2020
 */
@Entity
@DynamicUpdate
@DynamicInsert
@Data
public class CertificationCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "uid", insertable = false, updatable = false)
    @UpdateIgnore
    private GenericUser orderUser;

    @UpdateIgnore
    private Integer uid;


    @ColumnDefault("''")
    private String realName;
    @ColumnDefault("''")
    private String idCard;


    private String idCardPreImg;

    private String idCardAftImg;

    @Column(insertable = false, updatable = false)
    @org.hibernate.annotations.Generated(
            value = GenerationTime.INSERT
    )
    @ColumnDefault("current_timestamp")
    private LocalDateTime registerTime;
}
