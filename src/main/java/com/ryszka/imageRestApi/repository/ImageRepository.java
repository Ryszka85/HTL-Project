package com.ryszka.imageRestApi.repository;

import com.ryszka.imageRestApi.persistenceEntities.ImageEntity;
import com.ryszka.imageRestApi.persistenceEntities.TagEntity;
import com.ryszka.imageRestApi.persistenceEntities.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface ImageRepository extends PagingAndSortingRepository<ImageEntity, Integer> {

    @Query(value = "SELECT DISTINCT(image.image_id),image.id,  image.name , image.path, image.id_user, image.downloaded, image.is_public, image.url_reference, upload_Date" +
            " FROM image" +
            " INNER JOIN tagged_image on image.id = tagged_image.id_image" +
            " INNER JOIN tags on tags.id = tagged_image.id_tag" +
            " WHERE tags.tag_id IN (:tags) ", nativeQuery = true)
    List<ImageEntity> getRelatedImages(@Param("tags") List<String> tags);

    Optional<List<ImageEntity>> findAllByUserEntity(UserEntity userEntity);

    @Query(value = "SELECT image.id, image.image_id, image.name , image.path, image.id_user, image.downloaded, image.is_public, image.url_reference, upload_Date " +
            "FROM image " +
            "inner JOIN tagged_image on image.id = tagged_image.id_image " +
            "INNER JOIN tags ON tags.id = tagged_image.id_tag " +
            "WHERE upload_Date > DATE_SUB(CURRENT_DATE, INTERVAL :filter DAY) AND " +
            "tags.tag = :tag AND image.is_public = true", nativeQuery = true)
    List<ImageEntity> getImagesByTagFromOneWeek(@Param("filter") int filter, @Param("tag") String tag);

    ImageEntity findByImageId(String imageId);

    List<ImageEntity> findAllByTagsEquals(TagEntity tag, Pageable page);

    List<ImageEntity> findByUserEntityAndIsPublic(UserEntity userEntity, boolean isPublic);

    void deleteByImageIdAndUserEntity(String imageId, UserEntity userEntity);

}
