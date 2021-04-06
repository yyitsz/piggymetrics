package com.yyitsz.piggymetrics2.notification.repository;

import com.google.common.collect.ImmutableMap;
import com.yyitsz.piggymetrics2.notification.domain.Frequency;
import com.yyitsz.piggymetrics2.notification.domain.NotificationSettings;
import com.yyitsz.piggymetrics2.notification.domain.NotificationType;
import com.yyitsz.piggymetrics2.notification.domain.Recipient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RecipientRepositoryTest {

    @Autowired
    private RecipientRepository repository;
    @Autowired
    private TestEntityManager entityManager;
    @Test
    public void shouldFindByAccountName() {

        NotificationSettings remind = new NotificationSettings();
        remind.setActive(true);
        remind.setFrequency(Frequency.WEEKLY);
        remind.setLastNotified(LocalDateTime.now());

        NotificationSettings backup = new NotificationSettings();
        backup.setActive(false);
        backup.setFrequency(Frequency.MONTHLY);
        backup.setLastNotified(LocalDateTime.now());

        Recipient recipient = new Recipient();
        recipient.setAccountName("test");
        recipient.setEmail("test@test.com");
        recipient.setScheduledNotifications(ImmutableMap.of(
                NotificationType.BACKUP, backup,
                NotificationType.REMIND, remind
        ));

        repository.save(recipient);

        Recipient found = repository.findByAccountName(recipient.getAccountName());
        assertEquals(recipient.getAccountName(), found.getAccountName());
        assertEquals(recipient.getEmail(), found.getEmail());

        assertEquals(recipient.getScheduledNotifications().get(NotificationType.BACKUP).getActive(),
                found.getScheduledNotifications().get(NotificationType.BACKUP).getActive());
        assertEquals(recipient.getScheduledNotifications().get(NotificationType.BACKUP).getFrequency(),
                found.getScheduledNotifications().get(NotificationType.BACKUP).getFrequency());
        assertEquals(recipient.getScheduledNotifications().get(NotificationType.BACKUP).getLastNotified(),
                found.getScheduledNotifications().get(NotificationType.BACKUP).getLastNotified());

        assertEquals(recipient.getScheduledNotifications().get(NotificationType.REMIND).getActive(),
                found.getScheduledNotifications().get(NotificationType.REMIND).getActive());
        assertEquals(recipient.getScheduledNotifications().get(NotificationType.REMIND).getFrequency(),
                found.getScheduledNotifications().get(NotificationType.REMIND).getFrequency());
        assertEquals(recipient.getScheduledNotifications().get(NotificationType.REMIND).getLastNotified(),
                found.getScheduledNotifications().get(NotificationType.REMIND).getLastNotified());
    }

    @Test
    public void shouldFindReadyForRemindWhenFrequencyIsWeeklyAndLastNotifiedWas8DaysAgo() {

        NotificationSettings remind = new NotificationSettings();
        remind.setActive(true);
        remind.setFrequency(Frequency.WEEKLY);
        remind.setLastNotified(LocalDateTime.now().plusDays(-8));

        Recipient recipient = new Recipient();
        recipient.setAccountName("test");
        recipient.setEmail("test@test.com");
        recipient.setScheduledNotifications(ImmutableMap.of(
                NotificationType.REMIND, remind
        ));

        repository.save(recipient);

        List<Recipient> found = repository.findReadyForRemind();
        assertFalse(found.isEmpty());
    }

    @Test
    public void shouldNotFindReadyForRemindWhenFrequencyIsWeeklyAndLastNotifiedWasYesterday() {

        NotificationSettings remind = new NotificationSettings();
        remind.setActive(true);
        remind.setFrequency(Frequency.WEEKLY);
        //remind.setFrequency(Frequency.MONTHLY);
        remind.setLastNotified(LocalDateTime.now().plusDays(-1));

        Recipient recipient = new Recipient();
        recipient.setAccountName("test");
        recipient.setEmail("test@test.com");
        recipient.setScheduledNotifications(ImmutableMap.of(
                NotificationType.REMIND, remind
        ));

        repository.save(recipient);
        repository.flush();
        entityManager.clear();
        List<Recipient> found = repository.findReadyForRemind();
        assertTrue(found.isEmpty());
    }

    @Test
    public void shouldNotFindReadyForRemindWhenNotificationIsNotActive() {

        NotificationSettings remind = new NotificationSettings();
        remind.setActive(false);
        remind.setFrequency(Frequency.WEEKLY);
        remind.setLastNotified(LocalDateTime.now().plusDays(-30));

        Recipient recipient = new Recipient();
        recipient.setAccountName("test");
        recipient.setEmail("test@test.com");
        recipient.setScheduledNotifications(ImmutableMap.of(
                NotificationType.REMIND, remind
        ));

        repository.save(recipient);

        List<Recipient> found = repository.findReadyForRemind();
        assertTrue(found.isEmpty());
    }

    @Test
    public void shouldNotFindReadyForBackupWhenFrequencyIsQuaterly() {

        NotificationSettings remind = new NotificationSettings();
        remind.setActive(true);
        remind.setFrequency(Frequency.QUARTERLY);
        remind.setLastNotified(LocalDateTime.now().plusDays(-91));

        Recipient recipient = new Recipient();
        recipient.setAccountName("test");
        recipient.setEmail("test@test.com");
        recipient.setScheduledNotifications(ImmutableMap.of(
                NotificationType.BACKUP, remind
        ));

        repository.save(recipient);

        List<Recipient> found = repository.findReadyForBackup();
        assertFalse(found.isEmpty());
    }
}