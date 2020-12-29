package com.ryszka.imageRestApi.viewModels.request;

public class RenewAccountTokenRequest {
    private String email, password;

    public RenewAccountTokenRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public RenewAccountTokenRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
