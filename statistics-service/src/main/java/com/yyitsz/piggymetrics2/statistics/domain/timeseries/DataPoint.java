package com.yyitsz.piggymetrics2.statistics.domain.timeseries;

import com.yyitsz.piggymetrics2.statistics.domain.Currency;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

/**
 * Represents daily time series data point containing
 * current account state
 */
//@Document(collection = "datapoints")
public class DataPoint {

	//@Id
	private DataPointId id;

	private Set<ItemMetric> incomes;

	private Set<ItemMetric> expenses;

	private Map<StatisticMetric, BigDecimal> statistics;

	private Map<Currency, BigDecimal> rates;

	public DataPointId getId() {
		return id;
	}

	public void setId(DataPointId id) {
		this.id = id;
	}

	public Set<ItemMetric> getIncomes() {
		return incomes;
	}

	public void setIncomes(Set<ItemMetric> incomes) {
		this.incomes = incomes;
	}

	public Set<ItemMetric> getExpenses() {
		return expenses;
	}

	public void setExpenses(Set<ItemMetric> expenses) {
		this.expenses = expenses;
	}

	public Map<StatisticMetric, BigDecimal> getStatistics() {
		return statistics;
	}

	public void setStatistics(Map<StatisticMetric, BigDecimal> statistics) {
		this.statistics = statistics;
	}

	public Map<Currency, BigDecimal> getRates() {
		return rates;
	}

	public void setRates(Map<Currency, BigDecimal> rates) {
		this.rates = rates;
	}
}
