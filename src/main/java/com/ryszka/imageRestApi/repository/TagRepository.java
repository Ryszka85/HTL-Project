package com.ryszka.imageRestApi.repository;

import com.ryszka.imageRestApi.persistenceEntities.TagEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface TagRepository extends PagingAndSortingRepository<TagEntity, Integer> {
    @Query(value = "select id, tag, tag_id from tags where tag like %:term%", nativeQuery = true)
    List<TagEntity> getTagsLikeTerm(@Param("term") String term);

    TagEntity findByTag(String tagName);

    List<TagEntity> findAllByTagIn(List<String> tagNames);

    TagEntity findByTagId(String tagId);

    List<TagEntity> findAllByTagIdIn(List<String> tagId);


}
