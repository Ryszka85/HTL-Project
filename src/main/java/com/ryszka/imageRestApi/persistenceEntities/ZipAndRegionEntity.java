package com.ryszka.imageRestApi.persistenceEntities;


import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "regions_postals")
public class ZipAndRegionEntity implements Serializable {
    private static final long serialVersionUID = -3953427059593018230L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String zipcode;
    private String city;
    private String region;
    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_country")
    @JsonBackReference
    private Country country;

    public ZipAndRegionEntity() {
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipCode) {
        this.zipcode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
}
