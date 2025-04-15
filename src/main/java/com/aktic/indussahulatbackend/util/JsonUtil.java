package com.aktic.indussahulatbackend.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JsonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    public static String convertObjectToJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to serialize object to JSON", e);
        }
    }

    public static <T> T convertJsonToObject(String json, Class<T> targetClass) {
        try {
            return objectMapper.readValue(json, targetClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize JSON to object", e);
        }
    }
}
