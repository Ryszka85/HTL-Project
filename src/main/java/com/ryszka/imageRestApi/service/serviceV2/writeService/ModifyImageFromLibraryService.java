package com.ryszka.imageRestApi.service.serviceV2.writeService;

import com.ryszka.imageRestApi.dao.ImageDAO;
import com.ryszka.imageRestApi.dao.TagDAO;
import com.ryszka.imageRestApi.dao.UserDAO;
import com.ryszka.imageRestApi.errorHandling.EntityNotFoundException;
import com.ryszka.imageRestApi.errorHandling.ErrorMessages;
import com.ryszka.imageRestApi.persistenceEntities.ImageEntity;
import com.ryszka.imageRestApi.persistenceEntities.TagEntity;
import com.ryszka.imageRestApi.persistenceEntities.UserEntity;
import com.ryszka.imageRestApi.repository.ImageRepository;
import com.ryszka.imageRestApi.repository.TagRepository;
import com.ryszka.imageRestApi.service.dto.ImageDTO;
import com.ryszka.imageRestApi.service.dto.TagDTO;
import com.ryszka.imageRestApi.viewModels.response.UserImageViewModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ModifyImageFromLibraryService {
    private TagDAO tagDAO;
    private ImageDAO imageDAO;
    private UserDAO userDAO;
    private ImageRepository imageRepository;
    private TagRepository tagRepository;
    private final Logger logger =
            LoggerFactory.getLogger(ModifyImageFromLibraryService.class);

    public ModifyImageFromLibraryService(TagDAO tagDAO, ImageDAO imageDAO, UserDAO userDAO, ImageRepository imageRepository, TagRepository tagRepository) {
        this.tagDAO = tagDAO;
        this.imageDAO = imageDAO;
        this.userDAO = userDAO;
        this.imageRepository = imageRepository;
        this.tagRepository = tagRepository;
    }

    public void changeImageDetails(UserImageViewModel updateReq) {
        Optional<UserEntity> userEntityByUserIdOpt = this.userDAO.findUserEntityByUserId(updateReq.getUser().getUserId());
        userEntityByUserIdOpt.orElseThrow(() -> new EntityNotFoundException(ErrorMessages.NOT_FOUND_BY_EID.getMessage()));
        Optional<ImageEntity> imageByImageIdOpt = imageDAO.getImageByImageId(updateReq.getImageId());
        imageByImageIdOpt.orElseThrow(() -> new EntityNotFoundException(ErrorMessages.IMAGES_NOT_FOUND_BY_ID.getMessage()));
        ImageEntity imageEntity = imageByImageIdOpt.get();
        imageEntity.setUrlReference(updateReq.getLinkReference() != null ? updateReq.getLinkReference() : "null");
        imageEntity.setIsPublic(updateReq.getIsPublic());
        imageDAO.saveImage(imageEntity);
    }


    @Transactional
    public void deleteImageFromLibrary(UserImageViewModel request) {

        UserEntity userEntity = getUserEntityOrThrow(request.getUser().getUserId());
        Optional<ImageEntity> imageByImageId = this.imageDAO.getImageByImageId(request.getImageId());
        ImageEntity imageEntity = imageByImageId.orElseThrow(() -> new EntityNotFoundException("image could not be found"));

        imageEntity.getTags().clear();
        imageEntity.getUserLikesList().clear();
        imageRepository.save(imageEntity);
        imageRepository.delete(imageEntity);
        /*this.imageDAO.deleteImageByImageIdAndUserEntity(request.getImageId(), userEntity);*/
    }

    public void setTags(ImageDTO imageDTO) {
        System.out.println(imageDTO);
        if (imageDTO != null &&
                imageDTO.getTags() != null &&
                imageDTO.getUserId() != null) {
            UserEntity userEntity = getUserEntityOrThrow(imageDTO.getUserId());
            ImageEntity imageEntity = getImageEntityOrThrow(imageDTO);
            imageEntity.setUserEntity(userEntity);
            List<String> tagIDs = mapToIdList(imageDTO);
            getTagEntitiesOrThrow(tagIDs).forEach(tagEntity ->
                    imageEntity.getTags().add(tagEntity));
            imageDAO.saveImage(imageEntity);
        } else throw new IllegalArgumentException(
                ErrorMessages.INVALID_ARGUMENTS.getMessage());
    }

    public void deleteTagsFromImage(ImageDTO imageDTO) {
        System.out.println(imageDTO);
        if (imageDTO != null &&
                imageDTO.getTags() != null &&
                imageDTO.getUserId() != null) {
            UserEntity userEntity = getUserEntityOrThrow(imageDTO.getUserId());
            ImageEntity imageEntity = getImageEntityOrThrow(imageDTO);
            imageEntity.setUserEntity(userEntity);

            List<String> tagIDs = mapToIdList(imageDTO);
            List<TagEntity> tagEntities = getTagEntitiesOrThrow(tagIDs);
            List<TagEntity> filteredTagList = imageEntity.getTags()
                    .stream()
                    .filter(tagEntity -> tagEntities.stream()
                            .filter(tagEntity1 -> tagEntity1.getTagId()
                                    .equals(tagEntity.getTagId()))
                            .count() == 0)
                    .collect(Collectors.toList());
            filteredTagList.forEach(System.out::println);
            imageEntity.setTags(filteredTagList);
            imageDAO.saveImage(imageEntity);
            /*List<String> tagIDs = mapToIdList(imageDTO);
            getTagEntitiesOrThrow(tagIDs).forEach(tagEntity ->
                    imageEntity.getTags().add(tagEntity));
            imageDAO.saveImage(imageEntity);*/
        } else throw new IllegalArgumentException(
                ErrorMessages.INVALID_ARGUMENTS.getMessage());

    }


    private List<String> mapToIdList(ImageDTO imageDTO) {
        return imageDTO.getTags()
                .stream()
                .map(TagDTO::getTagId)
                .collect(Collectors.toList());
    }

    private List<TagEntity> getTagEntitiesOrThrow(List<String> tagIDs) {
        return tagDAO.getByTagIdList(tagIDs)
                .orElseThrow(() -> new EntityNotFoundException(
                        ErrorMessages.NOT_FOUND_BY_EID.getMessage()));
    }

    private ImageEntity getImageEntityOrThrow(ImageDTO imageDTO) {
        return imageDAO.getImagesByUserId(imageDTO.getImageId())
                .orElseThrow(() -> new EntityNotFoundException(
                        ErrorMessages.NOT_FOUND_BY_EID.getMessage()));
    }

    private UserEntity getUserEntityOrThrow(String userId) {
        return userDAO.findUserEntityByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        ErrorMessages.NOT_FOUND_BY_EID.getMessage()));
    }


}
