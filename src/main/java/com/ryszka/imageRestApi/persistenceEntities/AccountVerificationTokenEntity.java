package com.ryszka.imageRestApi.persistenceEntities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ryszka.imageRestApi.persistenceEntities.UserEntity;

import javax.persistence.*;

@Entity(name = "account_verification_token")
public class AccountVerificationTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "id_token")
    private String tokenId;
    private String token;
    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY, mappedBy = "accountVerificationToken")
    @JsonBackReference
    private UserEntity userEntity;
    @Column(name = "was_validated")
    private boolean wasValidated;
    @Column(name = "processed_by_view")
    private boolean processedByView;

    public AccountVerificationTokenEntity() {
    }

    public void setProcessedByView(boolean processedByView) {
        this.processedByView = processedByView;
    }

    public boolean getIsProcessedByView() {
        return this.processedByView;
    }

    public AccountVerificationTokenEntity(String tokenId, String token) {
        this.tokenId = tokenId;
        this.token = token;
    }

    public boolean isWasValidated() {
        return wasValidated;
    }

    public void setWasValidated(boolean wasValidated) {
        this.wasValidated = wasValidated;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }
}
