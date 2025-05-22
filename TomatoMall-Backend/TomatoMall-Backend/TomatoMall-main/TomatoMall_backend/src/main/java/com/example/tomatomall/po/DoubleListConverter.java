package com.example.tomatomall.po;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.List;

@Converter(autoApply = true)
public class DoubleListConverter implements AttributeConverter<List<Double>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<Double> rateList) {
        if (rateList == null || rateList.isEmpty()) {
            return "[]";
        }
        try {
            return objectMapper.writeValueAsString(rateList);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error converting rateList to JSON", e);
        }
    }

    @Override
    public List<Double> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.trim().isEmpty()) {
            return new ArrayList<>();
        }
        try {
            return objectMapper.readValue(dbData, new TypeReference<List<Double>>() {});
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error parsing rateList JSON: " + dbData, e);
        }
    }
}
