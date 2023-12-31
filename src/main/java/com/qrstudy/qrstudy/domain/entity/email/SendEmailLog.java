package com.qrstudy.qrstudy.domain.entity.email;

import com.qrstudy.qrstudy.base.jpa.BaseEntity;
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
@NoArgsConstructor(access =  PROTECTED)
@SuperBuilder
@ToString(callSuper = true)
public class SendEmailLog extends BaseEntity {

    private String resultcode;
    private String message;
    private String email;
    private String subject;
    private String body;
    private LocalDateTime sendDate;
    private LocalDateTime failDate;

    public void setCompleted(RsData rs){
        this.resultcode = rs.getResultCode();
        this.message = rs.getMsg();

        if(rs.isSuccess()) this.sendDate = LocalDateTime.now();
        else this.failDate = LocalDateTime.now();
    }
}
