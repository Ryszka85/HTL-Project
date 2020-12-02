package com.ryszka.imageRestApi.viewModels.response;

import java.io.Serializable;

public class SignedUpUserDetailsResponse implements Serializable {
    private static final long serialVersionUID = -608575456313402970L;
    private String userId, email;

    public SignedUpUserDetailsResponse() {
    }

    public SignedUpUserDetailsResponse(String userId, String email) {
        this.userId = userId;
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
