package com.ryszka.imageRestApi.service.serviceV2.writeService;

import com.ryszka.imageRestApi.dao.UserDAO;
import com.ryszka.imageRestApi.errorHandling.EntityNotFoundException;
import com.ryszka.imageRestApi.errorHandling.ErrorMessages;
import com.ryszka.imageRestApi.service.dto.ImageDTO;
import com.ryszka.imageRestApi.persistenceEntities.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Transactional
@Service
public class AddToUserLibraryService {
    public static final int PAGE_LIMIT_30 = 30;
    private final DatabaseAndFTPStorageService storage;
    private final UserDAO userDAO;
    private final Logger logger = LoggerFactory.getLogger(AddToUserLibraryService.class);


    public AddToUserLibraryService(DatabaseAndFTPStorageService storage,
                                   UserDAO userDAO) {
        this.storage = storage;
        this.userDAO = userDAO;
    }

    public void addImageToUserLibrary(ImageDTO imageDTO) {
        logger.info("Starting [ uploadImage ] ... {}", imageDTO);
        Optional<UserEntity> byUserIdQueryOpt = userDAO.findUserEntityByUserId(imageDTO.getUserId());
        UserEntity userEntity = byUserIdQueryOpt.orElseThrow(() ->
                new EntityNotFoundException(
                        ErrorMessages.NOT_FOUND_BY_EID.getMessage()));
        imageDTO.setUserEntity(userEntity);
        storage.storeToDbAndFTPInTransaction(imageDTO);
    }
}
