package com.yyitsz.piggymetrics2.notification.domain;


import com.yyitsz.piggymetrics2.common.domain.BaseModel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Map;

//@Document(collection = "recipients")
@Entity
@Table(name = "NS_RECIPIENT")
@Slf4j
@Setter
@Getter
@EqualsAndHashCode
public class Recipient extends BaseModel {

    @Id
    @Column(name = "AC_NAME")
    private String accountName;

    @NotNull
    @Email
    @Column(name = "EMAIL")
    private String email;

    @Valid
    @ElementCollection
    @CollectionTable(name = "NS_RECIPIENT_NS_TYPE", joinColumns = @JoinColumn(name = "AC_NAME"))
    @MapKeyColumn(name = "NOTIFY_TYPE")
    @MapKeyEnumerated(EnumType.STRING)
    private Map<NotificationType, NotificationSettings> scheduledNotifications;


    @Override
    public String toString() {
        return "Recipient{" +
                "accountName='" + accountName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
