package com.yyitsz.piggymetrics2.statistics.domain.timeseries;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yyitsz.piggymetrics2.common.domain.BaseModel;
import com.yyitsz.piggymetrics2.common.domain.Key;
import com.yyitsz.piggymetrics2.common.domain.ModelUtils;
import com.yyitsz.piggymetrics2.statistics.domain.Currency;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static javax.persistence.CascadeType.ALL;

/**
 * Represents daily time series data point containing
 * current account state
 */
//@Document(collection = "datapoints")
@Table(name = "ST_DATA_POINT"
        //,uniqueConstraints = @UniqueConstraint(name = "ST_DATA_POINT_UK1", columnNames = {"AC_NAME", "BUS_DATE"})
)

@Entity
//@SequenceGenerator(name = "DataPointSeq", sequenceName = "DataPointSeq")
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(value = {"createTime", "updateTime", "version", "createBy", "updatedBy"})
//@NaturalIdCache
public class DataPoint extends BaseModel {

//    @Id
//    @Column(name = "DATA_POINT_ID")
//    @GeneratedValue(generator = "DataPointSeq", strategy = GenerationType.SEQUENCE)
//    @JsonIgnore
//    private Long dataPointId;

    @EmbeddedId
    private DataPointId id;

    @OneToMany(mappedBy = "dataPoint", cascade = ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @Where(clause = "ITEM_TYPE='INCOME'")
    private List<IncomeItemMetric> incomes;

    @OneToMany(mappedBy = "dataPoint", cascade = ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @Where(clause = "ITEM_TYPE='EXPENSE'")
    private List<ExpenseItemMetric> expenses;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "ST_DATA_POINT_STAT",
            joinColumns = {@JoinColumn(name = "AC_NAME"), @JoinColumn(name = "BUS_DATE")})
    @MapKeyColumn(name = "STAT_METRIC")
    @Column(name = "VAL")
    @MapKeyEnumerated(EnumType.STRING)
    @Fetch(FetchMode.SUBSELECT)
    private Map<StatisticMetric, BigDecimal> statistics;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "ST_DATA_POINT_CCY_RATE",
            joinColumns = {@JoinColumn(name = "AC_NAME"), @JoinColumn(name = "BUS_DATE")})
    @MapKeyColumn(name = "CCY")
    @Column(name = "RATE")
    @MapKeyEnumerated(EnumType.STRING)
    @Fetch(FetchMode.SUBSELECT)
    private Map<Currency, BigDecimal> rates;

    public void setIncomes(List<IncomeItemMetric> incomes) {
        if (this.incomes == null) {
            this.incomes = incomes;
            initDataPoint(this.incomes);
        } else {
            ModelUtils.BASE_MODEL.copy(incomes, this.incomes,
                    item -> {
                        return new Key(item.getTitle());
                    },
                    item -> {
                        IncomeItemMetric newItem = new IncomeItemMetric();
                        newItem.setDataPoint(this);
                        newItem.setTitle(item.getTitle());
                        return newItem;
                    }, "itemMetricId", "dataPoint", "title");
        }
    }

    public void setExpenses(List<ExpenseItemMetric> expenses) {
        if (this.expenses == null) {
            this.expenses = expenses;
            initDataPoint(this.expenses);
        } else {
            ModelUtils.BASE_MODEL.copy(expenses, this.expenses,
                    item -> {
                        return new Key(item.getTitle());
                    },
                    item -> {
                        ExpenseItemMetric newItem = new ExpenseItemMetric();
                        newItem.setDataPoint(this);
                        newItem.setTitle(item.getTitle());
                        return newItem;
                    }, "itemMetricId", "dataPoint", "title");
        }
    }

    private <T extends ItemMetric> void initDataPoint(List<T> items) {
        items.forEach(item -> item.setDataPoint(this));
    }
}
