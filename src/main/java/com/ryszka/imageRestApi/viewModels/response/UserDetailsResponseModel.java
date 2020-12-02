package com.ryszka.imageRestApi.viewModels.response;

import com.ryszka.imageRestApi.persistenceEntities.UserEntity;
import com.ryszka.imageRestApi.security.AppConfigProperties;

import java.io.Serializable;
import java.util.List;

public class UserDetailsResponseModel implements Serializable {
    private static final long serialVersionUID = 8348389384956451693L;
    private String firstName, lastName, userId, email, profileImgPath, username;
    private List<UserImageViewModel> images;
    private List<UserImageViewModel> likes;

    public UserDetailsResponseModel() {
    }

    public UserDetailsResponseModel(String firstName, String lastName,
                                    String userId, String email,
                                    String profileImgPath, String username,
                                    List<UserImageViewModel> images,
                                    List<UserImageViewModel> likes) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userId = userId;
        this.email = email;
        this.profileImgPath = profileImgPath;
        this.username = username;
        this.images = images;
        this.likes = likes;
    }

    public UserDetailsResponseModel(String firstName, String lastName,
                                    String userId, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userId = userId;
        this.email = email;
    }



    public UserDetailsResponseModel(String firstName, String lastName,
                                    String userId, String email,
                                    String username, String profileImgPath) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userId = userId;
        this.email = email;
        this.username = username;
        this.profileImgPath = profileImgPath;
    }

    public UserDetailsResponseModel(UserEntity userEntity) {
        this.firstName = userEntity.getFirstName();
        this.lastName = userEntity.getLastName();
        this.userId = userEntity.getUserId();
        this.email = userEntity.getEmail();
        this.profileImgPath = getProfileImgPath(userEntity);
        this.username = userEntity.getUsername();
    }

    private String getProfileImgPath(UserEntity userEntity) {
        return userEntity.getProfilePath() != null ?
                AppConfigProperties.FILE_PATH + userEntity.getProfilePath() :
                null;
    }

    public List<UserImageViewModel> getImages() {
        return images;
    }

    public void setImages(List<UserImageViewModel> images) {
        this.images = images;
    }

    public List<UserImageViewModel> getLikes() {
        return likes;
    }

    public void setLikes(List<UserImageViewModel> likes) {
        this.likes = likes;
    }

    public String getProfileImgPath() {
        return profileImgPath;
    }

    public void setProfileImgPath(String profileImgPath) {
        this.profileImgPath = profileImgPath;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
