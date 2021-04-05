package com.yyitsz.piggymetrics2.statistics.domain.timeseries;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
@Data
public class DataPointId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column
	private String account;

	@Column
	private LocalDate date;

	public DataPointId() {
	}

	public DataPointId(String account, LocalDate date) {
		this.account = account;
		this.date = date;
	}

	public String getAccount() {
		return account;
	}

	public LocalDate getDate() {
		return date;
	}

	@Override
	public String toString() {
		return "DataPointId{" +
				"account='" + account + '\'' +
				", date=" + date +
				'}';
	}
}
