package com.ryszka.imageRestApi.controller.writeController;

import com.ryszka.imageRestApi.controller.readController.RedirectController;
import com.ryszka.imageRestApi.security.AppConfigProperties;
import com.ryszka.imageRestApi.security.JWTVerifier;
import com.ryszka.imageRestApi.service.dto.UserDTO;
import com.ryszka.imageRestApi.viewModels.request.UserRegistrationRequestModel;
import com.ryszka.imageRestApi.viewModels.response.SignedUpUserDetailsResponse;
import com.ryszka.imageRestApi.service.serviceV2.writeService.UserSignupService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

@RestController
@RequestMapping("users")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:8080"}, methods = {RequestMethod.GET, RequestMethod.POST})
public class UserSignupController {
    private final UserSignupService registerService;
    private RedirectController controller;

    public UserSignupController(UserSignupService registerService, RedirectController controller) {
        this.registerService = registerService;
        this.controller = controller;
    }


    /*public UserSignupController(UserSignupService registerService) {
        this.registerService = registerService;
    }*/



    /*@GetMapping(value = "/redirect")
    public ResponseEntity<Object> testRedirect() throws URISyntaxException {
        String foo = Jwts.builder()
                .setSubject("FOO")
                .setExpiration(new Date(System.currentTimeMillis() + 3000))
                .signWith(SignatureAlgorithm.HS512, AppConfigProperties.JWT_SECRET_SIGNUP)
                .compact();
        System.out.println(new JWTVerifier(
                foo, AppConfigProperties.JWT_SECRET_SIGNUP).verifyToken());
        return controller.redirectToUrl("http://localhost:4200/#/verify");
        *//*URI yahoo = new URI("http://localhost:4200/#/verify");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(yahoo);
        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);*//*
    }*/


    @CrossOrigin
    @PostMapping(value = "/signUp")
    public ResponseEntity<Object> signUp(@RequestBody UserRegistrationRequestModel newUser,
                                         HttpServletRequest request) throws URISyntaxException, MessagingException {
        /*try {
            System.out.println(newUser.getFirstName() + "   " + newUser.getLastName());
            SignedUpUserDetailsResponse loggedInUserResponse = registerService
                    .createNewUser(new UserDTO(newUser)).get();

            return validateAndCreateUserResponse(loggedInUserResponse, request, newUser);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }*/

        System.out.println(request);


        SignedUpUserDetailsResponse user = registerService.createNewUser(new UserDTO(newUser));
        if (user != null) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else return ResponseEntity.status(HttpStatus.CONFLICT).build();

    }

    public ResponseEntity<SignedUpUserDetailsResponse> validateAndCreateUserResponse(SignedUpUserDetailsResponse signedUpUserDetailsResponse,
                                                                                     HttpServletRequest request,
                                                                                     UserRegistrationRequestModel newUser) {
        if (signedUpUserDetailsResponse.getUserId() == null) return ResponseEntity.status(409).body(signedUpUserDetailsResponse);
        return ResponseEntity.status(201).body(signedUpUserDetailsResponse);
    }
}
