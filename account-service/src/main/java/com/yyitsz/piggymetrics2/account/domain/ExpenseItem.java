package com.yyitsz.piggymetrics2.account.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Represents normalized {@link com.yyitsz.piggymetrics2.statistics.domain.Item} object
 * with {@link Currency#getBase()} currency and {@link TimePeriod#getBase()} time period
 */

@Entity
@Table(name = "AS_ITEM"
        ,uniqueConstraints = @UniqueConstraint(name = "AS_ITEM_UK1",
                columnNames = {"AC_NAME", "ITEM_TYPE", "TITLE"})
)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "ITEM_TYPE", discriminatorType = DiscriminatorType.STRING, length = 30)
@DiscriminatorValue("EXPENSE")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class ExpenseItem extends Item {
    public ExpenseItem() {
    }

}
