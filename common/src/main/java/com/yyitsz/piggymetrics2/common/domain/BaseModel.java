package com.yyitsz.piggymetrics2.common.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
@Setter
@Getter
public class BaseModel implements Serializable {
    @Column(name = "CREATE_TIME")
    private LocalDateTime createTime;
    @Column(name = "UPDATE_TIME")
    private LocalDateTime updateTime;
    @Column(name = "VER_NO")
    @Version
    private Long version;
    @Column(name = "CREATE_BY")
    private String createBy;
    @Column(name = "UPDATE_BY")
    private String updatedBy;

    @PrePersist
    public void updateTimeWhenCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            createBy = authentication.getName();
            updatedBy = authentication.getName();
        }
    }

    @PreUpdate
    public void updateTimeWhenUpdate() {
        if (createTime == null) {
            createTime = LocalDateTime.now();
        }
        updateTime = LocalDateTime.now();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            if (createBy == null || createBy.length() == 0) {
                createBy = authentication.getName();
            }
            updatedBy = authentication.getName();
        }
    }
}
