package com.example.evostyle.common.util;

import com.example.evostyle.global.exception.ErrorCode;
import com.example.evostyle.global.exception.JsonSerializationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JsonHelper {

    private final ObjectMapper objectMapper;

    public String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new JsonSerializationException(ErrorCode.JSON_SERIALIZATION_FAILED);
        }
    }

    public <T> T fromJson(String json,Class<T> tClass) {
        try {
            return objectMapper.readValue(json, tClass);
        } catch (JsonProcessingException e) {
            throw new JsonSerializationException(ErrorCode.JSON_SERIALIZATION_FAILED);
        }
    }

    public <T> T convert(Object source, Class<T> targetType) {
        try {
            return objectMapper.convertValue(source, targetType);
        } catch (IllegalArgumentException e) {
            throw new JsonSerializationException(ErrorCode.JSON_SERIALIZATION_FAILED);
        }
    }
}

