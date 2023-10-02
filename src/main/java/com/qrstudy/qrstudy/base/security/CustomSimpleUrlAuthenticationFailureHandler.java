package com.qrstudy.qrstudy.base.security;

import com.qrstudy.qrstudy.domain.standard.util.Ut;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import java.io.IOException;

public class CustomSimpleUrlAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        setDefaultFailureUrl("/usr/member/login?failMsg="+ Ut.url.endcodeWithTtl("올바르지 않은 회원 정보입니다."));

        super.onAuthenticationFailure(request, response, exception);
    }
}
