package com.yyitsz.piggymetrics2.account.service;

import com.yyitsz.piggymetrics2.account.client.AuthServiceClient;
import com.yyitsz.piggymetrics2.account.client.StatisticsServiceClient;
import com.yyitsz.piggymetrics2.account.domain.*;
import com.yyitsz.piggymetrics2.account.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @InjectMocks
    private AccountServiceImpl accountService;

    @Mock
    private StatisticsServiceClient statisticsClient;

    @Mock
    private AuthServiceClient authClient;

    @Mock
    private AccountRepository repository;


    @Test
    public void shouldFindByName() {

        final Account account = new Account();
        account.setName("test");

        when(accountService.findByName(account.getName())).thenReturn(account);
        Account found = accountService.findByName(account.getName());

        assertEquals(account, found);
    }

    @Test
    public void shouldFailWhenNameIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> accountService.findByName(""));
    }

    @Test
    public void shouldCreateAccountWithGivenUser() {

        User user = new User();
        user.setUsername("test");

        Account account = accountService.create(user);

        assertEquals(user.getUsername(), account.getName());
        assertEquals(0, account.getSaving().getAmount().intValue());
        assertEquals(Currency.getDefault(), account.getSaving().getCurrency());
        assertEquals(0, account.getSaving().getInterest().intValue());
        assertEquals(false, account.getSaving().getDeposit());
        assertEquals(false, account.getSaving().getCapitalization());
        assertNotNull(account.getLastSeen());

        //verify(authClient, times(1)).createUser(user);
        verify(repository, times(1)).save(account);
    }

    @Test
    public void shouldSaveChangesWhenUpdatedAccountGiven() {

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

        Saving saving = new Saving();
        saving.setAmount(new BigDecimal(1500));
        saving.setCurrency(Currency.USD);
        saving.setInterest(new BigDecimal("3.32"));
        saving.setDeposit(true);
        saving.setCapitalization(false);

        final Account update = new Account();
        update.setName("test");
        update.setNote("test note");
        update.setIncomes(Arrays.asList(salary));
        update.setExpenses(Arrays.asList(grocery));
        update.setSaving(saving);

        final Account account = new Account();

        when(accountService.findByName("test")).thenReturn(account);
        accountService.saveChanges("test", update);

        assertEquals(update.getNote(), account.getNote());
        assertNotNull(account.getLastSeen());

        assertEquals(update.getSaving().getAmount(), account.getSaving().getAmount());
        assertEquals(update.getSaving().getCurrency(), account.getSaving().getCurrency());
        assertEquals(update.getSaving().getInterest(), account.getSaving().getInterest());
        assertEquals(update.getSaving().getDeposit(), account.getSaving().getDeposit());
        assertEquals(update.getSaving().getCapitalization(), account.getSaving().getCapitalization());

        assertEquals(update.getExpenses().size(), account.getExpenses().size());
        assertEquals(update.getIncomes().size(), account.getIncomes().size());

        assertEquals(update.getExpenses().get(0).getTitle(), account.getExpenses().get(0).getTitle());
        assertEquals(0, update.getExpenses().get(0).getAmount().compareTo(account.getExpenses().get(0).getAmount()));
        assertEquals(update.getExpenses().get(0).getCurrency(), account.getExpenses().get(0).getCurrency());
        assertEquals(update.getExpenses().get(0).getPeriod(), account.getExpenses().get(0).getPeriod());
        assertEquals(update.getExpenses().get(0).getIcon(), account.getExpenses().get(0).getIcon());

        assertEquals(update.getIncomes().get(0).getTitle(), account.getIncomes().get(0).getTitle());
        assertEquals(0, update.getIncomes().get(0).getAmount().compareTo(account.getIncomes().get(0).getAmount()));
        assertEquals(update.getIncomes().get(0).getCurrency(), account.getIncomes().get(0).getCurrency());
        assertEquals(update.getIncomes().get(0).getPeriod(), account.getIncomes().get(0).getPeriod());
        assertEquals(update.getIncomes().get(0).getIcon(), account.getIncomes().get(0).getIcon());

        verify(repository, times(1)).save(account);
        verify(statisticsClient, times(1)).updateStatistics("test", account);
    }

    //@Test
    public void shouldFailWhenNoAccountsExistedWithGivenName() {
        final Account update = new Account();
        update.setIncomes(Arrays.asList(new IncomeItem()));
        update.setExpenses(Arrays.asList(new ExpenseItem()));

        when(accountService.findByName("test")).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> accountService.saveChanges("test", update));
    }
}
