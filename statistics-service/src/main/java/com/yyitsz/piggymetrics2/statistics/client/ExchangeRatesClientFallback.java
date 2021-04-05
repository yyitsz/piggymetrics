package com.yyitsz.piggymetrics2.statistics.client;

import com.yyitsz.piggymetrics2.statistics.domain.Currency;
import com.yyitsz.piggymetrics2.statistics.domain.ExchangeRatesContainer;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;

@Component
public class ExchangeRatesClientFallback implements ExchangeRatesClient {

    @Override
    public ExchangeRatesContainer getRates(Currency base) {
        ExchangeRatesContainer container = new ExchangeRatesContainer();
        container.setBase(Currency.getBase());
        container.setRates(new HashMap<>(3));
        container.getRates().put(Currency.EUR.name(), new BigDecimal("1.5"));
        container.getRates().put(Currency.RUB.name(), new BigDecimal("3.5"));
        container.getRates().put(Currency.USD.name(), new BigDecimal("1"));
        return container;
    }
}
