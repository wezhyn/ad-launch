package com.ad.screen.server.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

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

    @ColumnDefault("current_timestamp")
    private LocalDateTime recordTime;

    public DriverInComeDetails(Integer driverId, Double amount, Integer oid, LocalDateTime recordTime) {
        this.driverId = driverId;
        this.amount = amount;
        this.oid = oid;
        this.recordTime = recordTime;
    }
}
