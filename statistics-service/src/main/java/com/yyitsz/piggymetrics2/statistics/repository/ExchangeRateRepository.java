package com.yyitsz.piggymetrics2.statistics.repository;

import com.yyitsz.piggymetrics2.statistics.domain.Currency;
import com.yyitsz.piggymetrics2.statistics.domain.timeseries.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {

    ExchangeRate findByDateAndCurrency(LocalDate date, Currency currency);

    List<ExchangeRate> findByDate(LocalDate date);
}
