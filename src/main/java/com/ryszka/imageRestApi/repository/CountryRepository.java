package com.ryszka.imageRestApi.repository;

import com.ryszka.imageRestApi.persistenceEntities.Country;
import org.springframework.data.repository.CrudRepository;

public interface CountryRepository extends CrudRepository<Country, Integer> {
}
