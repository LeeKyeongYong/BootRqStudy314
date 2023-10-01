package com.qrstudy.qrstudy.domain.controller.member;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/usr/member/")
public class MemberController {
    @GetMapping("/join")
    public String showJoin(){
        return "usr/member/join";
    }
}
