package com.ryszka.imageRestApi.controller.readController;

import com.ryszka.imageRestApi.viewModels.request.RenewAccountTokenRequest;
import com.ryszka.imageRestApi.viewModels.request.UserLoginRequest;
import com.ryszka.imageRestApi.viewModels.response.UserDetailsResponseModel;
import com.ryszka.imageRestApi.service.serviceV2.readService.UserAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.net.URISyntaxException;

@RestController
@RequestMapping(value = "auth")
public class UserAuthController {
    private final UserAuthService userAuthService;
    private final RedirectController controller;

    public UserAuthController(UserAuthService userAuthService, RedirectController redirectController) {
        this.userAuthService = userAuthService;
        this.controller = redirectController;
    }

    @GetMapping(value = "identify/user")
    public ResponseEntity<UserDetailsResponseModel> getLoggedUSerDetails(HttpServletRequest request) {
        return ResponseEntity
                .ok(userAuthService.getLoggedUserDetailsBySessionID(request));
    }

    @PostMapping(value = "renew/accountToken")
    public ResponseEntity<Object> renewAccountToken(@RequestBody RenewAccountTokenRequest request) throws MessagingException, URISyntaxException {
        System.out.println(request.getEmail());
        this.userAuthService.setAccountVerificationToken(request);
        return ResponseEntity.status(200).build();
    }
}
