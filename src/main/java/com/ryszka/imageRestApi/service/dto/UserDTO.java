package com.ryszka.imageRestApi.service.dto;

import com.ryszka.imageRestApi.viewModels.request.UserRegistrationRequestModel;

public class UserDTO {
    private String userId;
    private String firstName, lastName;
    private String email, password, username;
    private AddressDTO addressDTO;

    public UserDTO() {
    }

    public UserDTO(String userId,
                   String firstName, String lastName,
                   String email, String password) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public UserDTO(UserRegistrationRequestModel newUser) {
        this.firstName = newUser.getFirstName();
        this.lastName = newUser.getLastName();
        this.email = newUser.getEmail();
        this.password = newUser.getPassword();
        /*this.addressDTO = new AddressDTO(newUser);*/
        this.username = newUser.getUsername();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public AddressDTO getAddressDTO() {
        return addressDTO;
    }

    public void setAddressDTO(AddressDTO addressDTO) {
        this.addressDTO = addressDTO;
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
}
