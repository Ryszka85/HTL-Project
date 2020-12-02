package com.ryszka.imageRestApi.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryszka.imageRestApi.config.AppContext;
import com.ryszka.imageRestApi.dao.UserDAO;
import com.ryszka.imageRestApi.service.serviceV2.readService.ReadUserDetailsService;
import com.ryszka.imageRestApi.standardMesages.StandardMessages;
import com.ryszka.imageRestApi.viewModels.response.UserDetailsResponseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GoogleSuccessHandler implements AuthenticationSuccessHandler {
    private final Logger logger = LoggerFactory.getLogger(AuthenticationSuccessHandler.class);
    private final ObjectMapper objectMapper;

    public GoogleSuccessHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /*public GoogleSuccessHandler(ObjectMapper mapper, UserDAO userDAO) {
        this.mapper = mapper;
        this.userDAO = userDAO;
    }*/

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String email = authentication.getName();

        ReadUserDetailsService UserAuth = (ReadUserDetailsService) AppContext.getBean("readUserDetailsService");
        UserDetailsResponseModel loggedInUserResponse = UserAuth.findUserByEmail(email);
        response.addHeader("UserId", loggedInUserResponse.getUserId());
        objectMapper.writeValue(response.getWriter(), loggedInUserResponse);
        response.setStatus(HttpStatus.OK.value());
        logger.info(StandardMessages.LOGIN_SUCCESS.getMsg());
    }
}
