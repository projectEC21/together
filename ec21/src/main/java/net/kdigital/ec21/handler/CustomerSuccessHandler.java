package net.kdigital.ec21.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomerSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        log.info("Login Success");
        List<String> roleNames = new ArrayList<>();
        authentication.getAuthorities().forEach(authority -> {
            roleNames.add(authority.getAuthority());
        });

        log.info("ROLE NAMES : {}", roleNames);
        if (roleNames.contains("ROLE_ADMIN")) {
            response.sendRedirect("/manager/manager_index");
            return;
        }
        if (roleNames.contains("ROLE_USER")) {
            response.sendRedirect("/");
            return;
        }
        response.sendRedirect("/");

    }
    
}
