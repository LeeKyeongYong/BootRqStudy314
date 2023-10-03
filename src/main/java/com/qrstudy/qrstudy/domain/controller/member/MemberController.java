package com.qrstudy.qrstudy.domain.controller.member;

import com.qrstudy.qrstudy.base.rq.Rq;
import com.qrstudy.qrstudy.base.rsData.RsData;
import com.qrstudy.qrstudy.domain.entity.Member;
import com.qrstudy.qrstudy.domain.service.member.MemberService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/usr/member/")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    private final Rq rq;

    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    public String showLogin(){
        return "user/member/login";
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/join")
    public String showJoin(){
        return "usr/member/join";
    }

    @Getter
    @AllArgsConstructor
    @ToString
    public static class JoinForm{
        @NotBlank
        private String username;
        @NotBlank
        private String nickname;
        @NotBlank
        private String password;
        @NotBlank
        private String email;
        private MultipartFile profileImg;
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/join")
    public String join(@Valid JoinForm joinForm){

        System.out.println("joinForm: "+joinForm);
        RsData<Member> joinRs =  memberService.join(joinForm.getUsername(),joinForm.getPassword(),joinForm.getNickname(),joinForm.getEmail(),joinForm.getProfileImg());

            if(joinRs.isFail()){
                return rq.historyBack(joinRs.getMsg());
            }
        return rq.redirect("/",joinRs.getMsg());
    }

    @GetMapping("checkUsernameDup")
    @ResponseBody
    public RsData<String> checkUsernameDup(String username){
        return memberService.checkUsernameDup(username);
    }

    @GetMapping("/checkEmailDup")
    @ResponseBody
    public RsData<String> checkEmailDup(String email){
        return memberService.checkUsernameDup(email);
    }
}
