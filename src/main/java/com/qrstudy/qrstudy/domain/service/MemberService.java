package com.qrstudy.qrstudy.domain.service;

import com.qrstudy.qrstudy.base.rsData.RsData;
import com.qrstudy.qrstudy.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.qrstudy.qrstudy.domain.entity.Member;
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public RsData<Member> join(String username, String password, String nickname){

        if(findByUsername(username).isPresent())
            return RsData.of("F-1","%s(은)는 사용중인 아이디 입니다.".formatted(username));

        Member member = Member
                .builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
                .build();

        member = memberRepository.save(member);
        return RsData.of("S-1","회원가입이 완료 되었습니다.",member);
    }
    public RsData checkUsernameDup(String username){
        if(findByUsername(username).isPresent()) return RsData.of("F-1","%s(은)는 사용중인 아이디 입니다".formatted(username));

        return RsData.of("S-1","%s(은)는 사용 가능한 아이디 입니다.".formatted(username));
    }
}
