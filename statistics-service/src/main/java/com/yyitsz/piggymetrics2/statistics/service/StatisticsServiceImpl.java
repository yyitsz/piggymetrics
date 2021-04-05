package com.yyitsz.piggymetrics2.statistics.service;

import com.google.common.collect.ImmutableMap;
import com.yyitsz.piggymetrics2.statistics.domain.*;
import com.yyitsz.piggymetrics2.statistics.domain.timeseries.*;
import com.yyitsz.piggymetrics2.statistics.repository.DataPointRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private DataPointRepository repository;

    @Autowired
    private ExchangeRatesService ratesService;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DataPoint> findByAccountName(String accountName) {
        Assert.hasLength(accountName, "accountName is required.");
        return repository.findByAccountName(accountName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataPoint save(String accountName, Account account) {

        DataPointId pointId = new DataPointId(accountName, LocalDate.now());

        List<IncomeItemMetric> incomes = account.getIncomes().stream()
                .map(this::createIncomeItemMetric)
                .collect(Collectors.toList());

        List<ExpenseItemMetric> expenses = account.getExpenses().stream()
                .map(this::createExpenseItemMetric)
                .collect(Collectors.toList());

        Map<StatisticMetric, BigDecimal> statistics = createStatisticMetrics(incomes, expenses, account.getSaving());

        DataPoint dataPoint = new DataPoint();
        dataPoint.setDate(LocalDate.now());
        dataPoint.setAccountName(accountName);
        dataPoint.setIncomes(incomes);
        dataPoint.setExpenses(expenses);
        dataPoint.setStatistics(statistics);

        log.debug("new datapoint has been created: {}", pointId);

        return repository.save(dataPoint);
    }

    private Map<StatisticMetric, BigDecimal> createStatisticMetrics(List<IncomeItemMetric> incomes, List<ExpenseItemMetric> expenses, Saving saving) {

        BigDecimal savingAmount = ratesService.convert(saving.getCurrency(), Currency.getBase(), saving.getAmount());

        BigDecimal expensesAmount = expenses.stream()
                .map(ItemMetric::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal incomesAmount = incomes.stream()
                .map(ItemMetric::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return ImmutableMap.of(
                StatisticMetric.EXPENSES_AMOUNT, expensesAmount,
                StatisticMetric.INCOMES_AMOUNT, incomesAmount,
                StatisticMetric.SAVING_AMOUNT, savingAmount
        );
    }

    /**
     * Normalizes given item amount to {@link Currency#getBase()} currency with
     * {@link TimePeriod#getBase()} time period
     */
    private IncomeItemMetric createIncomeItemMetric(Item item) {
        BigDecimal amount = ratesService
                .convert(item.getCurrency(), Currency.getBase(), item.getAmount())
                .divide(item.getPeriod().getBaseRatio(), 4, RoundingMode.HALF_UP);

        return new IncomeItemMetric(item.getTitle(), amount);

    }

    private ExpenseItemMetric createExpenseItemMetric(Item item) {
        BigDecimal amount = ratesService
                .convert(item.getCurrency(), Currency.getBase(), item.getAmount())
                .divide(item.getPeriod().getBaseRatio(), 4, RoundingMode.HALF_UP);

        return new ExpenseItemMetric(item.getTitle(), amount);

    }
}
