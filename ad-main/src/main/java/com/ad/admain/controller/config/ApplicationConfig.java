package com.ad.admain.controller.config;

import com.wezhyn.project.annotation.StrategyEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author wezhyn
 * @since 05.17.2020
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationConfig {

    @Id
    @Type(type = "strategyEnum")
    @StrategyEnum(value = com.wezhyn.project.database.EnumType.NUMBER)
    private ApplicationConfigId id;


    private String configs;


}
