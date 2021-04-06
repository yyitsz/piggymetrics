package com.yyitsz.piggymetrics2.notification.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.yyitsz.piggymetrics2.notification.domain.Frequency;
import com.yyitsz.piggymetrics2.notification.domain.NotificationSettings;
import com.yyitsz.piggymetrics2.notification.domain.NotificationType;
import com.yyitsz.piggymetrics2.notification.domain.Recipient;
import com.yyitsz.piggymetrics2.notification.service.RecipientService;
import com.sun.security.auth.UserPrincipal;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
@SpringBootTest
public class RecipientControllerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @InjectMocks
    private RecipientController recipientController;

    @Mock
    private RecipientService recipientService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(recipientController).build();
    }

    @Test
    public void shouldSaveCurrentRecipientSettings() throws Exception {

        Recipient recipient = getStubRecipient();
        String json = mapper.writeValueAsString(recipient);

        mockMvc.perform(put("/recipients/current").principal(new UserPrincipal(recipient.getAccountName())).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldGetCurrentRecipientSettings() throws Exception {

        Recipient recipient = getStubRecipient();
        when(recipientService.findByAccountName(recipient.getAccountName())).thenReturn(recipient);

        mockMvc.perform(get("/recipients/current").principal(new UserPrincipal(recipient.getAccountName())).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.accountName").value(recipient.getAccountName()))
                .andExpect(status().isOk());
    }

    private Recipient getStubRecipient() {

        NotificationSettings remind = new NotificationSettings();
        remind.setActive(true);
        remind.setFrequency(Frequency.WEEKLY);
        remind.setLastNotified(null);

        NotificationSettings backup = new NotificationSettings();
        backup.setActive(false);
        backup.setFrequency(Frequency.MONTHLY);
        backup.setLastNotified(null);

        Recipient recipient = new Recipient();
        recipient.setAccountName("test");
        recipient.setEmail("test@test.com");
        recipient.setScheduledNotifications(ImmutableMap.of(
                NotificationType.BACKUP, backup,
                NotificationType.REMIND, remind
        ));

        return recipient;
    }
}