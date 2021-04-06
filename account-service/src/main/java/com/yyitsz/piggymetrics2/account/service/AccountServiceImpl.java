package com.yyitsz.piggymetrics2.account.service;

import com.yyitsz.piggymetrics2.account.client.AuthServiceClient;
import com.yyitsz.piggymetrics2.account.client.StatisticsServiceClient;
import com.yyitsz.piggymetrics2.account.domain.Account;
import com.yyitsz.piggymetrics2.account.domain.Currency;
import com.yyitsz.piggymetrics2.account.domain.Saving;
import com.yyitsz.piggymetrics2.account.domain.User;
import com.yyitsz.piggymetrics2.account.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class AccountServiceImpl implements AccountService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private StatisticsServiceClient statisticsClient;

    @Autowired
    private AuthServiceClient authClient;

    @Autowired
    private AccountRepository repository;

    /**
     * {@inheritDoc}
     */
    @Override
    public Account findByName(String accountName) {
        Assert.hasLength(accountName, "accountName is required.");
        return repository.findByName(accountName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Account create(User user) {

        Account existing = repository.findByName(user.getUsername());
        Assert.isNull(existing, "account already exists: " + user.getUsername());

        authClient.createUser(user);

        Saving saving = new Saving();
        saving.setAmount(new BigDecimal(0));
        saving.setCurrency(Currency.getDefault());
        saving.setInterest(new BigDecimal(0));
        saving.setDeposit(false);
        saving.setCapitalization(false);

        Account account = new Account();
        account.setName(user.getUsername());
        account.setLastSeen(new Date());
        account.setSaving(saving);

        repository.save(account);

        log.info("new account has been created: " + account.getName());

        return account;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveChanges(String name, Account update) {

        Account account = repository.findByName(name);
        if (account == null) {
            log.error("can't find account with name {}", name);
            //todo: remove it
            account = new Account();
            account.setName(name);
        }

        account.setIncomes(update.getIncomes());
        account.setExpenses(update.getExpenses());
        account.setSaving(update.getSaving());
        account.setNote(update.getNote());
        account.setLastSeen(new Date());
        repository.save(account);

        log.debug("account {} changes has been saved", name);

        statisticsClient.updateStatistics(name, account);
    }
}
