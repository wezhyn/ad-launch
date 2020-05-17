package com.ad.launch.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.IOException;
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
public class RevenueConfig {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private RevenueConfigPair seven = new RevenueConfigPair(7, 0.01);

    private RevenueConfigPair ten = new RevenueConfigPair(10, 0.02);

    private RevenueConfigPair sixteen = new RevenueConfigPair(16, 0.025);

    private RevenueConfigPair seventeen = new RevenueConfigPair(17, 0.03);

    private RevenueConfigPair twenty = new RevenueConfigPair(20, 0.04);

    private RevenueConfigPair twentyOne = new RevenueConfigPair(21, 0.035);

    private RevenueConfigPair twentyFour = new RevenueConfigPair(24, 0.024);

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

    public double revenue(LocalTime time) {
        final RevenueConfigPair[] pairs = getAll();
        for (RevenueConfigPair pair : pairs) {
            if (time.isBefore(pair.getEndTime())) {
                return pair.getRevenue();
            }
        }
        return twentyFour.getRevenue();
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
    public static class RevenueConfigPair {
        private final LocalTime endTime;
        private Double revenue;

        public RevenueConfigPair(int endTime, Double revenue) {
            if (endTime == 24) {
                this.endTime = LocalTime.MIN;
            } else {
                this.endTime = LocalTime.of(endTime, 0);
            }
            this.revenue = revenue;
        }
    }


}
