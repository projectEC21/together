package net.kdigital.ec21.handler;

import java.io.IOException;
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomerSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
        Authentication authentication) throws IOException, ServletException {
        
        String adminPageUrl = "/manager/manager_index";
        String userPageUrl = "/";
        System.out.println("============"+ authentication.getPrincipal());
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean isAdmin = authorities.stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        boolean isUser = authorities.stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_USER"));

        if (isAdmin) {
            response.sendRedirect(adminPageUrl);
        } else if (isUser) {
            response.sendRedirect(userPageUrl);
        } else {
            // 처리할 권한이 없는 경우의 로직
            response.sendRedirect("/error");
        }

        // // 사용자의 권한 확인
        // Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        // boolean isAdmin = authorities.stream()
        //         .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        // log.info("Is admin: {}", isAdmin);

        // // 권한에 따라 리디렉션
        // if (isAdmin) {
        //     log.info("Admin 로그인 성공: {}", authentication.getName());
        //     response.sendRedirect(adminPageUrl);
        //     return;
        // } else {
        //     log.info("사용자 로그인 성공: {}", authentication.getName());
        //     response.sendRedirect(userPageUrl);
        // }
    }
    
}
