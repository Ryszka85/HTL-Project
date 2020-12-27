package com.ryszka.imageRestApi.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryszka.imageRestApi.dao.UserDAO;
import com.ryszka.imageRestApi.errorHandling.ErrorMessages;
import com.ryszka.imageRestApi.persistenceEntities.UserEntity;
import com.ryszka.imageRestApi.standardMesages.StandardMessages;
import com.ryszka.imageRestApi.util.PasswordGenerator;
import com.ryszka.imageRestApi.util.mapper.mapStrategies.LoginModelToUserEntity;
import com.ryszka.imageRestApi.viewModels.request.UserLoginRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class GoogleAuthFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final Logger logger = LoggerFactory.getLogger(GoogleAuthFilter.class);
    private final UserDAO userDAO;
    private final ObjectMapper mapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final GoogleLogin googleLogin = new GoogleLogin();

    public GoogleAuthFilter(AuthenticationManager authenticationManager,
                            UserDAO userDAO,
                            ObjectMapper mapper,
                            BCryptPasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userDAO = userDAO;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {

            // map from request
            UserLoginRequest userLoginRequest = new ObjectMapper()
                    .readValue(request.getInputStream(), UserLoginRequest.class);
            // google authentication validate token
            googleLogin.validateToken(userLoginRequest);
            Optional<UserEntity> byEmailOpt = userDAO.findByEmail(userLoginRequest.getEmail());
            SecurityContext context = SecurityContextHolder.getContext();

            // validate if user already has an account
            if (byEmailOpt.isEmpty()) {
                userLoginRequest.setPassword(
                        passwordEncoder.encode(PasswordGenerator.generateCommonLangPassword()));
                UserEntity userEntity = com.ryszka.imageRestApi.util.mapper.ObjectMapper.mapByStrategy(userLoginRequest,
                        new LoginModelToUserEntity());
                userEntity.setAccountVerified(true);
                userDAO.saveUserEntity(userEntity);
                context.setAuthentication(
                        new UsernamePasswordAuthenticationToken(userLoginRequest.getEmail(),userLoginRequest.getPassword(), new ArrayList<>()));
            } else {
                UserEntity userEntity = byEmailOpt.get();
                context.setAuthentication(new UsernamePasswordAuthenticationToken(
                        userEntity.getEmail(),
                        userEntity.getPassword(),
                        new ArrayList<>()
                ));
            }
            logger.info(StandardMessages.LOGIN_SUCCESS.getMsg());
            return context.getAuthentication();
        } catch (IOException | IllegalArgumentException e) {
            logger.error(ErrorMessages.LOGIN_FAIL.getMessage());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
        return null;
    }

    private void authenticateWithGoogle(UserLoginRequest userLoginRequest) throws IOException {
        googleLogin.validateToken(userLoginRequest);
    }
}
