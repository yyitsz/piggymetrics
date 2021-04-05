package com.yyitsz.piggymetrics2.statistics.domain.timeseries;

import com.yyitsz.piggymetrics2.statistics.domain.Currency;
import com.yyitsz.piggymetrics2.statistics.domain.TimePeriod;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Represents normalized {@link com.yyitsz.piggymetrics2.statistics.domain.Item} object
 * with {@link Currency#getBase()} currency and {@link TimePeriod#getBase()} time period
 */

@Entity
@Table(name = "ST_ITEM_METRIC",
        uniqueConstraints = @UniqueConstraint(name = "ST_ITEM_METRIC_UK1",
                columnNames = {"DATA_POINT_ID", "METRIC_TYPE", "TITLE"})
)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "METRIC_TYPE", discriminatorType = DiscriminatorType.STRING, length = 30)
@DiscriminatorValue("INCOME")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class IncomeItemMetric extends ItemMetric {
    public IncomeItemMetric() {
    }

    public IncomeItemMetric(String title, BigDecimal amount) {
        super(title, amount);
    }
}
