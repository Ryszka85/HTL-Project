package com.ryszka.imageRestApi.service.serviceV2.readService;

import com.ryszka.imageRestApi.dao.SessionDAO;
import com.ryszka.imageRestApi.dao.UserDAO;
import com.ryszka.imageRestApi.errorHandling.AccountNotActiveException;
import com.ryszka.imageRestApi.errorHandling.EntityNotFoundException;
import com.ryszka.imageRestApi.errorHandling.ErrorMessages;
import com.ryszka.imageRestApi.persistenceEntities.AccountVerificationTokenEntity;
import com.ryszka.imageRestApi.repository.AccountVerificationRepository;
import com.ryszka.imageRestApi.security.AppConfigProperties;
import com.ryszka.imageRestApi.standardMesages.StandardMessages;
import com.ryszka.imageRestApi.util.EmailSender;
import com.ryszka.imageRestApi.viewModels.request.RenewAccountTokenRequest;
import com.ryszka.imageRestApi.viewModels.response.UserDetailsResponseModel;
import com.ryszka.imageRestApi.persistenceEntities.SessionEntity;
import com.ryszka.imageRestApi.persistenceEntities.UserEntity;
import com.ryszka.imageRestApi.util.mapper.ObjectMapper;
import com.ryszka.imageRestApi.util.mapper.mapStrategies.UserEntityToUserDetailsResponseModel;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Service
public class UserAuthService implements UserDetailsService {
    private final UserDAO userDAO;
    private final SessionDAO sessionDAO;
    private final Logger logger =
            LoggerFactory.getLogger(UserAuthService.class);
    private EmailSender emailSender;
    private BCryptPasswordEncoder passwordEncoder;
    private AccountVerificationRepository verificationRepository;

    public UserAuthService(UserDAO userDAO, SessionDAO sessionDAO, EmailSender emailSender, BCryptPasswordEncoder passwordEncoder, AccountVerificationRepository verificationRepository) {
        this.userDAO = userDAO;
        this.sessionDAO = sessionDAO;
        this.emailSender = emailSender;
        this.passwordEncoder = passwordEncoder;
        this.verificationRepository = verificationRepository;
    }


    /*public UserAuthService(UserDAO userDAO, SessionDAO sessionDAO, EmailSender emailSender,
                           BCryptPasswordEncoder passwordEncoder) {
        this.userDAO = userDAO;
        this.sessionDAO = sessionDAO;
        this.emailSender = emailSender;
        this.passwordEncoder = passwordEncoder;
    }*/

    public UserDetailsResponseModel getLoggedUserDetailsBySessionID(HttpServletRequest request) {
        logger.info("Attempting [ getLoggedUserDetailsBySessionID ]..");
        SessionEntity sessionEntity = this.sessionDAO
                .findUserBySessionID(request.getSession().getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        ErrorMessages.NOT_FOUND_BY_SESSIONID.getMessage()));
        UserEntity userEntity = userDAO.findByEmail(sessionEntity.getPrincipal())
                .orElseThrow(() -> new EntityNotFoundException(
                        ErrorMessages.NOT_FOUND_BY_USERNAME.getMessage()));
        if (!userEntity.isAccountVerified())
            throw new AccountNotActiveException(
                    ErrorMessages.ACCOUNT_NOT_ACTIVE.getMessage());
        UserDetailsResponseModel userDetailsResponseModel = ObjectMapper.mapByStrategy(userEntity,
                new UserEntityToUserDetailsResponseModel());
        userDetailsResponseModel.setThirdPartyLogin(userEntity.getLoginType().equals("GOOGLE"));
        return userDetailsResponseModel;
    }


    public void setAccountVerificationToken(RenewAccountTokenRequest request) throws MessagingException, URISyntaxException {
        System.out.println("SERRRR");
        UserEntity userEntity = userDAO.findByEmail(request.getEmail())
                .orElseThrow(() -> new EntityNotFoundException(
                        "User account could not be found"));
        AccountVerificationTokenEntity verificationTokenEntityByTokenId = verificationRepository.getAccountVerificationTokenEntityByTokenId(userEntity
                .getAccountVerificationToken().getTokenId())
                .orElseThrow(() -> new EntityNotFoundException("Token is invalid"));
        Claims body = null;
        try {

            // if body exists then token is not expired
            // verification email was already sent

            /*body = Jwts.parser()
                    .setSigningKey(AppConfigProperties.JWT_SECRET_SIGNUP)
                    .parseClaimsJws(verificationTokenEntityByTokenId.getToken())
                    .getBody();*/

            String token = Jwts.builder()
                    .setSubject(userEntity.getEmail())
                    .setExpiration(new Date(System.currentTimeMillis() + 300000))
                    .signWith(SignatureAlgorithm.HS512, AppConfigProperties.JWT_SECRET_SIGNUP)
                    .compact();

            if (!userEntity.isAccountVerified() &&
                    passwordEncoder.matches(request.getPassword(), userEntity.getPassword())) {
                logger.info("Starting to build token...");
                logger.info("Attempting to send email to : " + userEntity.getEmail());
                logger.info("Attempting to set accountVerification token...");
                System.out.println(userEntity.getAccountVerificationToken().getToken());
                System.out.println(verificationTokenEntityByTokenId.getToken());
                System.out.println(userEntity.getAccountVerificationToken().getToken());
                verificationTokenEntityByTokenId.setToken(token);
                verificationTokenEntityByTokenId.setProcessedByView(false);
                verificationTokenEntityByTokenId.setWasValidated(false);
                verificationRepository.save(verificationTokenEntityByTokenId);
                emailSender.sendVerifyTokenEmail(
                        "Verify your email",
                        "http://localhost:8880/image-app/verify/account/",
                        StandardMessages.ACCOUNT_VERIFY_EMAIL_TEXT.getMsg(),
                        "Activate-account,",
                        userEntity.getEmail(),
                        token);
            }


            /*throw new EmailAlreadySentException(ErrorMessages.EMAIL_ALREADY_SENT.getMessage());*/


        } catch (ExpiredJwtException | IllegalArgumentException | SignatureException | MalformedJwtException | UnsupportedJwtException e) {
            userEntity.setAccountVerificationToken(null);
            userDAO.saveUserEntity(userEntity);
            /*verificationRepository.delete();*/
             throw new AccountNotActiveException("Email could not be send due to invalid arguments.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Attempting [ loadUserByUsername ]...");
        UserEntity userEntity = userDAO.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException(
                        ErrorMessages.NOT_FOUND_BY_USERNAME.getMessage()));
        if (userEntity == null)
            throw new UsernameNotFoundException("Could not find user to login..");
        return new User(userEntity.getEmail(),
                userEntity.getPassword(),
                userEntity.isAccountVerified(),
                true,
                true,
                true,
                new ArrayList<>());
    }
}
