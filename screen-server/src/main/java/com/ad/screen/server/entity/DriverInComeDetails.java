package com.ad.screen.server.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

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

    @ColumnDefault("current_timestamp")
    private LocalDateTime recordTime;

    public DriverInComeDetails(Integer driverId, Double amount, LocalDateTime recordTime) {
        this.driverId = driverId;
        this.amount = amount;
        this.recordTime = recordTime;
    }
}