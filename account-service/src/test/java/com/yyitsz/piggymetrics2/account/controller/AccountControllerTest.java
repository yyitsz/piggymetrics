package com.yyitsz.piggymetrics2.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.security.auth.UserPrincipal;
import com.yyitsz.piggymetrics2.account.domain.*;
import com.yyitsz.piggymetrics2.account.service.AccountService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Date;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
@SpringBootTest
public class AccountControllerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @InjectMocks
    private AccountController accountController;

    @Mock
    private AccountService accountService;

    private MockMvc mockMvc;


    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
    }


    @Test
    public void shouldGetAccountByName() throws Exception {

        final Account account = new Account();
        account.setName("test");

        when(accountService.findByName(account.getName())).thenReturn(account);

        mockMvc.perform(get("/" + account.getName()).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.name").value(account.getName()))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldGetCurrentAccount() throws Exception {

        final Account account = new Account();
        account.setName("test");

        when(accountService.findByName(account.getName())).thenReturn(account);

        mockMvc.perform(get("/current").principal(new UserPrincipal(account.getName())).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.name").value(account.getName()))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldSaveCurrentAccount() throws Exception {

        Saving saving = new Saving();
        saving.setAmount(new BigDecimal(1500));
        saving.setCurrency(Currency.USD);
        saving.setInterest(new BigDecimal("3.32"));
        saving.setDeposit(true);
        saving.setCapitalization(false);

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

        final Account account = new Account();
        account.setName("test");
        account.setNote("test note");
        account.setLastSeen(new Date());
        account.setSaving(saving);
        account.setExpenses(Lists.newArrayList(grocery));
        account.setIncomes(Lists.newArrayList(salary));

        String json = mapper.writeValueAsString(account);

        mockMvc.perform(put("/current").principal(new UserPrincipal(account.getName())).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldFailOnValidationTryingToSaveCurrentAccount() throws Exception {

        final Account account = new Account();
        account.setName("test");

        String json = mapper.writeValueAsString(account);

        mockMvc.perform(put("/current").principal(new UserPrincipal(account.getName())).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldRegisterNewAccount() throws Exception {

        final User user = new User();
        user.setUsername("test");
        user.setPassword("password");

        String json = mapper.writeValueAsString(user);
        System.out.println(json);
        mockMvc.perform(post("/").principal(new UserPrincipal("test")).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldFailOnValidationTryingToRegisterNewAccount() throws Exception {

        final User user = new User();
        user.setUsername("t");

        String json = mapper.writeValueAsString(user);

        mockMvc.perform(post("/").principal(new UserPrincipal("test")).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
    }
}
