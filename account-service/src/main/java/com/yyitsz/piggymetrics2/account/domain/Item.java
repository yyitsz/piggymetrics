package com.yyitsz.piggymetrics2.account.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yyitsz.piggymetrics2.common.domain.BaseModel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@MappedSuperclass
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class Item extends BaseModel {


    @Id
    @GeneratedValue(generator = "ItemSeq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "ItemSeq", sequenceName = "ItemSeq")
    @Column(name = "ITEM_ID")
    @JsonIgnore
    private Long itemId;

    @ManyToOne
    @JoinColumn(name = "AC_NAME")
    @JsonIgnore
    private Account account;

    @NotNull
    @Length(min = 1, max = 20)
    @Column(name = "TITLE")
    private String title;

    @NotNull
    @Column(name = "AMT")
    private BigDecimal amount;

    @NotNull
    @Column(name = "CCY")
    private Currency currency;

    @NotNull
    @Column(name = "PERIOD")
    @Enumerated(EnumType.STRING)
    private TimePeriod period;

    @NotNull
    @Column(name = "ICON")
    private String icon;


}
