package com.ryszka.imageRestApi.dao;

import com.ryszka.imageRestApi.persistenceEntities.UserEntity;
import com.ryszka.imageRestApi.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserDAO {
    private final Logger logger =
            LoggerFactory.getLogger(UserDAO.class);
    private final UserRepository userRepository;

    public UserDAO(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public Optional<UserEntity> findUserEntityByUserId(String userId) {
        logger.info("Attempting [ findUserEntityByUserId ] query...");
        return Optional.ofNullable(
                userRepository.findUserEntityByUserId(userId));
    }

    public Optional<List<UserEntity>> getAllInEmail(String email) {
        logger.info("Attempting [ getAllInEmail ] query...");
        return Optional.ofNullable(userRepository.findUserEntitiesByEmailLike(email));
    }

    public Optional<UserEntity> saveUserEntity(UserEntity userEntity) {
        logger.info("Attempting [ saveUserEntity ] query...");
        return Optional.of(this.userRepository.save(userEntity));
    }

    public Optional<UserEntity> findByEmail(String email) {
        logger.info("Attempting [ findByEmail ] query...");
        return Optional.ofNullable(this.userRepository.getByEmail(email));
    }

    public void deleteUserAccount(UserEntity user) {
        logger.info("Attempting [ deleteUserAccount ] action...");
        this.userRepository.delete(user);
    }
}
