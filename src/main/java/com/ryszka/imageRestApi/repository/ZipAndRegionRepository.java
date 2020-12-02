package com.ryszka.imageRestApi.repository;

import com.ryszka.imageRestApi.persistenceEntities.Country;
import com.ryszka.imageRestApi.persistenceEntities.ZipAndRegionEntity;
import com.ryszka.imageRestApi.security.DBQueryStatements;
import com.ryszka.imageRestApi.util.mapper.dbMappers.RegionsQueryAttribute;
import com.ryszka.imageRestApi.util.mapper.dbMappers.ZipCodesAndCitiesByRegion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Repository
@Transactional
public interface ZipAndRegionRepository extends CrudRepository<ZipAndRegionEntity, Integer> {
    @Async
    @Query(value = DBQueryStatements.ZIPCODE_AND_CITIES_LIKE_ZIPCODE, nativeQuery = true)
    CompletableFuture<List<ZipCodesAndCitiesByRegion>> getRegions(@Param("zipcode") String zipcode,
                                                                  @Param("region") String region);

    @Async
    @Query(value = DBQueryStatements.GET_ZIPCODES_BY_REGION, nativeQuery = true)
    CompletableFuture<List<ZipCodesAndCitiesByRegion>> getAllRegions(@Param("region") String region);

    @Async
    @Query(value = DBQueryStatements.GET_REGIONS, nativeQuery = true)
    CompletableFuture<List<RegionsQueryAttribute>> getAllByCountries(@Param("country") String country);

    ZipAndRegionEntity getByRegion(String region);

    RegionsQueryAttribute getByCountry(Country country);

    ZipAndRegionEntity getByZipcode(String zipcode);
}
