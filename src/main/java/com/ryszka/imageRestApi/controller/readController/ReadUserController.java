package com.ryszka.imageRestApi.controller.readController;

import com.ryszka.imageRestApi.viewModels.response.UserDetailsResponseModel;
import com.ryszka.imageRestApi.service.serviceV2.readService.ReadUserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "user")
public class ReadUserController {
    private final ReadUserDetailsService readUserDetailsService;

    public ReadUserController(ReadUserDetailsService readUserDetailsService) {
        this.readUserDetailsService = readUserDetailsService;
    }

    @GetMapping(value = "/details/{userId}")
    public UserDetailsResponseModel userDetailsByUID(@PathVariable String userId,
                                                     HttpServletRequest request) {
        return readUserDetailsService.getUserDetailsByUID(userId, request);
    }

    @GetMapping(value = "/details/email/{email}")
    public UserDetailsResponseModel userDetailsByEmail(@PathVariable String email) {
        return readUserDetailsService.findUserByEmail(email);
    }
}
