package com.ad.admain.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomJsonDateDeserializer extends JsonDeserializer<Date> {
    @Override
    public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String date=jp.getText();
        try {
            return format.parse(date);
        } catch (ParseException e1) {
            format=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            try {
                return format.parse(date);
            } catch (ParseException e2) {
                format=new SimpleDateFormat("yyyy-MM-dd");
                try {
                    return format.parse(date);
                } catch (ParseException e3) {
                    throw new RuntimeException(e3);
                }
            }
        }
    }
}