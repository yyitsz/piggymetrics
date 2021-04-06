package com.yyitsz.piggymetrics2.notification.repository;

import com.yyitsz.piggymetrics2.notification.domain.Recipient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RecipientRepository extends JpaRepository<Recipient, String> {

    Recipient findByAccountName(String name);

    //	@Query("{ $and: [ {'scheduledNotifications.BACKUP.active': true }, { $where: 'this.scheduledNotifications.BACKUP.lastNotified < " +
//			"new Date(new Date().setDate(new Date().getDate() - this.scheduledNotifications.BACKUP.frequency ))' }] }")
    @Query("Select distinct r from Recipient r inner join r.scheduledNotifications n " +
            " where key(n) = 'BACKUP' " +
            "  and n.active = true " +
            "  and n.lastNotified < (sysdate - n.frequency)")
    List<Recipient> findReadyForBackup();

    //	@Query("{ $and: [ {'scheduledNotifications.REMIND.active': true }, { $where: 'this.scheduledNotifications.REMIND.lastNotified < " +
//			"new Date(new Date().setDate(new Date().getDate() - this.scheduledNotifications.REMIND.frequency ))' }] }")
    @Query("Select distinct r from Recipient r inner join r.scheduledNotifications n " +
            " where key(n) = 'REMIND' " +
            "  and n.active = true " +
            "  and n.lastNotified < (sysdate - n.frequency)")
    List<Recipient> findReadyForRemind();

}
