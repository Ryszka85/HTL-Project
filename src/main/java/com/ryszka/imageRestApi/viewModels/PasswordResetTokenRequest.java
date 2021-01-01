package com.ryszka.imageRestApi.viewModels;

public class PasswordResetTokenRequest {
    private String email;

    public PasswordResetTokenRequest() {
    }

    public PasswordResetTokenRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
