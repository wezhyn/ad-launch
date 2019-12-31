package com.ad.admain.controller.activity;

import com.ad.admain.controller.account.dto.AdminDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Id;
import java.io.IOException;

/**
 * @author wezhyn
 * @since 12.31.2019
 */
@Data
@Builder
public class ActivityDto {

    @Id
    private Integer id;

    private String publishTime;

    private String content;

    private String title;

    private Integer aid;


    @JsonSerialize(using=ActivitySerializer.class)
    @JsonProperty(access=JsonProperty.Access.READ_ONLY)
    private AdminDto admin;

    /**
     *
     */
    private Boolean publish;


    private static class ActivitySerializer extends JsonSerializer<AdminDto> {

        @Override
        public void serialize(AdminDto value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
//            gen.writeStringField("adminName", value.getUsername());
            gen.writeString(value.getUsername());
        }
    }

}
