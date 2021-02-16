package com.ryszka.imageRestApi.service.dto;

import com.ryszka.imageRestApi.viewModels.request.UserRegistrationRequestModel;

import java.io.Serializable;

public class AddressDTO implements Serializable {
    private static final long serialVersionUID = -5619543238957058793L;
    private String street, zipcode, region, city;


    public AddressDTO() {
    }

    public AddressDTO(UserRegistrationRequestModel requestModel) {
        /*AddressDTO address = requestModel.getAddress();*/
        /*this.street = address.getStreet();
        this.zipcode = address.getZipcode();
        this.region = address.region;
        this.city = address.city;*/
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "AddressDTO{" +
                "street='" + street + '\'' +
                ", zipcode='" + zipcode + '\'' +
                ", region='" + region + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
