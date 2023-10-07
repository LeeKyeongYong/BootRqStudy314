package com.qrstudy.qrstudy.domain.controller.email;

import com.qrstudy.qrstudy.base.rq.Rq;
import com.qrstudy.qrstudy.base.rsData.RsData;
import com.qrstudy.qrstudy.domain.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/emailVerification")
public class EmailVerificationController {
    private final MemberService memberService;
    private final Rq rq;

    @GetMapping("/verify")
    public String verify(long memberId,String code){
        RsData verifyEmailRsData = memberService.verifyEmail(memberId,code);

        if(verifyEmailRsData.isFail()){
            return rq.redirect("/",verifyEmailRsData.getMsg());
        }

        String succressMsg = verifyEmailRsData.getMsg();

        if(rq.isLogout()){
            return rq.redirect("/usr/member/login",succressMsg);
        }
        return rq.redirect("/",succressMsg);
    }
}
