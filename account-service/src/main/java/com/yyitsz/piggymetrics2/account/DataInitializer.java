package com.yyitsz.piggymetrics2.account;


import com.yyitsz.piggymetrics2.account.domain.*;
import com.yyitsz.piggymetrics2.account.service.AccountService;
import org.springframework.beans.BeansException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;

@Component
public class DataInitializer implements ApplicationContextAware, ApplicationListener<ApplicationReadyEvent> {
    ApplicationContext applicationContext;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

        AccountService accountService = this.applicationContext.getBean(AccountService.class);
        User user = new User();
        user.setUsername("yyi");
        accountService.create(user);

        Account account = new Account();
        Saving saving = new Saving();
        saving.setAmount(new BigDecimal("100"));
        saving.setCapitalization(true);
        saving.setCurrency(Currency.EUR);
        saving.setDeposit(true);
        saving.setInterest(new BigDecimal("0"));

        account.setSaving(saving);

        ArrayList<Item> incomes = new ArrayList<>();
        Item item = new Item();
        item.setTitle("ABC");
        item.setAmount(new BigDecimal("100"));
        item.setCurrency(Currency.EUR);
        item.setPeriod(TimePeriod.DAY);
        incomes.add(item);
        account.setIncomes(incomes);

        ArrayList<Item> expenses = new ArrayList<>();
        Item item1 = new Item();
        item1.setAmount(new BigDecimal(200));
        item1.setCurrency(Currency.USD);
        item1.setPeriod(TimePeriod.HOUR);
        item1.setTitle("Buy xxx");
        expenses.add(item1);
        account.setExpenses(expenses);

        accountService.saveChanges("yyi", account);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
