package com.gmail.derynem.web.security.handler;

import com.gmail.derynem.service.model.api.ResponseDTO;
import com.gmail.derynem.service.model.user.UserPrincipal;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ApiAccessDeniedHandler implements AccessDeniedHandler {
    private final static Logger logger = LoggerFactory.getLogger(AppAccessDeniedHandler.class);

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse response, AccessDeniedException e) throws IOException {
        UserPrincipal userDetails = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails != null) {
            logger.info("{} try to access in protected resource: {}",
                    userDetails.getUsername(),
                    httpServletRequest.getRequestURI());
        }
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setErrorCode("403");
        responseDTO.setMessage(e.getMessage());
        String jsonResponse = new Gson().toJson(responseDTO);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write(jsonResponse);
    }
}