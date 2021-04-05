package com.yyitsz.piggymetrics2.statistics.domain.timeseries;


import com.yyitsz.piggymetrics2.common.domain.BaseModel;
import com.yyitsz.piggymetrics2.statistics.domain.Currency;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "ST_EXCH_RATE", uniqueConstraints = @UniqueConstraint(name = "ST_EXCH_RATE_UK1", columnNames = {"BUS_DATE", "CCY"}))
@SequenceGenerator(name = "ExchangeRateSeq", sequenceName = "ExchangeRateSeq")
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class ExchangeRate extends BaseModel {
    @Id
    @GeneratedValue(generator = "ExchangeRateSeq", strategy = GenerationType.SEQUENCE)
    @Column(name = "EXCH_RATE_ID")
    private Long id;

    @Column(name = "BUS_DATE")
    private LocalDate date;
    @Enumerated(EnumType.STRING)
    @Column(name = "CCY")
    private Currency currency;
    @Column(name = "RATE")
    private BigDecimal rate;

}
