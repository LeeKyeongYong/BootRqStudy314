package com.qrstudy.qrstudy.domain.service.email;

import com.qrstudy.qrstudy.base.rsData.RsData;
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

    private final JavaMailSender mailSender;

    @Async
    public CompletableFuture<RsData> send(String to, String subject, String body){
        if(to.endsWith("@test.com")) return CompletableFuture.supplyAsync(()-> RsData.of("S-2","메일이 발송 되었습니다."));
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try{
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,false,"UTF-8");
            mimeMessageHelper.setTo(to); //메일수신자
            mimeMessageHelper.setSubject(subject); //메일제목
            mimeMessageHelper.setText(body,true);//메일 본문내용,Html 여부
            mailSender.send(mimeMessage);
        }catch(MessagingException e){
            return CompletableFuture.supplyAsync( () -> RsData.of("F-1","메일이 발송 되지 않았습니다."));
        }
        return CompletableFuture.supplyAsync( () -> RsData.of("S-1","메일이 발송되었습니다."));
    }
}
