package com.yyitsz.piggymetrics2.statistics.service;

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
import java.util.HashMap;
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
        List<DataPoint> dataPointList = repository.findByIdAccount(accountName);
        return dataPointList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataPoint save(String accountName, Account account) {

        LocalDate date = LocalDate.now();
        DataPointId dataPointId = new DataPointId(accountName, date);
        final DataPoint dataPoint = repository.findById(dataPointId)
                .orElseGet(() -> {
                    DataPoint newDataPoint = new DataPoint();
                    newDataPoint.setId(dataPointId);
                    return newDataPoint;
                });

        List<IncomeItemMetric> incomes = account.getIncomes().stream()
                .map(item -> createIncomeItemMetric(dataPoint, item))
                .collect(Collectors.toList());

        List<ExpenseItemMetric> expenses = account.getExpenses().stream()
                .map(item -> createExpenseItemMetric(dataPoint, item))
                .collect(Collectors.toList());

        Map<StatisticMetric, BigDecimal> statistics = createStatisticMetrics(incomes, expenses, account.getSaving());

        dataPoint.setIncomes(incomes);
        dataPoint.setExpenses(expenses);
        dataPoint.setStatistics(statistics);
        dataPoint.setRates(ratesService.getCurrentRates());

        log.debug("new datapoint has been created: {},{}", accountName, dataPoint.getId().getDate());

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

        Map<StatisticMetric, BigDecimal> map = new HashMap<>(3);
        map.put(StatisticMetric.EXPENSES_AMOUNT, expensesAmount);
        map.put(StatisticMetric.INCOMES_AMOUNT, incomesAmount);
        map.put(StatisticMetric.SAVING_AMOUNT, savingAmount);

        return map;
    }

    /**
     * Normalizes given item amount to {@link Currency#getBase()} currency with
     * {@link TimePeriod#getBase()} time period
     */
    private IncomeItemMetric createIncomeItemMetric(DataPoint dataPoint, Item item) {
        BigDecimal amount = ratesService
                .convert(item.getCurrency(), Currency.getBase(), item.getAmount())
                .divide(item.getPeriod().getBaseRatio(), 4, RoundingMode.HALF_UP);

        IncomeItemMetric incomeItemMetric = new IncomeItemMetric(item.getTitle(), amount);
        incomeItemMetric.setDataPoint(dataPoint);
        return incomeItemMetric;

    }

    private ExpenseItemMetric createExpenseItemMetric(DataPoint dataPoint, Item item) {
        BigDecimal amount = ratesService
                .convert(item.getCurrency(), Currency.getBase(), item.getAmount())
                .divide(item.getPeriod().getBaseRatio(), 4, RoundingMode.HALF_UP);

        ExpenseItemMetric expenseItemMetric = new ExpenseItemMetric(item.getTitle(), amount);
        expenseItemMetric.setDataPoint(dataPoint);
        return expenseItemMetric;

    }
}
