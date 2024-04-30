package net.kdigital.ec21.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import net.kdigital.ec21.dto.LoginUserDetails;
import net.kdigital.ec21.dto.check.YesOrNo;

@Slf4j
@Component
public class CustomerSuccessHandler implements AuthenticationSuccessHandler {
    private final RequestCache requestCache = new HttpSessionRequestCache();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        


        log.info("=========== Login Success");
        log.info("{}",authentication.getPrincipal());
        log.info( "{}",authentication.getDetails());
        List<String> roleNames = new ArrayList<>();
        authentication.getAuthorities().forEach(authority -> {
            roleNames.add(authority.getAuthority());
        });
        log.info("ROLE NAMES : {}", roleNames);

        SavedRequest savedRequest = requestCache.getRequest(request, response);

        String prevPage = (String) request.getSession().getAttribute("prevPage");
        if (prevPage != null) {
            request.getSession().removeAttribute("prevPage");
        }
        String uri = "/";

        // principal을 LoginUserDetails 타입으로 캐스팅
        if (authentication.getPrincipal() instanceof LoginUserDetails) {
            LoginUserDetails userDetails = (LoginUserDetails) authentication.getPrincipal();

            // 블랙리스트 상태 확인
            if (userDetails.getBlacklistCheck() == YesOrNo.Y) {
                // 사용자가 블랙리스트에 있는 경우 처리
                log.info("User is blacklisted");
                response.sendRedirect("/blacklist");
                return;
            }

            // 탈퇴한 회원인 경우 처리
            if (userDetails.getEnabled()==YesOrNo.Y) {
                log.info("User account is a withdrawal account");
                response.sendRedirect("/withdrawalAccount");
                return;
            }
        }

        // 관리자인 경우
        if (roleNames.contains("ROLE_ADMIN")) {
            response.sendRedirect("/manager/manager_index");
            return;
        }
        // 사용자인 경우
        if (roleNames.contains("ROLE_USER")) {
            if (savedRequest != null) {
                uri = savedRequest.getRedirectUrl();
                response.sendRedirect(uri);
                return;
            } else if (prevPage != null && !prevPage.equals("")) {
                uri = prevPage;
                response.sendRedirect(uri);
                return;
            }

            // 이전 URL이 없으면 홈 페이지로 리디렉션
            response.sendRedirect("/");
            return;
        }
        response.sendRedirect("/");

    }
    
}
