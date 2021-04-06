package com.yyitsz.piggymetrics2.account.repository;

import com.yyitsz.piggymetrics2.account.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest
public class AccountRepositoryTest {

    @Autowired
    private AccountRepository repository;

    @Test
    public void shouldFindAccountByName() {

        Account stub = getStubAccount();
        repository.save(stub);

        Account found = repository.findByName(stub.getName());
        assertEquals(stub.getLastSeen(), found.getLastSeen());
        assertEquals(stub.getNote(), found.getNote());
        assertEquals(stub.getIncomes().size(), found.getIncomes().size());
        assertEquals(stub.getExpenses().size(), found.getExpenses().size());
    }

    private Account getStubAccount() {

        Saving saving = new Saving();
        saving.setAmount(new BigDecimal(1500));
        saving.setCurrency(Currency.USD);
        saving.setInterest(new BigDecimal("3.32"));
        saving.setDeposit(true);
        saving.setCapitalization(false);

        ExpenseItem vacation = new ExpenseItem();
        vacation.setTitle("Vacation");
        vacation.setAmount(new BigDecimal(3400));
        vacation.setCurrency(Currency.EUR);
        vacation.setPeriod(TimePeriod.YEAR);
        vacation.setIcon("tourism");

        ExpenseItem grocery = new ExpenseItem();
        grocery.setTitle("Grocery");
        grocery.setAmount(new BigDecimal(10));
        grocery.setCurrency(Currency.USD);
        grocery.setPeriod(TimePeriod.DAY);
        grocery.setIcon("meal");

        IncomeItem salary = new IncomeItem();
        salary.setTitle("Salary");
        salary.setAmount(new BigDecimal(9100));
        salary.setCurrency(Currency.USD);
        salary.setPeriod(TimePeriod.MONTH);
        salary.setIcon("wallet");

        Account account = new Account();
        account.setName("test");
        account.setNote("test note");
        account.setLastSeen(new Date());
        account.setSaving(saving);
        account.setExpenses(Arrays.asList(grocery, vacation));
        account.setIncomes(Arrays.asList(salary));

        return account;
    }
}
