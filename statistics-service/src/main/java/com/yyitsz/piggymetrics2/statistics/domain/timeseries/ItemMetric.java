package com.yyitsz.piggymetrics2.statistics.domain.timeseries;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.yyitsz.piggymetrics2.common.domain.BaseModel;
import com.yyitsz.piggymetrics2.statistics.domain.Currency;
import com.yyitsz.piggymetrics2.statistics.domain.TimePeriod;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Represents normalized {@link com.yyitsz.piggymetrics2.statistics.domain.Item} object
 * with {@link Currency#getBase()} currency and {@link TimePeriod#getBase()} time period
 */

@MappedSuperclass
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class ItemMetric extends BaseModel {

    @Id
    @GeneratedValue(generator = "ItemMetricSeq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "ItemMetricSeq", sequenceName = "ItemMetricSeq")
    @Column(name = "ITEM_METRIC_ID")
    @JsonIgnore
    private Long itemMetricId;

    @ManyToOne
    @JoinColumn(name = "DATA_POINT_ID")
    @JsonIgnore
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
