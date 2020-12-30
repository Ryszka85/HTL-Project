package com.ryszka.imageRestApi.repository;

import com.ryszka.imageRestApi.persistenceEntities.AccountVerificationTokenEntity;
import com.ryszka.imageRestApi.persistenceEntities.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AccountVerificationRepository extends CrudRepository<AccountVerificationTokenEntity, Integer> {

    AccountVerificationTokenEntity getByUserEntity(UserEntity userEntity);

    Optional<AccountVerificationTokenEntity> getAccountVerificationTokenEntityByTokenId(String tokenId);

    Optional<AccountVerificationTokenEntity> getByToken(String tokenId);


}
