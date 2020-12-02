package com.ryszka.imageRestApi.util.mapper.mapStrategies;

import com.ryszka.imageRestApi.persistenceEntities.UserAddressEntity;
import com.ryszka.imageRestApi.service.dto.AddressDTO;
import com.ryszka.imageRestApi.service.dto.UserDTO;
import com.ryszka.imageRestApi.util.IDHashGenerator;

public class SetAddressToUserEntity implements MapStrategy<UserDTO, UserAddressEntity>{

    @Override
    public UserAddressEntity map(UserDTO source) {
        UserAddressEntity userAddressEntity = new UserAddressEntity();
        AddressDTO addressDTO = source.getAddressDTO();
        String street = addressDTO.getStreet();
        userAddressEntity.setStreet(street);
        IDHashGenerator.getHash(userAddressEntity::setAddressId);
        return userAddressEntity;
    }
}
