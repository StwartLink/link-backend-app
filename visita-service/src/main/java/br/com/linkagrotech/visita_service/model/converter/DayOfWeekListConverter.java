package br.com.linkagrotech.visita_service.model.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

@Converter
public class DayOfWeekListConverter implements AttributeConverter<List<DayOfWeek>, String[]> {

    @Override
    public String[] convertToDatabaseColumn(List<DayOfWeek> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return new String[0];
        }
        return attribute.stream().map(DayOfWeek::name).toArray(String[]::new);
    }

    @Override
    public List<DayOfWeek> convertToEntityAttribute(String[] dbData) {
        if (dbData == null || dbData.length == 0) {
            return List.of();
        }
        return Arrays.stream(dbData).map(DayOfWeek::valueOf).collect(Collectors.toList());
    }
}


