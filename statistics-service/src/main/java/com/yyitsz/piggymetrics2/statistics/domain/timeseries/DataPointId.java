package com.yyitsz.piggymetrics2.statistics.domain.timeseries;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
@Setter
@Getter
@EqualsAndHashCode
public class DataPointId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "AC_NAME")
    private String account;

    @Column(name = "BUS_DATE")
    private LocalDate date;

    public DataPointId() {
    }

    public DataPointId(String account, LocalDate date) {
        this.account = account;
        this.date = date;
    }

    @Override
    public String toString() {
        return "DataPointId{" +
                "account='" + account + '\'' +
                ", date=" + date +
                '}';
    }
}
