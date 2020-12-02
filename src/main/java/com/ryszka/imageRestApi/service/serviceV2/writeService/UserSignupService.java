package com.ryszka.imageRestApi.service.serviceV2.writeService;

import com.ryszka.imageRestApi.dao.UserDAO;
import com.ryszka.imageRestApi.dao.ZipAndRegionDAO;
import com.ryszka.imageRestApi.errorHandling.AddressNotFoundException;
import com.ryszka.imageRestApi.errorHandling.EntityPersistenceException;
import com.ryszka.imageRestApi.errorHandling.ErrorMessages;
import com.ryszka.imageRestApi.errorHandling.UserRegistrationFailedException;
import com.ryszka.imageRestApi.persistenceEntities.UserAddressEntity;
import com.ryszka.imageRestApi.persistenceEntities.UserEntity;
import com.ryszka.imageRestApi.util.mapper.ObjectMapper;
import com.ryszka.imageRestApi.util.mapper.mapStrategies.SetAddressToUserEntity;
import com.ryszka.imageRestApi.util.mapper.mapStrategies.UserDTOToUserEntity;
import com.ryszka.imageRestApi.viewModels.response.SignedUpUserDetailsResponse;
import com.ryszka.imageRestApi.persistenceEntities.ZipAndRegionEntity;
import com.ryszka.imageRestApi.service.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/*import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;*/

import java.util.Optional;

@Service
@Transactional
public class UserSignupService {
    private final UserDAO userDAO;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ZipAndRegionDAO zipAndRegionDAO;
    private final Logger logger = LoggerFactory.getLogger(UserSignupService.class);

    public UserSignupService(UserDAO userDAO,
                             BCryptPasswordEncoder passwordEncoder,
                             ZipAndRegionDAO zipAndRegionDAO) {
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
        this.zipAndRegionDAO = zipAndRegionDAO;
    }

    public SignedUpUserDetailsResponse createNewUser(UserDTO userDTO) {
        logger.info("Attempting to create new user {}", userDTO);
        Optional<UserEntity> byEmailOpt = this.userDAO.findByEmail(userDTO.getEmail());

        if (byEmailOpt.isPresent()) throw new UserRegistrationFailedException(ErrorMessages.USER_SIGNUP_FAILED.getMessage() +
                System.lineSeparator() + ErrorMessages.USER_ALREADY_EXISTS.getMessage());

        UserEntity newUser = ObjectMapper.mapByStrategy(userDTO, new UserDTOToUserEntity(passwordEncoder));
        /*UserAddressEntity userAddressEntity = ObjectMapper.mapByStrategy(userDTO, new SetAddressToUserEntity());
        newUser.setUserAddress(userAddressEntity);
        Optional<ZipAndRegionEntity> byZipCodeOpt = zipAndRegionDAO.getByZipCode(userDTO.getAddressDTO().getZipcode());

        if (byZipCodeOpt.isEmpty()) throw new AddressNotFoundException(
                ErrorMessages.ADDRESS_NOT_FOUND.getMessage());

        ZipAndRegionEntity zipAndRegionEntity = byZipCodeOpt.get();
        userAddressEntity.setZipAndRegionEntity(zipAndRegionEntity);
        newUser.setUserAddress(userAddressEntity);*/
        newUser.setLoginType("STANDARD");
        try {
            userDAO.saveUserEntity(newUser);
            return new SignedUpUserDetailsResponse(newUser.getUserId(), newUser.getEmail());
        } catch (Exception e) {
            throw new EntityPersistenceException(ErrorMessages.USER_SIGNUP_FAILED.getMessage());
        }
    }

}
