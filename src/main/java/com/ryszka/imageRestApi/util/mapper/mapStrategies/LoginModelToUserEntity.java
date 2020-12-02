package com.ryszka.imageRestApi.util.mapper.mapStrategies;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.ryszka.imageRestApi.persistenceEntities.UserEntity;
import com.ryszka.imageRestApi.util.IDHashGenerator;
import com.ryszka.imageRestApi.viewModels.request.UserLoginRequest;
import org.apache.commons.lang3.RandomUtils;
import org.hibernate.id.UUIDGenerator;
import org.springframework.beans.propertyeditors.UUIDEditor;

import java.util.UUID;

public class LoginModelToUserEntity implements MapStrategy<UserLoginRequest, UserEntity> {
    @Override
    public UserEntity map(UserLoginRequest source) {
        UserEntity entity = new UserEntity();
        IDHashGenerator.getHash(entity::setUserId);
        entity.setFirstName(source.getFirstName());
        entity.setLastName(source.getLastName());
        entity.setEmail(source.getEmail());
        entity.setPassword(source.getPassword());
        entity.setUsername(source.getEmail().substring(0, source.getEmail().indexOf('@')));
        entity.setProfilePath(source.getProfileImg());
        entity.setLoginType("GOOGLE");
        return entity;
    }
}
