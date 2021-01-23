package com.ryszka.imageRestApi.service.serviceV2.writeService;

import com.google.api.gax.rpc.InvalidArgumentException;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.ryszka.imageRestApi.config.FireBaseStorageConfig;
import com.ryszka.imageRestApi.dao.ImageDAO;
import com.ryszka.imageRestApi.dao.UserDAO;
import com.ryszka.imageRestApi.errorHandling.EntityAccessNotAllowedException;
import com.ryszka.imageRestApi.errorHandling.EntityNotFoundException;
import com.ryszka.imageRestApi.errorHandling.EntityPersistenceException;
import com.ryszka.imageRestApi.errorHandling.ErrorMessages;
import com.ryszka.imageRestApi.persistenceEntities.FtpPersistenceEntity;
import com.ryszka.imageRestApi.persistenceEntities.ImageEntity;
import com.ryszka.imageRestApi.persistenceEntities.PasswordResetTokenEntity;
import com.ryszka.imageRestApi.persistenceEntities.UserEntity;
import com.ryszka.imageRestApi.repository.GoogleCloudRepository;
import com.ryszka.imageRestApi.repository.PasswordResetTokenRepository;
import com.ryszka.imageRestApi.security.AppConfigProperties;
import com.ryszka.imageRestApi.service.dto.ImageDTO;
import com.ryszka.imageRestApi.util.ThumbnailProducer;
import com.ryszka.imageRestApi.util.imageScaler.*;
import com.ryszka.imageRestApi.viewModels.request.ChangeUserPasswordRequest;
import com.ryszka.imageRestApi.viewModels.request.DeleteAccountRequest;
import com.ryszka.imageRestApi.viewModels.request.UpdateUserDetailsRequest;
import com.ryszka.imageRestApi.viewModels.response.ChangePasswordResponse;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    public UpdateUserService(TransactionTemplate transactionTemplate,
                             UserDAO userDAO,
                             ImageDAO imageDAO,
                             FireBaseStorageConfig storageConfig,
                             GoogleCloudRepository googleCloudRepository,
                             BCryptPasswordEncoder bCryptPasswordEncoder, PasswordResetTokenRepository tokenRepository) {
        this.transactionTemplate = transactionTemplate;
        this.userDAO = userDAO;
        this.imageDAO = imageDAO;
        this.storageConfig = storageConfig;
        this.googleCloudRepository = googleCloudRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.passwordResetTokenRepository = tokenRepository;
    }


    /*public UpdateUserService(TransactionTemplate transactionTemplate, UserDAO userDAO, ImageDAO imageDAO, FireBaseStorageConfig storageConfig, GoogleCloudRepository googleCloudRepository) {
        this.transactionTemplate = transactionTemplate;
        this.userDAO = userDAO;
        this.imageDAO = imageDAO;
        this.storageConfig = storageConfig;
        this.googleCloudRepository = googleCloudRepository;
    }*/


    public ChangePasswordResponse changeUserPassword(ChangeUserPasswordRequest request) {
        UserEntity userEntity = this.userDAO.findUserEntityByUserId(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException(
                        ErrorMessages.NOT_FOUND_BY_EID.getMessage()));
        if (request.getOldPassword().length() == 0 || request.getOldPassword() != null){
            String encode = this.bCryptPasswordEncoder.encode(request.getOldPassword());
                if (!bCryptPasswordEncoder.matches(request.getOldPassword(), userEntity.getPassword())) {
                    return new ChangePasswordResponse(false,
                            "Provided password was invalid");
                } else {
                    userEntity.setPassword(bCryptPasswordEncoder.encode(request.getNewPassword()));
                    userDAO.saveUserEntity(userEntity);
                    return new ChangePasswordResponse(true,
                            "Password changed successfully");
                }
        }
        return new ChangePasswordResponse(false,
                "Provided password was invalid");
    }

    public void changeUserDetails(UpdateUserDetailsRequest request) {
        UserEntity userEntity = userDAO.findUserEntityByUserId(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException(
                        ErrorMessages.NOT_FOUND_BY_EID.getMessage()));
        userEntity.setEmail(getNotNullPropertyValue(request.getEmail(), userEntity.getEmail()));
        userEntity.setFirstName(getNotNullPropertyValue(request.getFirstName(), userEntity.getFirstName()));
        userEntity.setLastName(getNotNullPropertyValue(request.getLastName(), userEntity.getLastName()));
        userEntity.setPassword(getNotNullPropertyValue(request.getPassword(), userEntity.getPassword()));
        userEntity.setUsername(getNotNullPropertyValue(request.getUsername(), userEntity.getUsername()));
        userDAO.saveUserEntity(userEntity);
    }

    private String getNotNullPropertyValue(String when, String or) {
        return when != null ? when : or;
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

    public void deleteUserAccount(DeleteAccountRequest request) {
        UserEntity userEntity = this.userDAO.findUserEntityByUserId(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException(
                        ErrorMessages.NOT_FOUND_BY_EID.getMessage()));
        if (!this.bCryptPasswordEncoder.matches(request.getPassword(), userEntity.getPassword()))
            throw new EntityAccessNotAllowedException(ErrorMessages.INVALID_ARGUMENTS.getMessage());
        userEntity.getImageEntities().forEach(imageEntity -> {
            imageEntity.getTags().clear();
            imageEntity.getUserLikesList().clear();
        });
        this.userDAO.deleteUserAccount(userEntity);

    }
}
