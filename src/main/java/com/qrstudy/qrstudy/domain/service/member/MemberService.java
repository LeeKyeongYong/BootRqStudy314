package com.qrstudy.qrstudy.domain.service.member;

import com.qrstudy.qrstudy.base.rsData.RsData;
import com.qrstudy.qrstudy.domain.repository.member.MemberRepository;
import com.qrstudy.qrstudy.domain.service.genFile.GenFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.qrstudy.qrstudy.domain.entity.Member;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final GenFileService genFileService;

    @Transactional
    public RsData<Member> join(String username, String password, String nickname, MultipartFile profileImg){

        if(findByUsername(username).isPresent())
            return RsData.of("F-1","%s(은)는 사용중인 아이디 입니다.".formatted(username));

        Member member = Member
                .builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
                .build();

        member = memberRepository.save(member);

        if(profileImg!=null){
            genFileService.save(member.getModelName(),member.getId(),"common","profileImg",0,profileImg);
        }

        return RsData.of("S-1","회원가입이 완료 되었습니다.",member);
    }

    public Optional<Member> findByUsername(String username){
        return memberRepository.findByUsername(username);
    }

    public Optional<Member> findById(long id){
        return memberRepository.findById(id);
    }

    public RsData checkUsernameDup(String username){
        if(findByUsername(username).isPresent()) return RsData.of("F-1","%s(은)는 사용중인 아이디 입니다".formatted(username));

        return RsData.of("S-1","%s(은)는 사용 가능한 아이디 입니다.".formatted(username));
    }
}
