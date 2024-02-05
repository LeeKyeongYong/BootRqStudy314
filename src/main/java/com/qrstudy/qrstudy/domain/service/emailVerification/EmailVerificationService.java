package com.qrstudy.qrstudy.domain.service.emailVerification;

import com.qrstudy.qrstudy.base.app.AppConfig;
import com.qrstudy.qrstudy.base.rsData.RsData;
import com.qrstudy.qrstudy.domain.entity.member.Member;
import com.qrstudy.qrstudy.domain.service.attr.AttrService;
import com.qrstudy.qrstudy.domain.service.email.EmailService;
import com.qrstudy.qrstudy.domain.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmailVerificationService {
    private final EmailService emailService;
    private final AttrService attrService;
    @Autowired
    @Lazy
    private MemberService memberService;

    // 조회

    // 명령
    @Transactional
    public CompletableFuture<RsData> send(Member member) {
        String subject = "[%s 이메일인증] 안녕하세요 %s님. 링크를 클릭하여 회원가입을 완료해주세요."
                .formatted(
                        AppConfig.getSiteName(),
                        member.getUsername()
                );
        String body = genEmailVerificationUrl(member);

        return emailService.sendAsync(member.getEmail(), subject, body);
    }

    private String genEmailVerificationUrl(Member member) {
        return genEmailVerificationUrl(member.getId());
    }

    private String genEmailVerificationUrl(long memberId) {
        String code = genEmailVerificationCode(memberId);

        return AppConfig.getSiteBaseUrl() + "/emailVerification/verify?memberId=%d&code=%s".formatted(memberId, code);
    }

    private String genEmailVerificationCode(long memberId) {
        String code = UUID.randomUUID().toString();
        attrService.set("member__%d__extra__emailVerificationCode".formatted(memberId), code, LocalDateTime.now().plusSeconds(60 * 60));

        return code;
    }

    @Transactional
    public RsData verify(long memberId, String code) {
        RsData checkVerificationCodeValidRs = checkVerificationCodeValid(memberId, code);

        if (!checkVerificationCodeValidRs.isSuccess()) return checkVerificationCodeValidRs;

        setEmailVerified(memberId);

        return RsData.of("S-1", "이메일인증이 완료되었습니다.");
    }

    private RsData checkVerificationCodeValid(long memberId, String code) {
        String foundCode = attrService.get("member__%d__extra__emailVerificationCode".formatted(memberId), "");

        if (!foundCode.equals(code)) return RsData.of("F-1", "만료 되었거나 유효하지 않은 코드입니다.");

        return RsData.of("S-1", "인증된 코드 입니다.");
    }

    private void setEmailVerified(long memberId) {
        memberService.setEmailVerified(memberId);
    }
}