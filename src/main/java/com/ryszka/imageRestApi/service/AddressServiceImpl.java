package com.ryszka.imageRestApi.service;

import com.ryszka.imageRestApi.repository.ZipAndRegionRepository;
import com.ryszka.imageRestApi.util.mapper.dbMappers.RegionsQueryAttribute;
import com.ryszka.imageRestApi.util.mapper.dbMappers.ZipCodesAndCitiesByRegion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class AddressServiceImpl implements AddressService{
    private final Logger logger = LoggerFactory.getLogger(AddressServiceImpl.class);
    private final ZipAndRegionRepository zipAndRegionRepository;

    public AddressServiceImpl(ZipAndRegionRepository zipAndRegionRepository) {
        this.zipAndRegionRepository = zipAndRegionRepository;
    }

    @Override
    public CompletableFuture<List<ZipCodesAndCitiesByRegion>> validateAddress(String zipcode, String region) {
        logger.info("Preparing address validation query for parameter {}", zipcode);
        return zipAndRegionRepository.getRegions(zipcode, region);
    }

    @Override
    public CompletableFuture<List<ZipCodesAndCitiesByRegion>> getZipcodesFromRegion(String region) {
        return zipAndRegionRepository.getAllRegions(region);
    }

     

    @Override
    public CompletableFuture<List<RegionsQueryAttribute>> getOnlyRegions(String countryName) {
        return zipAndRegionRepository.getAllByCountries(countryName);
    }
}
