package com.qrstudy.qrstudy.domain.entity.email;

import com.qrstudy.qrstudy.base.jpa.baseEntity.BaseEntity;
import com.qrstudy.qrstudy.base.rsData.RsData;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Setter
@Getter
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@SuperBuilder
@ToString(callSuper = true)
public class SendEmailLog extends BaseEntity {
    private String resultCode;
    private String message;
    private String email;
    private String subject;
    private String body;
    private LocalDateTime sendEndDate;
    private LocalDateTime failDate;

    public void setCompleted(RsData rs) {
        this.resultCode = rs.getResultCode();
        this.message = rs.getMsg();

        if (rs.isSuccess()) this.sendEndDate = LocalDateTime.now();
        else this.failDate = LocalDateTime.now();
    }
}