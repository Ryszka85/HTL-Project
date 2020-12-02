package com.ryszka.imageRestApi.service.serviceV2.readService;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.ryszka.imageRestApi.config.FireBaseStorageConfig;
import com.ryszka.imageRestApi.dao.ImageDAO;
import com.ryszka.imageRestApi.dao.SessionDAO;
import com.ryszka.imageRestApi.dao.TagDAO;
import com.ryszka.imageRestApi.dao.UserDAO;
import com.ryszka.imageRestApi.errorHandling.EntityAccessNotAllowedException;
import com.ryszka.imageRestApi.errorHandling.EntityNotFoundException;
import com.ryszka.imageRestApi.persistenceEntities.*;
import com.ryszka.imageRestApi.security.AppConfigProperties;
import com.ryszka.imageRestApi.util.PathGenerator;

import com.ryszka.imageRestApi.util.SortImageViewModels;
import com.ryszka.imageRestApi.util.mapper.mapStrategies.ImageEntitiesToImageRespModels;
import com.ryszka.imageRestApi.util.mapper.mapStrategies.ImageEntityToImageRespModel;
import com.ryszka.imageRestApi.util.mapper.mapStrategies.UserEntityToImageRespModels;
import com.ryszka.imageRestApi.util.mapper.ObjectMapper;
import com.ryszka.imageRestApi.util.mapper.mapStrategies.UserEntityToUserDetailsResponseModel;
import com.ryszka.imageRestApi.viewModels.request.GetImagesByTagRequest;
import com.ryszka.imageRestApi.viewModels.response.UserDetailsResponseModel;
import com.ryszka.imageRestApi.viewModels.response.UserImageViewModel;
import com.ryszka.imageRestApi.errorHandling.ErrorMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReadUserLibraryService {
    private final Logger logger =
            LoggerFactory.getLogger(ReadUserLibraryService.class);
    private final UserDAO userDAO;
    private final TagDAO tagDAO;
    private final ImageDAO imageDAO;
    private final SessionDAO sessionDAO;
    private final FireBaseStorageConfig storageConfig;

    public ReadUserLibraryService(UserDAO userDAO,
                                  TagDAO tagDAO,
                                  ImageDAO imageDAO,
                                  SessionDAO sessionDAO,
                                  FireBaseStorageConfig storageConfig) {
        this.userDAO = userDAO;
        this.tagDAO = tagDAO;
        this.imageDAO = imageDAO;
        this.sessionDAO = sessionDAO;
        this.storageConfig = storageConfig;
    }

    /*public ReadUserLibraryService(UserDAO userDAO, TagDAO tagDAO) {
        this.userDAO = userDAO;
        this.tagDAO = tagDAO;
    }*/

    public byte[] getImageBytesByImgId(String imageId, HttpServletRequest request) throws IOException {
        boolean isOwner = false;

        // search in db for the image
        ImageEntity imageEntity = this.imageDAO
                .getImageByImageId(imageId)
                .orElseThrow(() -> new EntityNotFoundException(
                        ErrorMessages.IMAGES_NOT_FOUND_BY_ID.getMessage()));

        // check if httpSessionId is present in db
        Optional<SessionEntity> userBySessionIDOpt = this.sessionDAO
                .findUserBySessionID(request.getSession().getId());

        // if yes then validate if user is owner
        if (userBySessionIDOpt.isPresent()) {
            isOwner = userBySessionIDOpt.stream()
                    .filter(sessionEntity -> sessionEntity.getPrincipal() != null && sessionEntity
                            .getPrincipal()
                            .equals(imageEntity.getUserEntity().getEmail())
                    )
                    .map(sessionEntity -> true)
                    .findFirst()
                    .isPresent();
        }

        // check if file exists in cloud
        // when yes then
        String originalFilePath = "original/" + imageEntity.getUserEntity().getUserId() + "/" + imageEntity.getName();
        if (isOwner || imageEntity.getIsPublic()) {
            Storage storage = storageConfig.initAndGetStorage();
            BlobId blobId = BlobId.of(AppConfigProperties.BUCKET_NAME, originalFilePath);
            return Optional.ofNullable(storage.get(blobId))
                    .orElseThrow(() -> new EntityNotFoundException("Invalid file path"))
                    .getContent();
        }
        throw new EntityAccessNotAllowedException(
                ErrorMessages.ENTITY_ACCESS_NOT_ALLOWED.getMessage());
    }

    public List<UserImageViewModel> getRelatedImagesByTagIds(List<String> tags) {
        Optional<List<ImageEntity>> relatedByTags = imageDAO.getRelatedByTagIds(tags);
        if (relatedByTags.isPresent()) {
            logger.info("Starting getRelatedImagesByTagIds...");
            List<ImageEntity> imageEntities = relatedByTags.get();
            imageEntities.forEach(imageEntity -> System.out.println(imageEntity.getName()));
            List<ImageEntity> publicImages = imageEntities.stream()
                    .filter(ImageEntity::getIsPublic)
                    .collect(Collectors.toList());
            return ObjectMapper.mapByStrategy(publicImages, new ImageEntitiesToImageRespModels());
        }
        return null;
    }


    public Optional<List<UserImageViewModel>> getUserImages(String userId) {
        Optional<UserEntity> userEntityOpt = userDAO.findUserEntityByUserId(userId);
        if (userEntityOpt.isEmpty())
            throw new EntityNotFoundException(
                    ErrorMessages.NOT_FOUND_BY_EID.getMessage() + userId);
        UserEntity userEntity = userEntityOpt.get();
        List<UserImageViewModel> output = ObjectMapper.mapByStrategy(
                userEntity,
                new UserEntityToImageRespModels()
        );
        return Optional.ofNullable(output);
    }

    public UserImageViewModel getImageByImageId(String imageId, HttpServletRequest servletRequest) {
        Optional<ImageEntity> imageByImageIdOpt = imageDAO.getImageByImageId(imageId);
        if (imageByImageIdOpt.isPresent()) {
            ImageEntity imageEntity = imageByImageIdOpt.get();
            UserEntity userEntity = imageEntity.getUserEntity();
            Optional<SessionEntity> userBySessionIDOpt = sessionDAO.findUserBySessionID(servletRequest.getSession().getId());
            boolean isOwner = userBySessionIDOpt.isPresent() &&
                    userBySessionIDOpt.get().getPrincipal() != null &&
                    userBySessionIDOpt.get().getPrincipal().equals(userEntity.getEmail());
            logger.info("Validating is owner : {} ", isOwner);
            logger.info("Validating is image public : {}", imageEntity.getIsPublic());
            if (isOwner || imageEntity.getIsPublic()) {
                // if user is owner(principal)
                logger.info("Preparing get image by imageId...");
                UserImageViewModel response =
                        ObjectMapper.mapByStrategy(imageEntity, new ImageEntityToImageRespModel());

                int isMobileHeaderVal = Integer.parseInt(servletRequest.getHeader("isMobile"));
                boolean isMobile = isMobileHeaderVal == 1;
                System.out.println("Is frontend mobile : " + isMobile);


                String imgPath = userEntity.getUserId() + "/" + response.getName();
                String path = isMobile ? "gallery/" + imgPath : "downloadView/" + imgPath;
                logger.info("Generating path for request : {}", path);

                System.out.println(path);
                response.setLink(PathGenerator.generateFileAccessLink(path));
                List<ImageEntity> likes = userEntity.getLikes();

                UserDetailsResponseModel ownerDetails =
                        ObjectMapper.mapByStrategy(userEntity, new UserEntityToUserDetailsResponseModel());
                ownerDetails.setLikes(ObjectMapper.mapByStrategy(likes, new ImageEntitiesToImageRespModels()));
                ownerDetails.setImages(ObjectMapper.mapByStrategy(userEntity.getImageEntities(), new ImageEntitiesToImageRespModels()));
                response.setUser(ownerDetails);
                return response;
            }
        }
        throw new EntityNotFoundException(ErrorMessages.INVALID_ARGUMENTS.getMessage());
    }


    public Optional<List<UserImageViewModel>> getImagesByTagNamePageable(GetImagesByTagRequest request, int pageReq) {
        Optional<TagEntity> resultOpt = this.tagDAO.getTagByName(request.getTagName());
        if (resultOpt.isEmpty()) return Optional.empty();
        TagEntity tagEntity = resultOpt.get();
        // TODO: 15.09.2020 Implement pageable in this method -> call imageDAO.getImagesByTagName()
        //  and implement pageable in repository
        /*PageRequest pageRequest = PageRequest.of(5, 5);
        Optional<List<ImageEntity>> imagesByTagName = imageDAO.getImagesByTagName(tagEntity, pageRequest);
        imagesByTagName
                .ifPresent(imageEntities -> System.out.println(imageEntities.size()));*/
        List<Integer> filterPossibleVal = List.of(7, 30, 365);
        List<ImageEntity> images = new ArrayList<>();
        if (request.getFilterByDays() != -1 && request.getFilterByDays() != 0)
            images = imageDAO.getImagesByTagFilteredByDays(request.getFilterByDays(), tagEntity.getTag());

        if (images.isEmpty() && filterPossibleVal.contains(request.getFilterByDays())) {
            int offset = filterPossibleVal.indexOf(request.getFilterByDays()) + 1;
            for (int i = offset; i < filterPossibleVal.size(); i++) {
                images = imageDAO.getImagesByTagFilteredByDays(filterPossibleVal.get(i), tagEntity.getTag());
                if (images.size() > 0) break;
            }
        }
        if (images.isEmpty() || request.getFilterByDays() == 0)
            images = tagEntity.getImageEntities();

        List<UserImageViewModel> userImageViewModels =
                ObjectMapper.mapByStrategy(images, new ImageEntitiesToImageRespModels());
        return Optional.of(SortImageViewModels.sortRequest(request, userImageViewModels));
    }

}
