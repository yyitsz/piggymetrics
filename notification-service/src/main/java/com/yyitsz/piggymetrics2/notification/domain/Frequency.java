package com.yyitsz.piggymetrics2.notification.domain;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter
public enum Frequency implements AttributeConverter<Frequency, Integer> {

    WEEKLY(7), MONTHLY(30), QUARTERLY(90);

    private int days;

    Frequency(int days) {
        this.days = days;
    }

    public int getDays() {
        return days;
    }

    public static Frequency withDays(int days) {
        return Stream.of(Frequency.values())
                .filter(f -> f.getDays() == days)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public Integer convertToDatabaseColumn(Frequency attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.days;
    }

    @Override
    public Frequency convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return withDays(dbData);
    }
}
