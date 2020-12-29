package com.ryszka.imageRestApi.persistenceEntities;

import com.fasterxml.jackson.annotation.JsonBackReference;


import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity(name = "users")
public class UserEntity implements Serializable {
    private static final long serialVersionUID = 3443800604301465813L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "user_id")
    private String userId;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    private String email;
    private String password;
    private String username;
    @Column(name = "profile_img_path")
    private String profilePath;
    private boolean inactive = false;
    @Column(name = "account_verified")
    private boolean accountVerified;
    @JsonBackReference
    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ImageEntity> imageEntities;
    @JoinColumn(name = "id_address")
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    private UserAddressEntity userAddressEntity;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "liked_user_image",
            joinColumns = {@JoinColumn(name = "id_user")},
            inverseJoinColumns = {@JoinColumn(name = "id_image")})
    private List<ImageEntity> likes;

    @Column(name = "login_type")
    private String loginType;

    @JoinColumn(name = "token_id")
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    private AccountVerificationTokenEntity accountVerificationToken;

    public UserEntity() {
    }


    public UserAddressEntity getUserAddressEntity() {
        return userAddressEntity;
    }

    public void setUserAddressEntity(UserAddressEntity userAddressEntity) {
        this.userAddressEntity = userAddressEntity;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public List<ImageEntity> getLikes() {
        return likes;
    }

    public void setLikes(List<ImageEntity> likes) {
        this.likes = likes;
    }

    public boolean isInactive() {
        return inactive;
    }

    public void setInactive(boolean inactive) {
        this.inactive = inactive;
    }


    public List<ImageEntity> getImageEntities() {
        return imageEntities;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    public UserAddressEntity getUserAddress() {
        return userAddressEntity;
    }

    public void setUserAddress(UserAddressEntity userEntity) {
        this.userAddressEntity = userEntity;
    }

    public void setImageEntities(List<ImageEntity> imageEntities) {
        this.imageEntities = imageEntities;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public boolean isAccountVerified() {
        return accountVerified;
    }

    public void setAccountVerified(boolean accountVerified) {
        this.accountVerified = accountVerified;
    }

    public AccountVerificationTokenEntity getAccountVerificationToken() {
        return accountVerificationToken;
    }

    public void setAccountVerificationToken(AccountVerificationTokenEntity accountVerificationToken) {
        this.accountVerificationToken = accountVerificationToken;
    }
}
