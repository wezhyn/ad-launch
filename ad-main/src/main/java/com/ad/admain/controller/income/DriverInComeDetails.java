package com.ad.admain.controller.income;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenerationTime;

/**
 * @author wezhyn
 * @since 05.17.2020
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DriverInComeDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer driverId;

    private Double amount;

    private Integer oid;

    @Column(insertable = false, updatable = false)
    @org.hibernate.annotations.Generated(
        value = GenerationTime.ALWAYS
    )
    @ColumnDefault("current_timestamp")
    private LocalDateTime recordTime;

    public DriverInComeDetails(Integer driverId, Double amount) {
        this.driverId = driverId;
        this.amount = amount;
    }
}
