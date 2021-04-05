package com.yyitsz.piggymetrics2.statistics.domain.timeseries;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@DiscriminatorValue("EXPENSE")
@Table(name = "ST_ITEM_METRIC",
        uniqueConstraints = @UniqueConstraint(name = "ST_ITEM_METRIC_UK1",
                columnNames = {"DATA_POINT_ID", "ITEM_TYPE", "TITLE"})
)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "ITEM_TYPE", discriminatorType = DiscriminatorType.STRING, length = 30)
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(value = { "createTime","updateTime", "version", "createBy", "updatedBy"})
public class ExpenseItemMetric extends ItemMetric {
    public ExpenseItemMetric() {
    }

    public ExpenseItemMetric(String title, BigDecimal amount) {
        super(title, amount);
    }
}
