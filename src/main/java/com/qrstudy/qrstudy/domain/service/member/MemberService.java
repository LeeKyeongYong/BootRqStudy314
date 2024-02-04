package com.qrstudy.qrstudy.domain.service.member;

import com.qrstudy.qrstudy.base.app.AppConfig;
import com.qrstudy.qrstudy.base.rsData.RsData;
import com.qrstudy.qrstudy.domain.controller.genFile.GenFile;
import com.qrstudy.qrstudy.domain.entity.member.Member;
import com.qrstudy.qrstudy.domain.repository.member.MemberRepository;
import com.qrstudy.qrstudy.domain.service.attr.AttrService;
import com.qrstudy.qrstudy.domain.service.email.EmailService;
import com.qrstudy.qrstudy.domain.service.emailVerification.EmailVerificationService;
import com.qrstudy.qrstudy.domain.service.genFile.GenFileService;
import com.qrstudy.qrstudy.domain.standard.util.Ut;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberService {
    private final GenFileService genFileService;
    private final EmailService emailService;
    private final EmailVerificationService emailVerificationService;
    private final AttrService attrService;

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    // 조회
    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    public Optional<Member> findById(long id) {
        return memberRepository.findById(id);
    }

    private Optional<Member> findByProducerName(String producerName) {
        return memberRepository.findByProducerName(producerName);

    }

    public Optional<String> findProfileImgUrl(Member member) {
        return genFileService.findBy(
                        member.getModelName(), member.getId(), "common", "profileImg", 1
                )
                .map(GenFile::getUrl);
    }

    public RsData<String> checkUsernameDup(String username) {
        if (findByUsername(username).isPresent()) return RsData.of("F-1", "%s(은)는 사용중인 아이디입니다.".formatted(username));

        return RsData.of("S-1", "%s(은)는 사용 가능한 아이디 입니다.".formatted(username), username);
    }

    public RsData<String> checkEmailDup(String email) {
        if (findByEmail(email).isPresent()) return RsData.of("F-1", "%s(은)는 사용중인 이메일입니다.".formatted(email));

        return RsData.of("S-1", "%s(은)는 사용 가능한 이메일 입니다.".formatted(email), email);
    }

    public RsData<String> checkProducerNameDup(Member actor, String producerName) {
        if (producerName.equals(actor.getProducerName()))
            return RsData.of("S-1", "%s(은)는 사용 가능한 활동명입니다.".formatted(producerName), producerName);

        if (findByProducerName(producerName).isPresent())
            return RsData.of("F-1", "%s(은)는 사용중인 활동명입니다.".formatted(producerName));

        return RsData.of("S-1", "%s(은)는 사용 가능한 활동명입니다.".formatted(producerName), producerName);
    }

    // 명령
    @Transactional
    public RsData<Member> join(String username, String password, String nickname, String email, String profileImgFilePath) {
        if (findByUsername(username).isPresent())
            return RsData.of("F-1", "%s(은)는 사용중인 아이디 입니다.".formatted(username));

        if (findByEmail(email).isPresent())
            return RsData.of("F-2", "%s(은)는 사용중인 이메일 입니다.".formatted(username));

        nickname = genUniqueNicknameIfNeed(nickname);

        Member member = Member
                .builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
                .email(email)
                .build();

        memberRepository.save(member);

        if (profileImgFilePath != null) saveProfileImg(member, profileImgFilePath);

        sendJoinCompleteEmail(member);
        sendEmailVerificationEmail(member);

        return RsData.of("S-1", "회원가입이 완료되었습니다.", member);
    }

    private String genUniqueNicknameIfNeed(String nickname) {
        int appenderStrBaseLen = 3;
        int appenderStrAddiLen;
        String appendStr = "";

        int loopCount = 0;

        while (true) {
            Optional<Member> optMember = memberRepository.findByNickname(nickname + appendStr);

            if (optMember.isEmpty()) return nickname + appendStr;

            nickname = nickname.split("#", 2)[0];

            loopCount++;

            appenderStrAddiLen = loopCount / 10;

            appendStr = '#' + Ut.str.randomNumStr(appenderStrBaseLen + appenderStrAddiLen);
        }
    }

    @Transactional
    public RsData<Member> join(String username, String password, String nickname, String email, MultipartFile profileImg) {
        String profileImgFilePath = Ut.file.toFile(profileImg, AppConfig.getTempDirPath());
        return join(username, password, nickname, email, profileImgFilePath);
    }

    private void saveProfileImg(Member member, MultipartFile profileImg) {
        if (profileImg == null) return;
        if (profileImg.isEmpty()) return;

        String profileImgFilePath = Ut.file.toFile(profileImg, AppConfig.getTempDirPath());

        saveProfileImg(member, profileImgFilePath);
    }

    private void saveProfileImg(Member member, String profileImgFilePath) {
        if (Ut.str.isBlank(profileImgFilePath)) return;

        genFileService.save(member.getModelName(), member.getId(), "common", "profileImg", 1, profileImgFilePath);
    }

    private void sendJoinCompleteEmail(Member member) {
        final String email = member.getEmail();

        if (Ut.str.isBlank(email)) return;

        CompletableFuture<RsData> sendRsFuture = emailService.sendAsync(
                email,
                "[%s 가입축하] 회원가입이 완료되었습니다.".formatted(
                        AppConfig.getSiteName()
                ),
                "많은 이용 바랍니다."
        );

        sendRsFuture.whenComplete((rs, throwable) -> {
            if (rs.isFail()) {
                log.info("sendJoinCompleteMail, 메일 발송 실패 : " + email);
                return;
            }

            log.info("sendJoinCompleteMail, 메일 발송 성공 : " + email);
        });
    }

    private void sendEmailVerificationEmail(Member member) {
        if (Ut.str.isBlank(member.getEmail())) return;

        emailVerificationService.send(member);
    }

    @Transactional
    public void setEmailVerified(Long memberId) {
        attrService.set("member__%d__extra__emailVerified".formatted(memberId), true);
    }

    @Transactional
    public void setEmailVerified(Member member) {
        setEmailVerified(member.getId());
    }

    public boolean isEmailVerified(Member member) {
        if (member.isSocialMember()) return true;
        return attrService.getAsBoolean("member__%d__extra__emailVerified".formatted(member.getId()), false);
    }

    public Optional<Member> findByUsernameAndEmail(String username, String email) {
        return memberRepository.findByUsernameAndEmail(username, email);
    }

    @Transactional
    public void sendTempPasswordToEmail(Member member) {
        final String tempPassword = Ut.str.tempPassword(6);
        member.setPassword(passwordEncoder.encode(tempPassword));

        final String email = member.getEmail();

        CompletableFuture<RsData> sendRsFuture = emailService.sendAsync(
                email,
                "[%s 임시비밀번호] 임시 비밀번호 입니다.".formatted(
                        AppConfig.getSiteName()
                ),
                tempPassword
        );

        sendRsFuture.whenComplete((rs, throwable) -> {
            if (rs.isFail()) {
                log.info("sendTempPasswordToEmail, 메일 발송 실패 : " + email);
                return;
            }

            log.info("sendTempPasswordToEmail, 메일 발송 성공 : " + email);
        });
    }

    @Transactional
    public RsData<Member> modify(long memberId, String password, String nickname, MultipartFile profileImg) {
        Member member = findById(memberId).get();

        if (Ut.str.hasLength(password)) member.setPassword(passwordEncoder.encode(password));
        if (Ut.str.hasLength(nickname) && !member.getNickname().equals(nickname))
            member.setNickname(genUniqueNicknameIfNeed(nickname));
        if (profileImg != null) saveProfileImg(member, profileImg);

        return RsData.of("S-1", "회원정보가 수정되었습니다.", member);
    }

    public boolean isSamePassword(Member member, String oldPassword) {
        return passwordEncoder.matches(oldPassword, member.getPassword());
    }

    @Transactional
    public String genCheckPasswordAuthCode(Member member) {
        String code = UUID.randomUUID().toString();

        attrService.set("member__%d__extra__checkPasswordAuthCode".formatted(member.getId()), code, LocalDateTime.now().plusSeconds(60 * 30));

        return code;
    }

    public RsData<?> checkCheckPasswordAuthCode(Member member, String checkPasswordAuthCode) {
        if (Ut.str.isBlank(checkPasswordAuthCode)) return RsData.of("F-1", "checkPasswordAuthCode를 입력해주세요.");

        if (attrService.get("member__%d__extra__checkPasswordAuthCode".formatted(member.getId()), "").equals(checkPasswordAuthCode))
            return RsData.of("S-1", "유효한 코드입니다.");

        return RsData.of("F-2", "유효하지 않은 코드입니다.");
    }

    public Page<Member> findByKw(String kwType, String kw, Pageable pageable) {
        return memberRepository.findByKw(kwType, kw, pageable);
    }

    public String getProfileImgUrl(Member member) {
        return Optional.ofNullable(member)
                .flatMap(this::findProfileImgUrl)
                .orElse("https://placehold.co/30x30?text=UU");
    }

    @Transactional
    public Member whenSocialLogin(String providerTypeCode, String username, String nickname, String profileImgUrl) {
        Optional<Member> opMember = findByUsername(username);

        if (opMember.isPresent()) return opMember.get();

        String filePath = Ut.str.hasLength(profileImgUrl) ? Ut.file.downloadFileByHttp(profileImgUrl, AppConfig.getTempDirPath()) : "";

        return join(username, "", nickname, "", filePath).getData();
    }

    @Transactional
    public RsData<Member> beProducer(long memberId, String producerName) {
        memberRepository.findById(memberId)
                .ifPresent(member -> {
                    member.setProducerName(producerName);
                });

        return RsData.of("S-1", "활동명이 적용되었습니다.");
    }
}