package com.yyitsz.piggymetrics2.statistics.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.sun.security.auth.UserPrincipal;
import com.yyitsz.piggymetrics2.statistics.domain.*;
import com.yyitsz.piggymetrics2.statistics.domain.timeseries.DataPoint;
import com.yyitsz.piggymetrics2.statistics.service.StatisticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@ExtendWith(SpringExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
@SpringBootTest
public class StatisticsControllerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @InjectMocks
    private StatisticsController statisticsController;

    @Mock
    private StatisticsService statisticsService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(statisticsController).build();
    }

    @Test
    public void shouldGetStatisticsByAccountName() throws Exception {

        final DataPoint dataPoint = new DataPoint();
        dataPoint.setAccountName("test");
        dataPoint.setDate(LocalDate.now());

        when(statisticsService.findByAccountName(dataPoint.getAccountName()))
                .thenReturn(ImmutableList.of(dataPoint));

        mockMvc.perform(get("/test").principal(new UserPrincipal(dataPoint.getAccountName())).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$[0].accountName").value(dataPoint.getAccountName()))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldGetCurrentAccountStatistics() throws Exception {

        final DataPoint dataPoint = new DataPoint();
        dataPoint.setAccountName("test");
        dataPoint.setDate(LocalDate.now());

        when(statisticsService.findByAccountName(dataPoint.getAccountName()))
                .thenReturn(ImmutableList.of(dataPoint));

        mockMvc.perform(get("/current").principal(new UserPrincipal(dataPoint.getAccountName())).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$[0].accountName").value(dataPoint.getAccountName()))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldSaveAccountStatistics() throws Exception {

        Saving saving = new Saving();
        saving.setAmount(new BigDecimal(1500));
        saving.setCurrency(Currency.USD);
        saving.setInterest(new BigDecimal("3.32"));
        saving.setDeposit(true);
        saving.setCapitalization(false);

        Item grocery = new Item();
        grocery.setTitle("Grocery");
        grocery.setAmount(new BigDecimal(10));
        grocery.setCurrency(Currency.USD);
        grocery.setPeriod(TimePeriod.DAY);

        Item salary = new Item();
        salary.setTitle("Salary");
        salary.setAmount(new BigDecimal(9100));
        salary.setCurrency(Currency.USD);
        salary.setPeriod(TimePeriod.MONTH);

        final Account account = new Account();
        account.setSaving(saving);
        account.setExpenses(ImmutableList.of(grocery));
        account.setIncomes(ImmutableList.of(salary));

        String json = mapper.writeValueAsString(account);

        mockMvc.perform(put("/test").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());

        verify(statisticsService, times(1)).save(anyString(), any(Account.class));
    }
}