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
@Data
@MappedSuperclass
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@DiscriminatorColumn(name = "METRIC_TYPE", discriminatorType = DiscriminatorType.STRING, length = 30)
public class ItemMetric {

    @Id
    @GeneratedValue(generator = "ItemMetricSeq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "ItemMetricSeq", sequenceName = "ItemMetricSeq")
    @Column(name = "ITEM_METRIC_ID")
    private Long itemMetricId;

    @ManyToOne
    @JoinColumn(name = "DATA_POINT_ID")
    private DataPoint dataPoint;
    @Column(name = "TITLE")
    private String title;
    @Column(name = "AMT")
    private BigDecimal amount;

    public ItemMetric() {
    }

    public ItemMetric(String title, BigDecimal amount) {
        this.title = title;
        this.amount = amount;
    }
}
