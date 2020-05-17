package com.ad.admain.controller.account.user;

import com.wezhyn.project.annotation.StrategyEnum;
import com.wezhyn.project.annotation.UpdateIgnore;
import com.wezhyn.project.database.EnumType;
import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.Entity;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author wezhyn
 * @since 05.16.2020
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class SocialUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "uid", insertable = false, updatable = false)
    @UpdateIgnore
    private GenericUser orderUser;

    @UpdateIgnore
    private Integer uid;

    /**
     * 可用于查找该用户数据的账户
     */
    private String socialAccountId;

    @StrategyEnum(value = EnumType.NUMBER)
    @Type(type = "strategyEnum")
    private SocialType socialType;

    @Column(insertable = false, updatable = false)
    @org.hibernate.annotations.Generated(
            value = GenerationTime.ALWAYS
    )
    @ColumnDefault("current_timestamp")
    private LocalDateTime registerTime;


}
