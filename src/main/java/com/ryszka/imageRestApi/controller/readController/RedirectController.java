package com.ryszka.imageRestApi.controller.readController;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("redirect")
public class RedirectController {

    public RedirectController() {
    }

    @GetMapping
    public ResponseEntity<Object> redirectToUrl(String url) throws URISyntaxException {
        URI redirectUrl = new URI(url);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(redirectUrl);
        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
    }
}
