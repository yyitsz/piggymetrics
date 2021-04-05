package com.yyitsz.piggymetrics2.statistics;

import com.yyitsz.piggymetrics2.statistics.domain.*;
import com.yyitsz.piggymetrics2.statistics.service.StatisticsService;
import org.springframework.beans.BeansException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;

//@Component
public class DataInitializer implements ApplicationContextAware, ApplicationListener<ApplicationReadyEvent> {
    ApplicationContext applicationContext;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
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

        account.setExpenses(new ArrayList<>());

        StatisticsService statisticsService = this.applicationContext.getBean(StatisticsService.class);
        statisticsService.save("yyi", account);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
