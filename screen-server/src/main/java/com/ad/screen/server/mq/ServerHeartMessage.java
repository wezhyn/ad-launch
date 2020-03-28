package com.ad.screen.server.mq;

import com.ad.screen.server.config.GlobalIdentify;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * 服务器心跳帧, 定时向 rocket 心跳帧队列发送心跳帧，其他服务器检测到当前服务宕机后，接管由当前程序管理的订单
 *
 * @author wezhyn
 * @since 03.28.2020
 */
@Data
public class ServerHeartMessage {


    private String identify;
    @JsonSerialize(using=LocalDateTimeTimestampSerializer.class)
    @JsonDeserialize(using=LocalDateTimeTimestampDeserialize.class)
    private LocalDateTime sendTime;

    public ServerHeartMessage(String identify, LocalDateTime sendTime) {
        this.identify=identify;
        this.sendTime=sendTime;
    }

    public ServerHeartMessage() {
    }

    public static ServerHeartMessage create() {
        return new ServerHeartMessage(GlobalIdentify.IDENTIFY.getId(), LocalDateTime.now());
    }

    public static class LocalDateTimeTimestampSerializer extends JsonSerializer<LocalDateTime> {
        @Override
        public void serialize(LocalDateTime localDateTime, JsonGenerator jsonGenerator,
                              SerializerProvider serializerProvider) throws IOException {

            jsonGenerator.writeString(LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli() + "");
        }
    }

    public static class LocalDateTimeTimestampDeserialize extends JsonDeserializer<LocalDateTime> {
        @Override
        public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            String text=p.getText();
            long timestamp=Long.parseLong(text);
            return LocalDateTime.ofEpochSecond(timestamp/1000, 0, ZoneOffset.ofHours(8));
        }
    }

}
