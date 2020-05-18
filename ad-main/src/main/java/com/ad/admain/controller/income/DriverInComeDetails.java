package com.ad.admain.controller.income;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
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
