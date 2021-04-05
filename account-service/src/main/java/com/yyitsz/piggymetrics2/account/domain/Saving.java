package com.yyitsz.piggymetrics2.account.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;


@Embeddable
@Getter
@Setter
@EqualsAndHashCode
public class Saving {

	@NotNull
	@Column(name="SAVING_AMT")
	private BigDecimal amount;

	@NotNull
	@Column(name="SAVING_CCY")
	private Currency currency;

	@NotNull
	@Column(name="SAVING_INT")
	private BigDecimal interest;

	@NotNull
	@Column(name="IS_DEPOSIT")
	private Boolean deposit;

	@NotNull
	@Column(name="IS_CAPITAL")
	private Boolean capitalization;
}
