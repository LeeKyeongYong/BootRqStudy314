package com.qrstudy.qrstudy.domain.controller.member;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberController {
    @GetMapping("/")
    public String showMain(){
        return "usr/home/main";
    }
}
