package com.ad.admain.controller.pay;

import com.ad.admain.controller.pay.dto.AdProduceDto;
import com.ad.admain.controller.pay.to.AdProduce;
import com.ad.admain.convert.AbstractMapper;
import com.ad.admain.convert.CentralMapperConfig;
import com.ad.admain.convert.DateTimeMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author wezhyn
 * @since 02.25.2020
 */
@Mapper(
        config=CentralMapperConfig.class,
        unmappedSourcePolicy=ReportingPolicy.WARN,
        unmappedTargetPolicy=ReportingPolicy.WARN
)
public interface ProduceMapper extends AbstractMapper<AdProduce, AdProduceDto> {


    @Override
    AdProduceDto toDto(AdProduce produce);


    default LocalDate customDateStr(String string) {
        if (string==null) {
            return null;
        }
        LocalDateTime time=DateTimeMapper.parseLocalDateTime(string);
        return time==null ? null : time.toLocalDate();
    }

    default LocalTime customTimeStr(String string) {
        if (string==null) {
            return null;
        }
        LocalDateTime time=DateTimeMapper.parseLocalDateTime(string);
        return time==null ? null : time.toLocalTime();
    }

    default String customDate(LocalDate date) {
        return DateTimeMapper.convertLocalDateTime(LocalDateTime.of(date, LocalTime.MIN));
    }

    default String customTime(LocalTime date) {
        return DateTimeMapper.convertLocalDateTime(LocalDateTime.of(LocalDate.now(), date));
    }
}
