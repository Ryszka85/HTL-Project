package com.ryszka.imageRestApi.viewModels.request;

public class ChangePasswordFromRedirectRequest {
    private String userId, password, tokenId;

    public ChangePasswordFromRedirectRequest() {
    }

    public ChangePasswordFromRedirectRequest(String userId, String password, String tokenId) {
        this.tokenId = tokenId;
        this.userId = userId;
        this.password = password;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
