package com.qrstudy.qrstudy.base.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultHttpSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.context.ApplicationContext;
import java.util.stream.Stream;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final ApplicationContext context;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(
                        authorizeRequests -> authorizeRequests
                                .requestMatchers(requestMatchersOf("/usr/member/notVerified")
                                ).permitAll()
                                .requestMatchers(
                                        requestMatchersOf("/usr/post/modify/*")
                                ).access(accessOf("@postController.assertActorCanModify()"))
                                .requestMatchers(
                                        requestMatchersOf("/usr/post/remove/*")
                                ).access(accessOf("@postController.assertActorCanRemove()"))
                                .requestMatchers(
                                        requestMatchersOf("/usr/book/*/write")
                                ).access(accessOf("@bookController.assertActorCanWrite()"))
                                .requestMatchers(
                                        requestMatchersOf("/usr/book/*/modify/*")
                                ).access(accessOf("@bookController.assertActorCanModify()"))
                                .requestMatchers(
                                        requestMatchersOf("/usr/book/*/remove/*")
                                ).access(accessOf("@bookController.assertActorCanRemove()"))
                                .requestMatchers(
                                        requestMatchersOf("/usr/article/*/write")
                                ).access(accessOf("@articleController.assertActorCanWrite()"))
                                .requestMatchers(
                                        requestMatchersOf("/usr/article/*/modify/*")
                                ).access(accessOf("@articleController.assertActorCanModify()"))
                                .requestMatchers(
                                        requestMatchersOf("/usr/article/*/remove/*")
                                ).access(accessOf("@articleController.assertActorCanRemove()"))
                                .requestMatchers(
                                        requestMatchersOf("/usr/member/beProducer", "/usr/member/modify")
                                ).access(accessOf("@memberController.assertCheckPasswordAuthCodeVerified()"))
                                .requestMatchers(
                                        requestMatchersOf("/", "/usr/**")
                                ).access(accessOf("isAnonymous() or @memberController.assertCurrentMemberVerified()"))
                                .requestMatchers(
                                        requestMatchersOf("/adm/**")
                                )
                                .hasAuthority("admin")
                                .anyRequest().permitAll()
                )
                .exceptionHandling(
                        exceptionHandling -> exceptionHandling
                                .accessDeniedHandler(new CustomAccessDeniedHandler())
                )
                .oauth2Login(
                        oauth2Login -> oauth2Login
                                .loginPage("/usr/member/login")
                )
                .csrf((csrf) -> csrf
                        .ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**")))
                .headers((headers) -> headers
                        .addHeaderWriter(new XFrameOptionsHeaderWriter(
                                XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))
                .formLogin((formLogin) -> formLogin
                        .loginPage("/usr/member/login")
                        .successHandler(new CustomSimpleUrlAuthenticationSuccessHandler())
                        .failureHandler(new CustomSimpleUrlAuthenticationFailureHandler())
                )
                .logout((logout) -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/usr/member/logout"))
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true))
        ;
        return http.build();
    }

    private WebExpressionAuthorizationManager accessOf(String expressionString) {
        DefaultHttpSecurityExpressionHandler expressionHandler = new DefaultHttpSecurityExpressionHandler();
        expressionHandler.setApplicationContext(context);
        WebExpressionAuthorizationManager authorization = new WebExpressionAuthorizationManager(expressionString);
        authorization.setExpressionHandler(expressionHandler);

        return authorization;
    }

    private AntPathRequestMatcher[] requestMatchersOf(String... patterns) {
        return Stream.of(patterns)
                .map(AntPathRequestMatcher::new)
                .toArray(AntPathRequestMatcher[]::new);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}