package com.ryszka.imageRestApi.persistenceEntities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity(name = "password_reset_token")
public class PasswordResetTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "id_token")
    private String tokenId;
    private String token;
    @JoinColumn(name = "user_id")
    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JsonBackReference
    private UserEntity userEntity;
    @Column(name = "was_validated")
    private boolean wasValidated;
    @Column(name = "processed_by_view")
    private boolean processedByView;

    public PasswordResetTokenEntity() {
    }

    public void setProcessedByView(boolean processedByView) {
        this.processedByView = processedByView;
    }

    public boolean getIsProcessedByView() {
        return this.processedByView;
    }

    public PasswordResetTokenEntity(String tokenId, String token) {
        this.tokenId = tokenId;
        this.token = token;
    }

    public PasswordResetTokenEntity(String tokenId, String token,
                                    UserEntity userEntity, boolean wasValidated,
                                    boolean processedByView) {
        this.tokenId = tokenId;
        this.token = token;
        this.userEntity = userEntity;
        this.wasValidated = wasValidated;
        this.processedByView = processedByView;
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
