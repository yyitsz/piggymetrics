package com.yyitsz.piggymetrics2.statistics.domain.timeseries;

import com.yyitsz.piggymetrics2.statistics.domain.Currency;
import com.yyitsz.piggymetrics2.statistics.domain.TimePeriod;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Represents normalized {@link com.yyitsz.piggymetrics2.statistics.domain.Item} object
 * with {@link Currency#getBase()} currency and {@link TimePeriod#getBase()} time period
 */

@Entity
@DiscriminatorValue("INCOME")
@Table(name = "ST_ITEM_METRIC",
        uniqueConstraints = @UniqueConstraint(name = "ST_ITEM_METRIC_UK1",
                columnNames = {"DATA_POINT_ID", "METRIC_TYPE", "TITLE"})
)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "METRIC_TYPE", discriminatorType = DiscriminatorType.STRING, length = 30)
@Data
public class ExpenseItemMetric extends ItemMetric {
    public ExpenseItemMetric() {
    }

    public ExpenseItemMetric(String title, BigDecimal amount) {
        super(title, amount);
    }
}
