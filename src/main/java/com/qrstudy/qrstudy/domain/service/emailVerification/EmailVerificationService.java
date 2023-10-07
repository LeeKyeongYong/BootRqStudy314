package com.qrstudy.qrstudy.domain.service.emailVerification;

import com.qrstudy.qrstudy.base.app.AppConfig;
import com.qrstudy.qrstudy.base.rsData.RsData;
import com.qrstudy.qrstudy.domain.entity.member.Member;
import com.qrstudy.qrstudy.domain.service.attr.AttrService;
import com.qrstudy.qrstudy.domain.service.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmailVerificationService {

    private final EmailService emailService;
    private final AttrService attrService;

    public CompletableFuture<RsData> send(Member member){
        String subject="".formatted(AppConfig.getSiteName(),member.getUsername());
        String body = genEmailVerificationUrl(member);

        return emailService.send(member.getEmail(),subject,body);
    }

    public String genEmailVerificationUrl(Member member){
        return genEmailVerificationUrl(member.getId());
    }

    public String genEmailVerificationUrl(long memberId){
        String code = genEmailVerificationUrl(memberId);

        return AppConfig.getSiteBaseUrl()+"member__%d__extra__emailVerificationCode".formatted(memberId,code);
    }

    public String genEmailVerificationCode(long memberId){
        String code = UUID.randomUUID().toString();
        attrService.set("member__%d__extra__emailVerificationCode".formatted(memberId),code, LocalDateTime.now().plusSeconds(60*60*1));
        return code;
    }

    public RsData verifyVerificationCode(long memberId,String code){
        String foundCode = attrService.get("member__%d__extra__emailVerificationCode".formatted(memberId),"");

        if(foundCode.equals(code)==false){
            return RsData.of("F-1","만료되었거나 유효하지 않은 코드입니다.");
        }

        return RsData.of("S-1","인증된 코드 입니다.");
    }
}
