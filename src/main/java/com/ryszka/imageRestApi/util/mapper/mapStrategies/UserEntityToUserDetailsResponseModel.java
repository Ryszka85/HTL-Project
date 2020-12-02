package com.ryszka.imageRestApi.util.mapper.mapStrategies;

import com.ryszka.imageRestApi.viewModels.response.UserDetailsResponseModel;
import com.ryszka.imageRestApi.persistenceEntities.UserEntity;
import com.ryszka.imageRestApi.util.PathGenerator;

public class UserEntityToUserDetailsResponseModel implements MapStrategy<UserEntity, UserDetailsResponseModel> {

    @Override
    public UserDetailsResponseModel map(UserEntity source) {
        return new UserDetailsResponseModel(
                source.getFirstName(),
                source.getLastName(),
                source.getUserId(),
                source.getEmail(),
                source.getUsername(),
                source.getLoginType().equals("GOOGLE") ? source.getProfilePath() :
                        PathGenerator.generateFileAccessLink(source.getProfilePath()) != null ?
                                PathGenerator.generateFileAccessLink(source.getProfilePath()) :
                                null
        );
    }
}
