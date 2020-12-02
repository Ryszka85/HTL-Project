package com.ryszka.imageRestApi.controller.readController;

import com.ryszka.imageRestApi.viewModels.response.UserDetailsResponseModel;
import com.ryszka.imageRestApi.service.serviceV2.readService.UserAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "auth")
public class UserAuthController {
    private final UserAuthService userAuthService;

    public UserAuthController(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }

    @GetMapping(value = "identify/user")
    public ResponseEntity<UserDetailsResponseModel> getLoggedUSerDetails(HttpServletRequest request) {
        return ResponseEntity
                .ok(userAuthService.getLoggedUserDetailsBySessionID(request));
    }
}
