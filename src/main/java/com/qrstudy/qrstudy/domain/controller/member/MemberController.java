package com.qrstudy.qrstudy.domain.controller.member;

import com.qrstudy.qrstudy.base.rsData.RsData;
import com.qrstudy.qrstudy.domain.entity.Member;
import com.qrstudy.qrstudy.domain.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
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
    public String join(@Valid JoinForm joinForm, HttpServletRequest req){

        RsData<Member> joinRs =  memberService.join(joinForm.getUsername(),joinForm.getPassword(),joinForm.getUsername(),joinForm.getNickname());

            if(joinRs.isFail()){
                req.setAttribute("msg",joinRs.getMsg());
                return "common/js";
            }
        return "redirect:/?msg="+Ut.url.encode(joinRs.getMsg());
    }
}
