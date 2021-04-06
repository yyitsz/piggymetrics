package com.yyitsz.piggymetrics2.notification.repository.converter;

import com.yyitsz.piggymetrics2.notification.domain.Frequency;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class FrequencyReaderConverter implements Converter<Integer, Frequency> {

    @Override
    public Frequency convert(Integer days) {
        return Frequency.withDays(days);
    }
}
