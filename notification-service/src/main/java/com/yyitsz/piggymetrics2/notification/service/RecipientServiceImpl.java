package com.yyitsz.piggymetrics2.notification.service;

import com.yyitsz.piggymetrics2.notification.domain.NotificationType;
import com.yyitsz.piggymetrics2.notification.domain.Recipient;
import com.yyitsz.piggymetrics2.notification.repository.RecipientRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class RecipientServiceImpl implements RecipientService {

    @Autowired
    private RecipientRepository repository;

    @Override
    public Recipient findByAccountName(String accountName) {
        Assert.hasLength(accountName, "accountName is required.");
        return repository.findByAccountName(accountName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Recipient save(String accountName, Recipient recipient) {

        recipient.setAccountName(accountName);
        recipient.getScheduledNotifications().values()
                .forEach(settings -> {
                    if (settings.getLastNotified() == null) {
                        settings.setLastNotified(LocalDateTime.now());
                    }
                });

        repository.save(recipient);

        log.info("recipient {} settings has been updated", recipient);

        return recipient;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Recipient> findReadyToNotify(NotificationType type) {
        switch (type) {
            case BACKUP:
                return repository.findReadyForBackup();
            case REMIND:
                return repository.findReadyForRemind();
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void markNotified(NotificationType type, Recipient recipient) {
        recipient.getScheduledNotifications().get(type).setLastNotified(LocalDateTime.now());
        repository.save(recipient);
    }
}
