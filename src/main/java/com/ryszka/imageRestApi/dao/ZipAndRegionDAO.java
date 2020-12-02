package com.ryszka.imageRestApi.dao;

import com.ryszka.imageRestApi.persistenceEntities.ZipAndRegionEntity;
import com.ryszka.imageRestApi.repository.ZipAndRegionRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ZipAndRegionDAO {
    private final ZipAndRegionRepository zipAndRegionRepository;

    public ZipAndRegionDAO(ZipAndRegionRepository zipAndRegionRepository) {
        this.zipAndRegionRepository = zipAndRegionRepository;

    }

    public Optional<ZipAndRegionEntity> getByZipCode(String zipCode) {
        return Optional.ofNullable(
                zipAndRegionRepository.getByZipcode(zipCode));
    }
}
