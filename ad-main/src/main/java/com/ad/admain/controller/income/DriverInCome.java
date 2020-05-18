package com.ad.admain.controller.income;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * @author wezhyn
 * @since 05.16.2020
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(indexes = {
        @Index(name = "deliver_unique", columnList = "driver_id", unique = true)
})
public class DriverInCome {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @Column(name = "driver_id", columnDefinition = "decimal(15,5)")
    private Integer driverId;

    private Double amount;


}
