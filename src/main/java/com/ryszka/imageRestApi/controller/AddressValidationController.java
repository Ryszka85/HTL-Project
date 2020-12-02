package com.ryszka.imageRestApi.controller;

import com.ryszka.imageRestApi.persistenceEntities.Country;
import com.ryszka.imageRestApi.viewModels.response.CountryResponse;
import com.ryszka.imageRestApi.repository.CountryRepository;
import com.ryszka.imageRestApi.service.AddressService;
import com.ryszka.imageRestApi.util.mapper.dbMappers.RegionsQueryAttribute;
import com.ryszka.imageRestApi.util.mapper.dbMappers.ZipCodesAndCitiesByRegion;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Spliterator;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("addresses")
public class AddressValidationController {
    private final AddressService addressService;
    private final CountryRepository countryRepository;

    public AddressValidationController(AddressService addressService, CountryRepository countryRepository) {
        this.addressService = addressService;
        this.countryRepository = countryRepository;
    }


    // TODO: 21.08.2020 Implement profile-image resource!! 

    @GetMapping(value = "get/all/countries")
    public List<CountryResponse> allCountries() {
        Spliterator<Country> spliterator = this.countryRepository
                .findAll().spliterator();
        return StreamSupport
                .stream(spliterator, false)
                .map(CountryResponse::new)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "regions/{countryName}")
    public CompletableFuture<List<RegionsQueryAttribute>> getRegionsByCountry(@PathVariable String countryName) {
        return addressService.getOnlyRegions(countryName);
    }


    @GetMapping(value = "zipcodes/{region}")
    public CompletableFuture<List<ZipCodesAndCitiesByRegion>> getZipCodesByRegion(@PathVariable String region) {
        System.out.println("Hallo?");
        return addressService.getZipcodesFromRegion(region);
    }


    @GetMapping(value = "zip/cities/{region}/{zipcode}")
    public CompletableFuture<List<ZipCodesAndCitiesByRegion>> getRegions(@PathVariable String zipcode,
                                                                         @PathVariable String region) {
        return addressService.validateAddress(zipcode, region);
    }

}
