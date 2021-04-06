package com.yyitsz.piggymetrics2.notification.service;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.yyitsz.piggymetrics2.notification.domain.Frequency;
import com.yyitsz.piggymetrics2.notification.domain.NotificationSettings;
import com.yyitsz.piggymetrics2.notification.domain.NotificationType;
import com.yyitsz.piggymetrics2.notification.domain.Recipient;
import com.yyitsz.piggymetrics2.notification.repository.RecipientRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class RecipientServiceImplTest {

    @InjectMocks
    private RecipientServiceImpl recipientService;

    @Mock
    private RecipientRepository repository;


    @Test
    public void shouldFindByAccountName() {
        Recipient recipient = new Recipient();
        recipient.setAccountName("test");

        when(repository.findByAccountName(recipient.getAccountName())).thenReturn(recipient);
        Recipient found = recipientService.findByAccountName(recipient.getAccountName());

        assertEquals(recipient, found);
    }

    @Test
    public void shouldFailToFindRecipientWhenAccountNameIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> recipientService.findByAccountName(""));
    }

    @Test
    public void shouldSaveRecipient() {

        NotificationSettings remind = new NotificationSettings();
        remind.setActive(true);
        remind.setFrequency(Frequency.WEEKLY);
        remind.setLastNotified(null);

        NotificationSettings backup = new NotificationSettings();
        backup.setActive(false);
        backup.setFrequency(Frequency.MONTHLY);
        backup.setLastNotified(LocalDateTime.now());

        Recipient recipient = new Recipient();
        recipient.setEmail("test@test.com");
        recipient.setScheduledNotifications(ImmutableMap.of(
                NotificationType.BACKUP, backup,
                NotificationType.REMIND, remind
        ));

        Recipient saved = recipientService.save("test", recipient);

        verify(repository).save(recipient);
        assertNotNull(saved.getScheduledNotifications().get(NotificationType.REMIND).getLastNotified());
        assertEquals("test", saved.getAccountName());
    }

    @Test
    public void shouldFindReadyToNotifyWhenNotificationTypeIsBackup() {
        final List<Recipient> recipients = ImmutableList.of(new Recipient());
        when(repository.findReadyForBackup()).thenReturn(recipients);

        List<Recipient> found = recipientService.findReadyToNotify(NotificationType.BACKUP);
        assertEquals(recipients, found);
    }

    @Test
    public void shouldFindReadyToNotifyWhenNotificationTypeIsRemind() {
        final List<Recipient> recipients = ImmutableList.of(new Recipient());
        when(repository.findReadyForRemind()).thenReturn(recipients);

        List<Recipient> found = recipientService.findReadyToNotify(NotificationType.REMIND);
        assertEquals(recipients, found);
    }

    @Test
    public void shouldMarkAsNotified() {

        NotificationSettings remind = new NotificationSettings();
        remind.setActive(true);
        remind.setFrequency(Frequency.WEEKLY);
        remind.setLastNotified(null);

        Recipient recipient = new Recipient();
        recipient.setAccountName("test");
        recipient.setEmail("test@test.com");
        recipient.setScheduledNotifications(ImmutableMap.of(
                NotificationType.REMIND, remind
        ));

        recipientService.markNotified(NotificationType.REMIND, recipient);
        assertNotNull(recipient.getScheduledNotifications().get(NotificationType.REMIND).getLastNotified());
        verify(repository).save(recipient);
    }
}