package com.ryszka.imageRestApi.viewModels.response;

import com.ryszka.imageRestApi.persistenceEntities.Country;

public class CountryResponse {
    private String name;



    public CountryResponse() {
    }

    public CountryResponse(Country country) {
        this.name = country.getName();
    }

    public CountryResponse(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
