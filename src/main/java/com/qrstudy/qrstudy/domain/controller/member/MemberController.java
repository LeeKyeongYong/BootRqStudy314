package com.qrstudy.qrstudy.domain.controller.member;

import com.qrstudy.qrstudy.base.rq.Rq;
import com.qrstudy.qrstudy.base.rsData.RsData;
import com.qrstudy.qrstudy.domain.entity.Member;
import com.qrstudy.qrstudy.domain.service.MemberService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartRequest;

@Controller
@RequestMapping("/usr/member/")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    private final Rq rq;

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
        private MultipartRequest profileImg;
    }

    @PostMapping("/join")
    public String join(@Valid JoinForm joinForm){

        RsData<Member> joinRs =  memberService.join(joinForm.getUsername(),joinForm.getPassword(),joinForm.getNickname());

            if(joinRs.isFail()){
                return rq.historyBack(joinRs.getMsg());
            }
        return rq.redirect("/",joinRs.getMsg());
    }

    @GetMapping("checkUsernameDup")
    @ResponseBody
    public RsData checkUsernameDup(String username){
        return memberService.checkUsernameDup(username);
    }
}
