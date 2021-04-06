package com.yyitsz.piggymetrics2.notification.repository.converter;

import com.yyitsz.piggymetrics2.notification.domain.Frequency;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class FrequencyJpaConverter implements AttributeConverter<Frequency, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Frequency attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getDays();
    }

    @Override
    public Frequency convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return Frequency.withDays(dbData);
    }
}
