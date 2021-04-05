package com.yyitsz.piggymetrics2.statistics.domain.timeseries;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static javax.persistence.CascadeType.ALL;

/**
 * Represents daily time series data point containing
 * current account state
 */
//@Document(collection = "datapoints")
@Table(name = "ST_DATA_POINT",
        uniqueConstraints = @UniqueConstraint(name = "ST_DATA_POINT_UK1", columnNames = {"AC_NAME", "BUS_DATE"})
)
@Entity
@SequenceGenerator(name = "DataPointSeq", sequenceName = "DataPointSeq")
@Data
public class DataPoint {

    @Id
    @Column(name = "DATA_POINT_ID")
    @GeneratedValue(generator = "DataPointSeq", strategy = GenerationType.SEQUENCE)
    private Long dataPointId;

    @Column(name = "AC_NAME")
    private String accountName;

    @Column(name = "BUS_DATE")
    private LocalDate date;

    @OneToMany(mappedBy = "dataPoint", cascade = ALL, orphanRemoval = true)
    private List<IncomeItemMetric> incomes;

    @OneToMany(mappedBy = "dataPoint", cascade = ALL, orphanRemoval = true)
    private List<ExpenseItemMetric> expenses;

    @ElementCollection
    @CollectionTable(name = "ST_DATA_POINT_STAT")
    @MapKeyColumn(name = "STAT_METRIC")
    @Column(name = "VAL")
    @MapKeyEnumerated(EnumType.STRING)
    private Map<StatisticMetric, BigDecimal> statistics;

}
