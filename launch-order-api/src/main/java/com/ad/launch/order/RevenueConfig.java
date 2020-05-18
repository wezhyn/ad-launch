package com.ad.launch.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wezhyn
 * @since 05.17.2020
 */
@Getter
@Setter
@ToString
public class RevenueConfig implements Serializable {

    private static final int[] TIME_SCOPE = {7, 10, 16, 17, 20, 21, 24};
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private RevenueConfigPair seven = new RevenueConfigPair(TIME_SCOPE[0], 0.01);

    private RevenueConfigPair ten = new RevenueConfigPair(TIME_SCOPE[1], 0.02);

    private RevenueConfigPair sixteen = new RevenueConfigPair(TIME_SCOPE[2], 0.025);

    private RevenueConfigPair seventeen = new RevenueConfigPair(TIME_SCOPE[3], 0.03);

    private RevenueConfigPair twenty = new RevenueConfigPair(TIME_SCOPE[4], 0.04);

    private RevenueConfigPair twentyOne = new RevenueConfigPair(TIME_SCOPE[5], 0.035);

    private RevenueConfigPair twentyFour = new RevenueConfigPair(TIME_SCOPE[6], 0.024);

    @SuppressWarnings("unchecked")
    public static RevenueConfig fromJson(String json) throws IOException {
        final Map<String, String> map = (Map<String, String>) MAPPER.readValue(json, Map.class);
        RevenueConfig defaultConfig = new RevenueConfig();
        for (RevenueConfigPair pair : defaultConfig.getAll()) {
            final String mountStr = map.get(pair.getEndTime().toString());
            if (!mountStr.equals(pair.getRevenue().toString())) {
                pair.setRevenue(Double.valueOf(mountStr));
            }
        }
        return defaultConfig;
    }

    /**
     * 原数据
     *
     * @return pair
     */
    public RevenueConfigPair[] getAll() {
        return new RevenueConfigPair[]{seven, ten, sixteen, seventeen, twenty, twentyOne, twentyFour};
    }


    public static int revenueScope(LocalDateTime time) {
        final int hour = time.getHour();
        final int day = time.getDayOfYear();
        final int year = time.getYear();
        return year * 10000 + day * 10 + timeScopeIndex(hour);
    }

    private static int timeScopeIndex(int hour) {
        int high = TIME_SCOPE.length, low = 0;
        while (low <= high) {
            int middle = (high + low) / 2;
            int value = TIME_SCOPE[middle];
            if (value == hour) {
                return middle;
            } else if (value < hour) {
                low = middle + 1;
            } else {
                high = middle - 1;
            }
        }
        return high + 1;
    }


    public double revenue(Integer timeScope) {
        return find(timeScope).getRevenue();
    }

    private RevenueConfigPair find(Integer timeScope) {
        final RevenueConfigPair[] pairs = getAll();
        int high = pairs.length, low = 0;
        int h = TIME_SCOPE[timeScope % 10];
        while (low <= high) {
            int middle = (low + high) / 2;
            int v = pairs[middle].getEndHour();
            if (v == h) {
                return pairs[middle];
            } else if (v > h) {
                high = middle - 1;
            } else {
                low = middle + 1;
            }
        }
        throw new RuntimeException("时间范围错误");
    }

    public String toJson() throws JsonProcessingException {
        Map<String, String> map = new HashMap<>(16);
        for (RevenueConfigPair revenueConfigPair : getAll()) {
            map.put(revenueConfigPair.getEndTime().toString(), revenueConfigPair.getRevenue().toString());
        }
        return MAPPER.writeValueAsString(map);
    }


    @Getter
    @EqualsAndHashCode
    @Setter
    public static class RevenueConfigPair implements Serializable {
        private final LocalTime endTime;
        private final int endHour;
        private Double revenue;

        public RevenueConfigPair(int endTime, Double revenue) {
            if (endTime == 24) {
                this.endTime = LocalTime.MIN;
            } else {
                this.endTime = LocalTime.of(endTime, 0);
            }
            this.revenue = revenue;
            this.endHour = this.endTime.getHour();
        }
    }


}
