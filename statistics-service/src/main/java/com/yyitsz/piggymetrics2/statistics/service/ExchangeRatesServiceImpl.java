package com.yyitsz.piggymetrics2.statistics.service;

import com.google.common.collect.ImmutableMap;
import com.yyitsz.piggymetrics2.statistics.client.ExchangeRatesClient;
import com.yyitsz.piggymetrics2.statistics.domain.Currency;
import com.yyitsz.piggymetrics2.statistics.domain.ExchangeRatesContainer;
import com.yyitsz.piggymetrics2.statistics.domain.timeseries.ExchangeRate;
import com.yyitsz.piggymetrics2.statistics.repository.ExchangeRateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExchangeRatesServiceImpl implements ExchangeRatesService {

    private static final Logger log = LoggerFactory.getLogger(ExchangeRatesServiceImpl.class);


    @Autowired
    private ExchangeRatesClient client;

    //@Autowired
    private ExchangeRateRepository exchangeRateRepository;

    private ExchangeRatesContainer container;


    /**
     * {@inheritDoc}
     */
    @Override
    public Map<Currency, BigDecimal> getCurrentRates() {

        if (container == null || !container.getDate().equals(LocalDate.now())) {
            container = client.getRates(Currency.getBase());
            log.info("exchange rates has been updated: {}", container);
        }

        return new HashMap<>(ImmutableMap.of(
                Currency.EUR, container.getRates().get(Currency.EUR.name()),
                Currency.RUB, container.getRates().get(Currency.RUB.name()),
                Currency.USD, BigDecimal.ONE
        ));
    }

    /**
     * {@inheritDoc}
     */
    //@Override
    private Map<Currency, BigDecimal> getCurrentRates2() {

        LocalDate date = LocalDate.now();
        List<ExchangeRate> exchangeRateList = exchangeRateRepository.findByDate(date);

        if (exchangeRateList.isEmpty()) {
            ExchangeRatesContainer container = client.getRates(Currency.getBase());

            log.info("got exchange rate: {}", container);

            for (Map.Entry<String, BigDecimal> kv : container.getRates().entrySet()) {
                try {
                    Currency currency = Currency.valueOf(kv.getKey());
                    ExchangeRate exchangeRate = new ExchangeRate();
                    exchangeRate.setDate(date);
                    exchangeRate.setCurrency(currency);
                    exchangeRate.setRate(kv.getValue());

                    exchangeRateList.add(exchangeRate);

                } catch (IllegalArgumentException e) {
                    //ignore
                }

            }
            exchangeRateRepository.saveAll(exchangeRateList);
            log.info("exchange rates has been updated: {}", exchangeRateList);
        }

        return ImmutableMap.of(
                Currency.EUR, getRate(exchangeRateList, Currency.EUR),
                Currency.RUB, getRate(exchangeRateList, Currency.RUB),
                Currency.USD, BigDecimal.ONE
        );
    }

    private BigDecimal getRate(List<ExchangeRate> exchangeRateList, Currency currency) {
        for (ExchangeRate exchangeRate : exchangeRateList) {
            if (exchangeRate.getCurrency() == currency) {
                return exchangeRate.getRate();
            }
        }
        return BigDecimal.ONE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal convert(Currency from, Currency to, BigDecimal amount) {

        Assert.notNull(amount, "amount is required");

        Map<Currency, BigDecimal> rates = getCurrentRates();
        BigDecimal ratio = rates.get(to).divide(rates.get(from), 4, RoundingMode.HALF_UP);

        return amount.multiply(ratio);
    }
}
