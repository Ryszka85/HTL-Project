package com.ryszka.imageRestApi.util.mapper.mapStrategies;

import com.ryszka.imageRestApi.persistenceEntities.UserEntity;
import com.ryszka.imageRestApi.service.dto.UserDTO;
import com.ryszka.imageRestApi.util.IDHashGenerator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.function.Function;

public class UserDTOToUserEntity implements MapStrategy<UserDTO, UserEntity> {
    private final BCryptPasswordEncoder passwordEncoder;
    private final Function<UserDTO, String> setUserName = userDTO -> userDTO.getUsername() == null ?
            userDTO.getEmail().substring(0, userDTO.getEmail().indexOf('@')) :
            userDTO.getUsername();

    public UserDTOToUserEntity(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserEntity map(UserDTO source) {
        UserEntity entity = new UserEntity();
        IDHashGenerator.getHash(entity::setUserId);
        entity.setFirstName(source.getFirstName());
        entity.setLastName(source.getLastName());
        entity.setEmail(source.getEmail());
        entity.setPassword(passwordEncoder.encode(source.getPassword()));
        entity.setUsername(setUserName.apply(source));
        return entity;
    }
}
