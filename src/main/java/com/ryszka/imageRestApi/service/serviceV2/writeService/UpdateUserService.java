package com.ryszka.imageRestApi.service.serviceV2.writeService;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.ryszka.imageRestApi.config.FireBaseStorageConfig;
import com.ryszka.imageRestApi.dao.ImageDAO;
import com.ryszka.imageRestApi.dao.UserDAO;
import com.ryszka.imageRestApi.errorHandling.EntityNotFoundException;
import com.ryszka.imageRestApi.errorHandling.EntityPersistenceException;
import com.ryszka.imageRestApi.errorHandling.ErrorMessages;
import com.ryszka.imageRestApi.persistenceEntities.FtpPersistenceEntity;
import com.ryszka.imageRestApi.persistenceEntities.ImageEntity;
import com.ryszka.imageRestApi.persistenceEntities.UserEntity;
import com.ryszka.imageRestApi.repository.GoogleCloudRepository;
import com.ryszka.imageRestApi.security.AppConfigProperties;
import com.ryszka.imageRestApi.service.dto.ImageDTO;
import com.ryszka.imageRestApi.util.ThumbnailProducer;
import com.ryszka.imageRestApi.util.imageScaler.*;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;

@Service
public class UpdateUserService {
    private final Logger logger =
            LoggerFactory.getLogger(UpdateUserService.class);
    private final TransactionTemplate transactionTemplate;
    private final UserDAO userDAO;
    private final ImageDAO imageDAO;
    private final FireBaseStorageConfig storageConfig;
    private final GoogleCloudRepository googleCloudRepository;

    public UpdateUserService(TransactionTemplate transactionTemplate, UserDAO userDAO, ImageDAO imageDAO, FireBaseStorageConfig storageConfig, GoogleCloudRepository googleCloudRepository) {
        this.transactionTemplate = transactionTemplate;
        this.userDAO = userDAO;
        this.imageDAO = imageDAO;
        this.storageConfig = storageConfig;
        this.googleCloudRepository = googleCloudRepository;
    }


    /*public UpdateUserService(TransactionTemplate transactionTemplate, UserDAO userDAO, ImageDAO imageDAO, FireBaseStorageConfig storageConfig) {
        this.transactionTemplate = transactionTemplate;
        this.userDAO = userDAO;
        this.imageDAO = imageDAO;
        this.storageConfig = storageConfig;
    }*/

    /*public UpdateUserService(TransactionTemplate transactionTemplate,
                             UserDAO userDAO, ImageDAO imageDAO) {
        this.transactionTemplate = transactionTemplate;
        this.userDAO = userDAO;
        this.imageDAO = imageDAO;
    }*/

    public void addUserLike(String imageId, String userId) {
        Optional<UserEntity> userEntityOpt = userDAO.findUserEntityByUserId(userId);
        Optional<ImageEntity> imageEntityOpt = imageDAO.getImageByImageId(imageId);
        if (userEntityOpt.isEmpty() || imageEntityOpt.isEmpty())
            throw new EntityNotFoundException(ErrorMessages.NOT_FOUND_BY_EID.getMessage());
        UserEntity userEntity = userEntityOpt.get();
        ImageEntity imageEntity = imageEntityOpt.get();
        userEntity.getLikes().add(imageEntity);
        userDAO.saveUserEntity(userEntity);
    }

    public void setUserProfileImage(ImageDTO imageDTO) {
        UserEntity userEntity = getUserEntityOrThrow(imageDTO);
        transactionTemplate.execute(tx -> {
            ImageResizer resizedImg = null;
            String path = "profiles/" + imageDTO.getPath();
            try {

                BufferedImage profileImg = ImageIO.read(imageDTO.getFile().getInputStream());
                if (profileImg.getWidth() > profileImg.getHeight()) {
                    logger.info("Starting to resize image " + profileImg.getWidth() + "...");
                    byte[] content = imageDTO.getContent();
                    resizedImg = new ResizeByIndividualResolution(96, 96, content);
                } else {
                    logger.info("Starting to resize image " + profileImg.getWidth() + "...");
                    byte[] content = imageDTO.getContent();
                    resizedImg = new ResizeDownByHeightForDownload(content, 500);


                }


                byte[] content = imageDTO.getContent();
                resizedImg = new ProcessImageForProfile(content);

                String originalFilename = imageDTO.getFile().getOriginalFilename();
                userEntity.setProfilePath(path + "/" + originalFilename);
                googleCloudRepository.storeImage(path, originalFilename, resizedImg.resize());
                userDAO.saveUserEntity(userEntity)
                        .orElseThrow(() -> new EntityPersistenceException(
                                ErrorMessages.SAVE_TO_DB_ERROR.name()
                        ));


                /*ByteArrayOutputStream os = new ByteArrayOutputStream();
                ThumbnailProducer.createThumbnail(imageDTO.getFile().getInputStream());*/
                /*storageConfig.initFireBaseStorage(storage -> {
                    BlobId blobId = BlobId.of(AppConfigProperties.BUCKET_NAME, path);
                    BlobInfo info = BlobInfo.newBuilder(blobId).setContentType("image/jpeg").build();
                    storage.create(info, resizedImg.resize());
                });*/
                return true;



                /*FtpStorageService storageService = new FtpStorageService();
                return storageService.makeDirAndSaveFileToFileSystem(
                        new FtpPersistenceEntity(path, imageDTO.getFile().getInputStream()));*/
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        });
    }

    private UserEntity getUserEntityOrThrow(ImageDTO imageDTO) {
        return userDAO.findUserEntityByUserId(imageDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException(
                        ErrorMessages.NOT_FOUND_BY_EID.getMessage()
                ));
    }
}
