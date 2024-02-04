package com.qrstudy.qrstudy.base.security;

import com.qrstudy.qrstudy.domain.standard.util.Ut;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import java.io.IOException;

public class CustomSimpleUrlAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // request 객체에서 폼에서 POST 방식으로 username 을 이름으로 하여 보낸 값을 얻고 싶어
        String username = request.getParameter("username");

        String failMsg = exception instanceof BadCredentialsException ? "비밀번호가 일치하지 않습니다." : "존재하지 않는 회원입니다.";

        setDefaultFailureUrl("/usr/member/login?lastUsername=" + username + "&failMsg=" + Ut.url.encodeWithTtl(failMsg));
        super.onAuthenticationFailure(request, response, exception);
    }
}