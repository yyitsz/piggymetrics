package com.yyitsz.piggymetrics2.notification.service;

import com.yyitsz.piggymetrics2.notification.domain.NotificationType;
import com.yyitsz.piggymetrics2.notification.domain.Recipient;

import javax.mail.MessagingException;
import java.io.IOException;

public interface EmailService {

    void send(NotificationType type, Recipient recipient, String attachment) throws MessagingException, IOException;

}
