package com.ryszka.imageRestApi.viewModels.request;

public class TokenIdRequest {
    private String tokenId;

    public TokenIdRequest() {
    }

    public TokenIdRequest(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }
}
