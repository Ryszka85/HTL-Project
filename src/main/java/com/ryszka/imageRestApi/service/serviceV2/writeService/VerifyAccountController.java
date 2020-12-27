package com.ryszka.imageRestApi.service.serviceV2.writeService;

import com.ryszka.imageRestApi.controller.readController.RedirectController;
import com.ryszka.imageRestApi.dao.UserDAO;
import com.ryszka.imageRestApi.errorHandling.EntityNotFoundException;
import com.ryszka.imageRestApi.errorHandling.ErrorMessages;
import com.ryszka.imageRestApi.persistenceEntities.UserEntity;
import com.ryszka.imageRestApi.security.AppConfigProperties;
import com.ryszka.imageRestApi.security.JWTVerifier;
import com.ryszka.imageRestApi.viewModels.response.SignedUpUserDetailsResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping(value = "verify")
public class VerifyAccountController {
    private UserDAO userDAO;
    private RedirectController redirectController;

    public VerifyAccountController(UserDAO userDAO,
                                   RedirectController controller) {
        this.userDAO = userDAO;
        this.redirectController = controller;
    }

    @GetMapping(value = "/account/{token}")
    public ResponseEntity<Object> verifyAccount(@PathVariable String token) throws URISyntaxException {
        Claims body = Jwts.parser()
                .setSigningKey(AppConfigProperties.JWT_SECRET_SIGNUP)
                .parseClaimsJws(token)
                .getBody();
        Date tokenExpirationDate = body.getExpiration();
        Date today = new Date();
        System.out.println(tokenExpirationDate.before(today));
        if (!tokenExpirationDate.before(today)) {
            String verifiedToken = new JWTVerifier(token, AppConfigProperties.JWT_SECRET_SIGNUP)
                    .verifyToken();
            System.out.println(verifiedToken);
            if (verifiedToken != null) {
                Optional<UserEntity> userEntityByToken = userDAO.findUserEntityByToken(token);
                UserEntity userEntity = userEntityByToken.orElseThrow(() ->
                        new EntityNotFoundException(ErrorMessages.INVALID_ARGUMENTS.getMessage()));
                userEntity.setAccountVerified(true);
                userDAO.saveUserEntity(userEntity);
                SecurityContext context = SecurityContextHolder.getContext();
                context.setAuthentication(
                        new UsernamePasswordAuthenticationToken(userEntity.getEmail(),userEntity.getPassword(), new ArrayList<>()));
                return redirectController.redirectToUrl("http://localhost:4200/#/verify");
            }
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
}
