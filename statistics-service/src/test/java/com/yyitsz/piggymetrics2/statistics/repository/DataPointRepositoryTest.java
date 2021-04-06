package com.yyitsz.piggymetrics2.statistics.repository;

import com.google.common.collect.ImmutableMap;
import com.yyitsz.piggymetrics2.statistics.domain.timeseries.DataPoint;
import com.yyitsz.piggymetrics2.statistics.domain.timeseries.ExpenseItemMetric;
import com.yyitsz.piggymetrics2.statistics.domain.timeseries.IncomeItemMetric;
import com.yyitsz.piggymetrics2.statistics.domain.timeseries.StatisticMetric;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class DataPointRepositoryTest {

    @Autowired
    private DataPointRepository repository;

    @Test
    public void shouldSaveDataPoint() {

        IncomeItemMetric salary = new IncomeItemMetric("salary", new BigDecimal(20_000));

        ExpenseItemMetric grocery = new ExpenseItemMetric("grocery", new BigDecimal(1_000));
        ExpenseItemMetric vacation = new ExpenseItemMetric("vacation", new BigDecimal(2_000));

        String acName = "test-account";
        LocalDate date = LocalDate.now();


        DataPoint point = new DataPoint();
        point.setAccountName(acName);
        point.setDate(date);
        point.setIncomes(Lists.newArrayList(salary));
        point.setExpenses(Lists.newArrayList(grocery, vacation));
        point.setStatistics(new HashMap<>(ImmutableMap.of(
                StatisticMetric.SAVING_AMOUNT, new BigDecimal(400_000),
                StatisticMetric.INCOMES_AMOUNT, new BigDecimal(20_000),
                StatisticMetric.EXPENSES_AMOUNT, new BigDecimal(3_000))
        ));

        repository.save(point);

        List<DataPoint> points = repository.findByAccountName(acName);
        assertEquals(1, points.size());
        assertEquals(date, points.get(0).getDate());
        assertEquals(point.getStatistics().size(), points.get(0).getStatistics().size());
        assertEquals(point.getIncomes().size(), points.get(0).getIncomes().size());
        assertEquals(point.getExpenses().size(), points.get(0).getExpenses().size());
    }

    //@Test
    public void shouldRewriteDataPointWithinADay() {

        final BigDecimal earlyAmount = new BigDecimal(100);
        final BigDecimal lateAmount = new BigDecimal(200);
        String acName = "test-account";
        LocalDate date = LocalDate.ofEpochDay(0);

        DataPoint earlier = new DataPoint();
        earlier.setAccountName(acName);
        earlier.setDate(date);
        earlier.setStatistics(new HashMap<>(ImmutableMap.of(
                StatisticMetric.SAVING_AMOUNT, earlyAmount
        )));

        repository.save(earlier);

        DataPoint later = new DataPoint();
        later.setAccountName(acName);
        later.setDate(LocalDate.now());
        later.setStatistics(new HashMap<>(ImmutableMap.of(
                StatisticMetric.SAVING_AMOUNT, lateAmount
        )));

        repository.save(later);

        repository.flush();

        List<DataPoint> points = repository.findByAccountName(acName);

        assertEquals(1, points.size());
        assertEquals(lateAmount, points.get(0).getStatistics().get(StatisticMetric.SAVING_AMOUNT));
    }
}
