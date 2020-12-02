package com.ryszka.imageRestApi.security;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.ryszka.imageRestApi.viewModels.request.UserLoginRequest;

import java.io.IOException;
import java.util.Collections;

public class GoogleLogin {
    public GoogleLogin() {
    }

    public void validateToken(UserLoginRequest userLoginRequest) throws IOException {
        NetHttpTransport netHttpTransport = new NetHttpTransport();
        JacksonFactory instance = JacksonFactory.getDefaultInstance();
        GoogleIdTokenVerifier.Builder verifier = new GoogleIdTokenVerifier.Builder(netHttpTransport, instance)
                .setAudience(Collections.singletonList(AppConfigProperties.GOOGLE_ID));
        GoogleIdToken.parse(verifier.getJsonFactory(), userLoginRequest.getTokenId());
    }
}