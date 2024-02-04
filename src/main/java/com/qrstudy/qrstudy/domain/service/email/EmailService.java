package com.qrstudy.qrstudy.domain.service.email;

import com.qrstudy.qrstudy.base.rsData.RsData;
import com.qrstudy.qrstudy.domain.entity.email.SendEmailLog;
import com.qrstudy.qrstudy.domain.repository.email.SendEmailLogRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmailService {
    private final SendEmailLogRepository emailLogRepository;
    private final JavaMailSender mailSender;

    @Async
    @Transactional
    public CompletableFuture<RsData> sendAsync(String to, String subject, String body) {
        return CompletableFuture.completedFuture(send(to, subject, body));
    }

    public RsData<?> send(String to, String subject, String body) {
        SendEmailLog sendEmailLog = saveSendEmailLog(to, subject, body);

        if (isTestEmail(to)) {
            RsData<?> rs = RsData.of("S-2", "메일이 발송되었습니다.(테스트 메일)");
            setCompleted(sendEmailLog.getId(), rs);
            return rs;
        }

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(to); // 메일 수신자
            mimeMessageHelper.setSubject(subject); // 메일 제목
            mimeMessageHelper.setText(body, true); // 메일 본문 내용, HTML 여부
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            RsData<?> rs = RsData.of("F-1", "메일이 발송되지 않았습니다.");
            setCompleted(sendEmailLog.getId(), rs);
            return rs;
        }

        RsData<?> rs = RsData.of("S-1", "메일이 발송되었습니다.");
        setCompleted(sendEmailLog.getId(), rs);
        return rs;
    }

    private boolean isTestEmail(String email) {
        return email.endsWith("@test.com");
    }

    private void setCompleted(Long id, RsData rs) {
        SendEmailLog sendEmailLog = emailLogRepository.findById(id).orElseThrow();
        sendEmailLog.setCompleted(rs);
    }

    private SendEmailLog saveSendEmailLog(String to, String subject, String body) {
        SendEmailLog sendEmailLog = SendEmailLog
                .builder()
                .email(to)
                .subject(subject)
                .body(body)
                .build();

        return emailLogRepository.save(sendEmailLog);
    }
}