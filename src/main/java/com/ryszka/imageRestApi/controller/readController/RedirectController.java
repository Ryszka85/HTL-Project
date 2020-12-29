package com.ryszka.imageRestApi.controller.readController;

import org.apache.catalina.core.ApplicationSessionCookieConfig;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("redirect")
public class RedirectController {

    public RedirectController() {
    }

    @GetMapping
    public ResponseEntity<Object> redirectToUrl(String url, HttpServletResponse response) throws URISyntaxException {
        URI redirectUrl = new URI(url);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(redirectUrl);
        httpHeaders.add("tokenRefresh", "true");
        Cookie cookie = new Cookie("tokenRefresh", "true");
        response.addCookie(cookie);
        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
    }
}
