package com.ryszka.imageRestApi.persistenceEntities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "user_address")
public class UserAddressEntity implements Serializable {
    private static final long serialVersionUID = -5217137074605538925L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "id_Address")
    private String addressId;
    private String street;
    @JoinColumn(name = "id_user")
    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JsonBackReference
    private UserEntity userEntity;
    @JoinColumn(name = "zip_region_id")
    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JsonBackReference
    private ZipAndRegionEntity zipAndRegionEntity;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public ZipAndRegionEntity getZipAndRegionEntity() {
        return zipAndRegionEntity;
    }

    public void setZipAndRegionEntity(ZipAndRegionEntity zipAndRegionEntity) {
        this.zipAndRegionEntity = zipAndRegionEntity;
    }
}
