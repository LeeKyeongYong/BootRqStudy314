package com.qrstudy.qrstudy.base.security;

import com.qrstudy.qrstudy.domain.standard.exception.EmailNotVerifiedAccessDeniedException;
import com.qrstudy.qrstudy.domain.standard.util.Ut;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;

import java.io.IOException;

public class CustomAccessDenineHandler extends AccessDeniedHandlerImpl {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        if(accessDeniedException instanceof EmailNotVerifiedAccessDeniedException){
            response.sendRedirect("/usr/member/notVerified?msg=", Ut.url.endcodeWithTtl(accessDeniedException.getMessage()));
            return;
        }
        super.handle(request, response, accessDeniedException);
    }
}
