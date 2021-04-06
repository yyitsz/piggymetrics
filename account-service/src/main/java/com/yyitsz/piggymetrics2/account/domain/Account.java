package com.yyitsz.piggymetrics2.account.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yyitsz.piggymetrics2.common.domain.BaseModel;
import com.yyitsz.piggymetrics2.common.domain.Key;
import com.yyitsz.piggymetrics2.common.domain.ModelUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

//@Document(collection = "accounts")
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "AS_AC")
@Getter
@Setter
@EqualsAndHashCode
public class Account extends BaseModel {

    @Id
    @Column(name = "AC_NAME")
    private String name;

    @Column(name = "LAST_SEEN")
    private Date lastSeen;

    @Valid
    @OneToMany(mappedBy = "account", cascade = ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @Where(clause = "ITEM_TYPE='INCOME'")
    private List<IncomeItem> incomes;


    @Valid
    @OneToMany(mappedBy = "account", cascade = ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @Where(clause = "ITEM_TYPE='EXPENSE'")
    private List<ExpenseItem> expenses;

    @Valid
    @NotNull
    @Embedded
    private Saving saving;

    @Length(min = 0, max = 20_000)
    @Column(name = "NOTE")
    private String note;

    public void setIncomes(List<IncomeItem> incomes) {
        if (this.incomes == null) {
            this.incomes = incomes;
            initAccount(this.incomes);
        } else {
            ModelUtils.BASE_MODEL.copy(incomes, this.incomes,
                    item -> {
                        return new Key(item.getTitle());
                    },
                    item -> {
                        IncomeItem newItem = new IncomeItem();
                        newItem.setAccount(this);
                        newItem.setTitle(item.getTitle());
                        return newItem;
                    }, "itemId", "account", "title");
        }
    }

    private <T extends Item> void initAccount(List<T> items) {
        items.forEach(item -> item.setAccount(this));
    }

    public void setExpenses(List<ExpenseItem> expenses) {
        if (this.expenses == null) {
            this.expenses = expenses;
            initAccount(this.expenses);
        } else {
            ModelUtils.BASE_MODEL.copy(expenses, this.expenses,
                    item -> {
                        return new Key(item.getTitle());
                    },
                    item -> {
                        ExpenseItem newItem = new ExpenseItem();
                        newItem.setAccount(this);
                        newItem.setTitle(item.getTitle());
                        return newItem;
                    }, "itemId", "account", "title");
        }
    }
}
