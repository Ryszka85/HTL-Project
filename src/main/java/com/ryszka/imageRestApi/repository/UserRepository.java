package com.ryszka.imageRestApi.repository;

import com.ryszka.imageRestApi.persistenceEntities.UserEntity;
import com.ryszka.imageRestApi.security.DBQueryStatements;
import com.ryszka.imageRestApi.util.mapper.dbMappers.UserNameAndIdBySession;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Transactional
@Repository
public interface UserRepository extends CrudRepository<UserEntity, Integer> {
    @Async
    CompletableFuture<UserEntity> findByUserId(String userId);

    @Async
    CompletableFuture<UserEntity> findByEmail(String email);

    UserEntity findUserEntityByUserId(String userId);

    UserEntity getByEmail(String email);


    List<UserEntity> findByEmailLike(String email);

    @Query(value = DBQueryStatements.likeEmail, nativeQuery = true)
    List<UserEntity> findUserEntitiesByEmailLike(@Param("email") String email);

    @Async
    @Query(value = DBQueryStatements.GET_ENTITY_LIKE_FIRST_NAME_AT, nativeQuery = true)
    CompletableFuture<UserNameAndIdBySession> findByFirstNameAt(@Param("firstNameAt") String firstNameAt);

}
