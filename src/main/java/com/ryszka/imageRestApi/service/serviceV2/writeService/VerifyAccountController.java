package com.ryszka.imageRestApi.service.serviceV2.writeService;

import com.ryszka.imageRestApi.controller.readController.RedirectController;
import com.ryszka.imageRestApi.dao.UserDAO;
import com.ryszka.imageRestApi.errorHandling.EntityNotFoundException;
import com.ryszka.imageRestApi.errorHandling.ErrorMessages;
import com.ryszka.imageRestApi.persistenceEntities.AccountVerificationTokenEntity;
import com.ryszka.imageRestApi.persistenceEntities.UserEntity;
import com.ryszka.imageRestApi.repository.AccountVerificationRepository;
import com.ryszka.imageRestApi.security.AppConfigProperties;
import com.ryszka.imageRestApi.security.JWTVerifier;
import com.ryszka.imageRestApi.service.serviceV2.readService.UserAuthService;
import com.ryszka.imageRestApi.viewModels.ShowTokenValidationResponse;
import com.ryszka.imageRestApi.viewModels.request.TokenIdRequest;
import com.ryszka.imageRestApi.viewModels.response.SignedUpUserDetailsResponse;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping(value = "verify")
public class VerifyAccountController {
    private UserDAO userDAO;
    private RedirectController redirectController;
    private AccountVerificationRepository verificationRepository;
    private final Logger logger =
            LoggerFactory.getLogger(VerifyAccountController.class);

    public VerifyAccountController(UserDAO userDAO,
                                   RedirectController controller, AccountVerificationRepository verificationRepository) {
        this.userDAO = userDAO;
        this.redirectController = controller;
        this.verificationRepository = verificationRepository;
    }

    @GetMapping(value = "/account/{token}")
    public ResponseEntity<Object> verifyAccount(@PathVariable String token, HttpServletResponse response) throws URISyntaxException {
        Claims body = null;
        AccountVerificationTokenEntity tokenEntity = null;
        try {
            logger.info("attempting [ verificationRepository.getByToken ]..");
            tokenEntity = verificationRepository.getByToken(token)
                    .orElseThrow(() -> new IllegalArgumentException("Token not found!"));
            body = Jwts.parser()
                    .setSigningKey(AppConfigProperties.JWT_SECRET_SIGNUP)
                    .parseClaimsJws(token)
                    .getBody();
            Date tokenExpirationDate = body.getExpiration();
            Date today = new Date();
            if (!tokenExpirationDate.before(today) && !tokenEntity.getIsProcessedByView() && !tokenEntity.isWasValidated()) {
                System.out.println(body);
                tokenEntity.setWasValidated(true);
                String verifiedToken = new JWTVerifier(token, AppConfigProperties.JWT_SECRET_SIGNUP)
                        .verifyToken();
                if (verifiedToken != null) {
                    logger.info("token was verified successful.");
                    Optional<UserEntity> userEntityByToken = userDAO.findByEmail(body.getSubject());
                    UserEntity userEntity1 = userEntityByToken.get();
                    if (userEntityByToken.isPresent() &&
                            userEntity1.getAccountVerificationToken().getToken().equals(tokenEntity.getToken())) {
                        logger.info("get user by token ...");
                        UserEntity userEntity = userEntityByToken.get();
                        userEntity.setAccountVerified(true);
                        userDAO.saveUserEntity(userEntity);
                        SecurityContext context = SecurityContextHolder.getContext();
                        context.setAuthentication(
                                new UsernamePasswordAuthenticationToken(userEntity.getEmail(), userEntity.getPassword(), new ArrayList<>()));
                        verificationRepository.save(tokenEntity);
                        return redirectController.redirectToUrl("http://localhost:4200/#/verify;id=" +
                                tokenEntity.getTokenId(), response);
                    }
                }
            } else return redirectController.redirectToUrl("http://localhost:4200/#/verify;id=" +
                    tokenEntity.getTokenId(), response);
        } catch (ExpiredJwtException | IllegalArgumentException | SignatureException | MalformedJwtException | UnsupportedJwtException e) {
            logger.info("Token is expired!!");
            assert tokenEntity != null;
            return redirectController.redirectToUrl("http://localhost:4200/#/verify;id=66666", response);
        }
        logger.info("Something went really wrong!");
        return null;
    }

    @PostMapping(value = "/show-validated-token")
    public ResponseEntity<ShowTokenValidationResponse> showValidatedToken(@RequestBody TokenIdRequest request) throws URISyntaxException {
        System.out.println(request.getTokenId());
        try {
            AccountVerificationTokenEntity entity = verificationRepository
                    .getAccountVerificationTokenEntityByTokenId(request.getTokenId())
                    .orElseThrow(() -> new EntityNotFoundException("Token is invalid!"));
            if (entity.isWasValidated() && !entity.getIsProcessedByView()) {
                entity.setProcessedByView(true);
                verificationRepository.save(entity);
                return ResponseEntity
                        .ok(new ShowTokenValidationResponse(true, true));
            } else {
                return ResponseEntity
                    .ok(new ShowTokenValidationResponse(false, true));
            }

        } catch (Exception e) {
            return ResponseEntity
                    .ok(new ShowTokenValidationResponse(false));
        }
    }

}


