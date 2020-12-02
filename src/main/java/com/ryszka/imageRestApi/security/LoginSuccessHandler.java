package com.ryszka.imageRestApi.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryszka.imageRestApi.config.AppContext;
import com.ryszka.imageRestApi.viewModels.response.UserDetailsResponseModel;
import com.ryszka.imageRestApi.service.serviceV2.readService.ReadUserDetailsService;
import com.ryszka.imageRestApi.standardMesages.StandardMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final Logger logger = LoggerFactory.getLogger(LoginSuccessHandler.class);
    private final ObjectMapper mapper;

    public LoginSuccessHandler(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String username = ((User) authentication.getPrincipal()).getUsername();
        ReadUserDetailsService UserAuth = (ReadUserDetailsService) AppContext.getBean("readUserDetailsService");
        UserDetailsResponseModel loggedInUserResponse = UserAuth.findUserByEmail(username);
        response.addHeader("UserId", loggedInUserResponse.getUserId());
        response.setStatus(HttpStatus.OK.value());
        mapper.writeValue(response.getWriter(), loggedInUserResponse);
        logger.info(StandardMessages.LOGIN_SUCCESS.getMsg());
    }

}
