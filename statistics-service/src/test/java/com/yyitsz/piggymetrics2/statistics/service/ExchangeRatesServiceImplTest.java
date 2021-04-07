package com.yyitsz.piggymetrics2.statistics.service;

import com.google.common.collect.ImmutableMap;

import com.yyitsz.piggymetrics2.statistics.client.ExchangeRatesClient;
import com.yyitsz.piggymetrics2.statistics.domain.Currency;
import com.yyitsz.piggymetrics2.statistics.domain.ExchangeRatesContainer;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class ExchangeRatesServiceImplTest {

    @InjectMocks
    private ExchangeRatesServiceImpl ratesService;

    @Mock
    private ExchangeRatesClient client;


    @Test
    public void shouldReturnCurrentRatesWhenContainerIsEmptySoFar() {

        ExchangeRatesContainer container = new ExchangeRatesContainer();
        container.setRates(ImmutableMap.of(
                Currency.EUR.name(), new BigDecimal("0.8"),
                Currency.RUB.name(), new BigDecimal("80")
        ));

        when(client.getRates(Currency.getBase())).thenReturn(container);

        Map<Currency, BigDecimal> result = ratesService.getCurrentRates();
        verify(client, times(1)).getRates(Currency.getBase());

        assertEquals(container.getRates().get(Currency.EUR.name()), result.get(Currency.EUR));
        assertEquals(container.getRates().get(Currency.RUB.name()), result.get(Currency.RUB));
        assertEquals(BigDecimal.ONE, result.get(Currency.USD));
    }

    @Test
    public void shouldNotRequestRatesWhenTodaysContainerAlreadyExists() {

        ExchangeRatesContainer container = new ExchangeRatesContainer();
        container.setRates(ImmutableMap.of(
                Currency.EUR.name(), new BigDecimal("0.8"),
                Currency.RUB.name(), new BigDecimal("80")
        ));

        when(client.getRates(Currency.getBase())).thenReturn(container);

        // initialize container
        ratesService.getCurrentRates();

        // use existing container
        ratesService.getCurrentRates();

        verify(client, times(1)).getRates(Currency.getBase());
    }

    @Test
    public void shouldConvertCurrency() {

        ExchangeRatesContainer container = new ExchangeRatesContainer();
        container.setRates(ImmutableMap.of(
                Currency.EUR.name(), new BigDecimal("0.8"),
                Currency.RUB.name(), new BigDecimal("80")
        ));

        when(client.getRates(Currency.getBase())).thenReturn(container);

        final BigDecimal amount = new BigDecimal(100);
        final BigDecimal expectedConversionResult = new BigDecimal("1.25");

        BigDecimal result = ratesService.convert(Currency.RUB, Currency.USD, amount);

        assertTrue(expectedConversionResult.compareTo(result) == 0);
    }

    @Test
    public void shouldFailToConvertWhenAmountIsNull() {
        assertThrows(IllegalArgumentException.class, () -> ratesService.convert(Currency.EUR, Currency.RUB, null));
    }
}