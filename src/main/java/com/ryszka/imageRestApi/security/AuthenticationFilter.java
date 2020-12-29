package com.ryszka.imageRestApi.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryszka.imageRestApi.errorHandling.ErrorMessages;
import com.ryszka.imageRestApi.errorHandling.LoginException;
import com.ryszka.imageRestApi.viewModels.request.UserLoginRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final ObjectMapper mapper;
    private final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    public AuthenticationFilter(AuthenticationManager authenticationManager, ObjectMapper mapper) {
        this.authenticationManager = authenticationManager;
        this.mapper = mapper;
    }


    /*public AuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }*/

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            UserLoginRequest userLoginRequest = new ObjectMapper()
                    .readValue(request.getInputStream(), UserLoginRequest.class);
            return authenticate(userLoginRequest, response);
        } catch (IOException e) {
            logger.error("Attempted authentication failed {}", e.getMessage());
            response.setStatus(401);

        }
        return null;
    }

    // hallo jaeger

    private Authentication authenticate(UserLoginRequest userLoginRequest, HttpServletResponse response) throws IOException {
        try {
            return authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            userLoginRequest.getEmail(),
                            userLoginRequest.getPassword(),
                            new ArrayList<>())
                    );
        } catch (AuthenticationException e) {
            if (e.getMessage().equals("User is disabled")) {
                response.setStatus(423);
                mapper.writeValue(response.getWriter(),
                        "Account needs to be validated. Check your inbox.");
            } else {
                response.setStatus(401);
                mapper.writeValue(response.getWriter(),
                        ErrorMessages.LOGIN_ERROR_MESSAGE.getMessage());
            }
            logger.error("{}", e.getMessage());
            /*response.setStatus(401);*/
            /*response.sendError(401, ErrorMessages.LOGIN_FAIL.getMessage());*/
        }
        return null;
    }

}
