package com.ryszka.imageRestApi.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryszka.imageRestApi.config.AuthSecurityConfig;
import com.ryszka.imageRestApi.errorHandling.ErrorMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginFailureHandler implements AuthenticationFailureHandler {
    private final Logger logger = LoggerFactory.getLogger(LoginFailureHandler.class);
    private final ObjectMapper mapper;

    public LoginFailureHandler(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        logger.error(ErrorMessages.LOGIN_FAIL.getMessage());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        mapper.writeValue(response.getWriter(),
                ErrorMessages.LOGIN_FAIL.getMessage());
    }
}
