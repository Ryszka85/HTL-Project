package com.ryszka.imageRestApi.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryszka.imageRestApi.standardMesages.StandardMessages;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SuccessfulLogoutHandler implements LogoutSuccessHandler {
    private final ObjectMapper mapper;

    public SuccessfulLogoutHandler(ObjectMapper mapper) {
        this.mapper = mapper;
    }
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setStatus(HttpStatus.OK.value());
        mapper.writeValue(response.getWriter(), StandardMessages.LOGOUT_SUCCESS.getMsg());
    }
}
