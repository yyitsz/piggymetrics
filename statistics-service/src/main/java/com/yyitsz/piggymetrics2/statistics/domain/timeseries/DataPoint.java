package com.yyitsz.piggymetrics2.statistics.domain.timeseries;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

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
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class DataPoint {

    @Id
    @Column(name = "DATA_POINT_ID")
    @GeneratedValue(generator = "DataPointSeq", strategy = GenerationType.SEQUENCE)
    @JsonIgnore
    private Long dataPointId;

    @Column(name = "AC_NAME")
    private String accountName;

    @Column(name = "BUS_DATE")
    private LocalDate date;

    @OneToMany(mappedBy = "dataPoint", cascade = ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @Where(clause = "METRIC_TYPE='INCOME'")
    private List<IncomeItemMetric> incomes;

    @OneToMany(mappedBy = "dataPoint", cascade = ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @Where(clause = "METRIC_TYPE='EXPENSE'")
    private List<ExpenseItemMetric> expenses;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "ST_DATA_POINT_STAT")
    @MapKeyColumn(name = "STAT_METRIC")
    @Column(name = "VAL")
    @MapKeyEnumerated(EnumType.STRING)
    @Fetch(FetchMode.SUBSELECT)
    private Map<StatisticMetric, BigDecimal> statistics;

}
