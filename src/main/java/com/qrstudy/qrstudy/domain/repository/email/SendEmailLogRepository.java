package com.qrstudy.qrstudy.domain.repository.email;

import com.qrstudy.qrstudy.domain.entity.email.SendEmailLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SendEmailLogRepository extends JpaRepository<SendEmailLog,Long> {
}
