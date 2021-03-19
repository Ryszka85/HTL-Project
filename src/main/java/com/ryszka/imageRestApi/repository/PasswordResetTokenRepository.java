package com.ryszka.imageRestApi.repository;

import com.ryszka.imageRestApi.persistenceEntities.PasswordResetTokenEntity;
import com.ryszka.imageRestApi.persistenceEntities.UserEntity;
import org.springframework.data.repository.CrudRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetTokenEntity, Integer> {
    Optional<PasswordResetTokenEntity> getByTokenId(String tokenId);

    Optional<PasswordResetTokenEntity> getByUserEntity(UserEntity userEntity);

    Optional<PasswordResetTokenEntity> getByToken(String token);


}
