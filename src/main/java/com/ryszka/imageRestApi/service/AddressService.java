package com.ryszka.imageRestApi.service;

import com.ryszka.imageRestApi.util.mapper.dbMappers.RegionsQueryAttribute;
import com.ryszka.imageRestApi.util.mapper.dbMappers.ZipCodesAndCitiesByRegion;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface AddressService {
    CompletableFuture<List<ZipCodesAndCitiesByRegion>> validateAddress(String zipcode, String region);

    CompletableFuture<List<ZipCodesAndCitiesByRegion>> getZipcodesFromRegion(String countryName);

    CompletableFuture<List<RegionsQueryAttribute>> getOnlyRegions(String countryName);

}
