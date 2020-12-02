package com.ryszka.imageRestApi.dao;

import com.ryszka.imageRestApi.persistenceEntities.TagEntity;
import com.ryszka.imageRestApi.repository.TagRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagDAO {
    private final Logger logger =
            LoggerFactory.getLogger(TagDAO.class);
    private final TagRepository tagRepository;

    public TagDAO(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public Optional<List<TagEntity>> getTagsLikeSearchTerm(String term) {
        logger.info("Attempting [ getTagsLikeSearchTerm ] query...");
        return Optional.ofNullable(this.tagRepository.getTagsLikeTerm(term));
    }

    public Optional<TagEntity> getTagByName(String tagName) {
        return Optional.ofNullable(this.tagRepository.findByTag(tagName));
    }

    public Optional<List<TagEntity>> getByTagIdList(List<String> tagIds) {
        return Optional.ofNullable(this.tagRepository.findAllByTagIdIn(tagIds));
    }


    public Optional<TagEntity> getByTagId(String tagId) {
        return Optional.ofNullable(this.tagRepository.findByTagId(tagId));
    }
}
