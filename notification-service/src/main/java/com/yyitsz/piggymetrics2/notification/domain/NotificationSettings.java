package com.yyitsz.piggymetrics2.notification.domain;

import com.yyitsz.piggymetrics2.notification.repository.converter.FrequencyJpaConverter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Embeddable
@Setter
@Getter
@EqualsAndHashCode
public class NotificationSettings {

    @NotNull
    @Column(name = "IS_ACTIVE")
    private Boolean active;

    @NotNull
    @Column(name = "FREQUENCY")
    @Convert(converter = FrequencyJpaConverter.class)
    private Frequency frequency;

    @Column(name = "LAST_NOTIFY_TIME")
    private LocalDateTime lastNotified;

//    @Column(name = "NOTIFY_TYPE", updatable = false, insertable = false)
//    @Enumerated(EnumType.STRING)
//    private NotificationType notificationType;

}
