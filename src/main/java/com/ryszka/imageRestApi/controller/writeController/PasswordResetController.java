package com.ryszka.imageRestApi.controller.writeController;

import com.google.api.gax.rpc.InvalidArgumentException;
import com.ryszka.imageRestApi.controller.readController.RedirectController;
import com.ryszka.imageRestApi.dao.UserDAO;
import com.ryszka.imageRestApi.errorHandling.EntityNotFoundException;
import com.ryszka.imageRestApi.persistenceEntities.PasswordResetTokenEntity;
import com.ryszka.imageRestApi.persistenceEntities.UserEntity;
import com.ryszka.imageRestApi.repository.PasswordResetTokenRepository;
import com.ryszka.imageRestApi.security.AppConfigProperties;
import com.ryszka.imageRestApi.service.serviceV2.EmailService;
import com.ryszka.imageRestApi.standardMesages.StandardMessages;
import com.ryszka.imageRestApi.util.EmailSender;
import com.ryszka.imageRestApi.viewModels.PasswordResetTokenRequest;
import com.ryszka.imageRestApi.viewModels.ShowTokenValidationResponse;
import com.ryszka.imageRestApi.viewModels.request.TokenIdRequest;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(value = "reset/password")
public class PasswordResetController {
    private final EmailService emailService;
    private final UserDAO userDAO;
    private final PasswordResetTokenRepository tokenRepository;
    private EmailSender emailSender;
    private final Logger logger = LoggerFactory.getLogger(PasswordResetController.class);
    private final RedirectController redirectController;


    public PasswordResetController(EmailSender emailSender,
                                   EmailService emailService,
                                   UserDAO userDAO,
                                   PasswordResetTokenRepository passwordResetTokenRepository,
                                   RedirectController redirectController) {
        this.redirectController = redirectController;
        this.userDAO = userDAO;
        this.emailService = emailService;
        this.tokenRepository = passwordResetTokenRepository;
        this.emailSender = emailSender;

    }

    @PostMapping(value = "request-token")
    public ResponseEntity<ShowTokenValidationResponse> processPasswordResetTokenRequest(@RequestBody PasswordResetTokenRequest request) throws MessagingException, URISyntaxException {
        logger.info("Entered [ processPasswordResetTokenRequest ]...");
        PasswordResetTokenEntity resetTokenEntity = null;
        if (request.getEmail() != null) {
            logger.info("starting [ userDAO.findByEmail ]");
            UserEntity userEntity = userDAO.findByEmail(request.getEmail())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Password reset was aborted. Provided credentials are invalid"));
            logger.info("starting [ tokenRepository.getByUserEntity ]");
            Optional<PasswordResetTokenEntity> byUserEntityOpt = tokenRepository.getByUserEntity(userEntity);
            String passwordResetToken = Jwts.builder()
                    .setSubject(userEntity.getEmail())
                    .setExpiration(new Date(System.currentTimeMillis() + 300000))
                    .signWith(SignatureAlgorithm.HS512, AppConfigProperties.JWT_SECRET_SIGNUP)
                    .compact();
            resetTokenEntity = byUserEntityOpt.orElseGet(() ->
                    new PasswordResetTokenEntity(
                            UUID.randomUUID().toString(),
                            passwordResetToken,
                            userEntity,
                            false,
                            false));
            resetTokenEntity.setUserEntity(userEntity);
            resetTokenEntity.setTokenId(UUID.randomUUID().toString());
            resetTokenEntity.setToken(passwordResetToken);
            resetTokenEntity.setWasValidated(false);
            resetTokenEntity.setProcessedByView(false);
            emailSender.sendVerifyTokenEmail(
                    "Password Reset",
                    "http://localhost:8880/image-app/reset/password/verify/",
                    StandardMessages.PASSWORD_RESET_TEXT.getMsg(),
                    "Reset-your-password",
                    userEntity.getEmail(),
                    passwordResetToken);
            tokenRepository.save(resetTokenEntity);
            return ResponseEntity.ok(new ShowTokenValidationResponse(true, false));
        }
        return ResponseEntity.ok(new ShowTokenValidationResponse(false, false));
    }

    @GetMapping(value = "verify/{token}")
    public ResponseEntity<Object> validateRedirectResetPassword(@PathVariable String token, HttpServletResponse response) throws MessagingException, URISyntaxException {

        Claims body = null;
        logger.info("Starting [ passwordTokenRepository.getByToken ]");
        PasswordResetTokenEntity passwordResetTokenEntity = tokenRepository.getByToken(token)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Reset-password-token could not be found"));
        try {
            logger.info("Starting [ Jwts.parser() ]");
            body = Jwts.parser()
                    .setSigningKey(AppConfigProperties.JWT_SECRET_SIGNUP)
                    .parseClaimsJws(token)
                    .getBody();
            Date tokenExpirationDate = body.getExpiration();
            Date today = new Date();
            System.out.println(!passwordResetTokenEntity.getIsProcessedByView() &&
                    !passwordResetTokenEntity.isWasValidated());
            if (token.equals(passwordResetTokenEntity.getToken()) &&
                    !tokenExpirationDate.before(today) &&
                    !passwordResetTokenEntity.getIsProcessedByView() &&
                    !passwordResetTokenEntity.isWasValidated() &&
                    passwordResetTokenEntity.getUserEntity().getEmail()
                            .equals(body.getSubject())) {

                passwordResetTokenEntity.setWasValidated(true);
                tokenRepository.save(passwordResetTokenEntity);
                logger.info("Starting [ redirect ] to http://localhost:4200/#/renew-password/:" +
                        passwordResetTokenEntity.getTokenId());
                return redirectController.redirectToUrl("http://localhost:4200/#/renew-password/:" +
                        passwordResetTokenEntity.getTokenId(), response);
            }
        } catch (ExpiredJwtException | URISyntaxException | IllegalArgumentException | SignatureException | MalformedJwtException | UnsupportedJwtException e) {
            return ResponseEntity.status(403).body("Token is invalid -> " + e.getMessage());
        }
        return null;
    }

    @PostMapping(value = "/validate-tokenId")
    public ValidatedResetPasswordTokenIdResponse validateRedirectResetPassword(@RequestBody TokenIdRequest request) {
        PasswordResetTokenEntity byTokenId = tokenRepository
                .getByTokenId(request.getTokenId())
                .filter(passwordResetTokenEntity -> !passwordResetTokenEntity.getIsProcessedByView())
                .filter(PasswordResetTokenEntity::isWasValidated)
                .orElseThrow(() -> new EntityNotFoundException("Requested tokenId is invalid!"));
        byTokenId.setWasValidated(true);
        byTokenId.setProcessedByView(true);
        tokenRepository.save(byTokenId);
        return new ValidatedResetPasswordTokenIdResponse(
                byTokenId.getTokenId(),
                byTokenId.getUserEntity().getUserId());
    }
}


class ValidatedResetPasswordTokenIdResponse {
    private String tokenId, userId;

    public ValidatedResetPasswordTokenIdResponse() {
    }

    public ValidatedResetPasswordTokenIdResponse(String tokenId, String userId) {
        this.tokenId = tokenId;
        this.userId = userId;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
