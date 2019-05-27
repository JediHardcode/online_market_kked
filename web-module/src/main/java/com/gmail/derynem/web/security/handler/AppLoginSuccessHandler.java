package com.gmail.derynem.web.security.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;

import static com.gmail.derynem.web.constants.RoleNamesConstant.ADMINISTRATOR_ROLE;
import static com.gmail.derynem.web.constants.RoleNamesConstant.CUSTOMER_ROLE;
import static com.gmail.derynem.web.constants.RoleNamesConstant.SALE_ROLE;

public class AppLoginSuccessHandler implements AuthenticationSuccessHandler {
    private static final Logger logger = LoggerFactory.getLogger(AppLoginSuccessHandler.class);
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException {
        handle(httpServletRequest, httpServletResponse, authentication);
        clearAuthenticationAttributes(httpServletRequest);
    }

    private void handle(HttpServletRequest request,
                        HttpServletResponse response, Authentication authentication) throws IOException {
        String targetUrl = determineTargetUrl(authentication);
        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to {} ", targetUrl);
            return;
        }
        redirectStrategy.sendRedirect(request, response, targetUrl);
    }

    private String determineTargetUrl(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities
                = authentication.getAuthorities();
        for (GrantedAuthority grantedAuthority : authorities) {
            if (grantedAuthority.getAuthority().equalsIgnoreCase(ADMINISTRATOR_ROLE)) {
                logger.info("{} role detected, return  ", grantedAuthority.toString());
                return "/private/reviews";
            } else if (grantedAuthority.getAuthority().equalsIgnoreCase(CUSTOMER_ROLE)) {
                return "/home";// TODO ADD LATER ALL URL FOR 4 ROLES
            } else if (grantedAuthority.getAuthority().equalsIgnoreCase(SALE_ROLE)) {
                return "/public/articles";
            }
        }
        logger.debug("Not found matched roles, check success Handler");
        throw new IllegalStateException("Not found matched roles, check success Handler");
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }
}