package com.ryszka.imageRestApi.controller.readController;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.OAuth2Credentials;
import com.ryszka.imageRestApi.security.AppConfigProperties;
import com.ryszka.imageRestApi.service.SearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;

@RestController
@RequestMapping(value = "query")
public class SearchController {
    private SearchService searchService;
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }


    @GetMapping(value = "{pMethod}/{params}")
    public ResponseEntity<Object> getImagesFromUserGallery(@PathVariable String[] params,
                                                           @PathVariable String pMethod) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, IOException {
        Class<String>[] argsClass = new Class[params.length];
        Arrays.fill(argsClass, String.class);
        Method method = SearchService.class.getDeclaredMethod(pMethod, argsClass);
        System.out.println(params);
        return ResponseEntity.ok(method.invoke(searchService, params));
    }
}
