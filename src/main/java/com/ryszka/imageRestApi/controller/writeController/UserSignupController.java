package com.ryszka.imageRestApi.controller.writeController;

import com.ryszka.imageRestApi.service.dto.UserDTO;
import com.ryszka.imageRestApi.viewModels.request.UserRegistrationRequestModel;
import com.ryszka.imageRestApi.viewModels.response.SignedUpUserDetailsResponse;
import com.ryszka.imageRestApi.service.serviceV2.writeService.UserSignupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("users")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:8080"}, methods = {RequestMethod.GET, RequestMethod.POST})
public class UserSignupController {
    private final UserSignupService registerService;

    public UserSignupController(UserSignupService registerService) {
        this.registerService = registerService;
    }


    @CrossOrigin
    @PostMapping(value = "/signUp")
    public ResponseEntity<SignedUpUserDetailsResponse> signUp(@RequestBody UserRegistrationRequestModel newUser,
                                                              HttpServletRequest request) {
        /*try {
            System.out.println(newUser.getFirstName() + "   " + newUser.getLastName());
            SignedUpUserDetailsResponse loggedInUserResponse = registerService
                    .createNewUser(new UserDTO(newUser)).get();

            return validateAndCreateUserResponse(loggedInUserResponse, request, newUser);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }*/



        return ResponseEntity
                .ok(registerService.createNewUser(new UserDTO(newUser)));
    }

    public ResponseEntity<SignedUpUserDetailsResponse> validateAndCreateUserResponse(SignedUpUserDetailsResponse signedUpUserDetailsResponse,
                                                                                     HttpServletRequest request,
                                                                                     UserRegistrationRequestModel newUser) {
        if (signedUpUserDetailsResponse.getUserId() == null) return ResponseEntity.status(409).body(signedUpUserDetailsResponse);
        return ResponseEntity.status(201).body(signedUpUserDetailsResponse);
    }
}
