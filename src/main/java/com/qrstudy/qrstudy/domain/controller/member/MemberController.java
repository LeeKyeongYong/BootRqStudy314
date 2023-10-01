package com.qrstudy.qrstudy.domain.controller.member;

import com.qrstudy.qrstudy.base.rsData.RsData;
import com.qrstudy.qrstudy.domain.entity.Member;
import com.qrstudy.qrstudy.domain.service.MemberService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.qrstudy.qrstudy.domain.standard.util.Ut;

@Controller
@RequestMapping("/usr/member/")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/join")
    public String showJoin(){
        return "usr/member/join";
    }

    @Getter
    @AllArgsConstructor
    public static class JoinForm{
        @NotBlank
        private String username;
        @NotBlank
        private String nickname;
        @NotBlank
        private String password;
    }

    @PostMapping("/join")
    public String join(@Valid JoinForm joinForm){
        RsData<Member> joinRs =  memberService.join(joinForm.getUsername(),joinForm.getPassword(),joinForm.getUsername());
            if(joinRs.isFail()){
                return "redirect:/usr/member/join?failMsg="+Ut.url.encode(joinRs.getMsg());
            }
        return "redirect:/";
    }
}
