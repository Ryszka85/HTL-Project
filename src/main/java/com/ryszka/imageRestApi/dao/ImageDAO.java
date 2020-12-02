package com.ryszka.imageRestApi.dao;

import com.ryszka.imageRestApi.errorHandling.ErrorMessages;
import com.ryszka.imageRestApi.persistenceEntities.ImageEntity;
import com.ryszka.imageRestApi.persistenceEntities.TagEntity;
import com.ryszka.imageRestApi.persistenceEntities.UserEntity;
import com.ryszka.imageRestApi.repository.ImageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Optional;

@Service
public class ImageDAO {
    private final ImageRepository imageRepository;
    private final Logger logger = LoggerFactory.getLogger(ImageDAO.class);

    public ImageDAO(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }



    public boolean saveImage(ImageEntity imageEntity) {
        try {
            logger.info("Attempting to store image {}", imageEntity.getName());
            ImageEntity savedEntity = this.imageRepository.save(imageEntity);
            return true;
        } catch (Exception e) {
            logger.error(ErrorMessages.SAVE_TO_DB_ERROR.getMessage() + " {}", imageEntity.getName());
            throw new PersistenceException(ErrorMessages.SAVE_TO_DB_ERROR.getMessage());
        }
    }


    public List<ImageEntity> getImagesByTagFilteredByDays(int days, String tag) {
        return imageRepository.getImagesByTagFromOneWeek(days, tag);
    }

    public Optional<List<ImageEntity>> getRelatedByTagIds(List<String> tags) {
        return Optional.ofNullable(imageRepository.getRelatedImages(tags));
    }

    public Optional<List<ImageEntity>> findByUserEntityAndIsPublic(UserEntity userEntity, boolean isPublic) {
        return Optional.ofNullable(imageRepository.findByUserEntityAndIsPublic(userEntity, isPublic));
    }

    public Optional<ImageEntity> getImagesByUserId(String imageId) {
        return Optional.ofNullable(
                this.imageRepository.findByImageId(imageId));
    }

    public Optional<List<ImageEntity>> getImagesByUserEntity(UserEntity userEntity) {
        return this.imageRepository
                .findAllByUserEntity(userEntity);
    }

    public void deleteImageByImageIdAndUserEntity(String imageId, UserEntity userEntity) {
        imageRepository.deleteByImageIdAndUserEntity(imageId, userEntity);
    }

    public Optional<List<ImageEntity>> getImagesByTagName(TagEntity tagName, Pageable pageable) {
        return Optional.ofNullable(this.imageRepository.findAllByTagsEquals(tagName, pageable));
    }

    public Optional<ImageEntity> getImageByImageId(String imageId) {
        return Optional.ofNullable(this.imageRepository.findByImageId(imageId));
    }
}
