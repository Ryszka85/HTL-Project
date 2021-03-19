package com.ryszka.imageRestApi.service.serviceV2.readService;
import com.ryszka.imageRestApi.dao.TagDAO;
import com.ryszka.imageRestApi.errorHandling.ErrorMessages;
import com.ryszka.imageRestApi.service.dto.TagDTO;
import com.ryszka.imageRestApi.repository.TagRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@Service
public class ReadTagsService {
    private final TagRepository tagRepository;
    private final TagDAO tagDAO;
    private final Logger logger = LoggerFactory.getLogger(ReadTagsService.class);

    public ReadTagsService(TagRepository tagRepository, TagDAO tagDAO) {
        this.tagRepository = tagRepository;
        this.tagDAO = tagDAO;
    }

    public List<TagDTO> getTagsLikeSearchTerm(String searchTerm) {
        logger.info("Starting [ getAllTagsByName ] query ... {} ", searchTerm);
        return tagDAO.getTagsLikeSearchTerm(searchTerm)
                .orElseThrow(() -> new IllegalArgumentException(
                        ErrorMessages.INVALID_ARGUMENTS.getMessage()))
                .stream()
                .map(tagEntity -> new TagDTO(tagEntity.getTagId(), tagEntity.getTag()))
                .collect(Collectors.toList());
    }

    public List<TagDTO> getAllTags() {
        logger.info("Starting [ getAllTags() ] query ...");
        return StreamSupport.stream(tagRepository.findAll().spliterator(), false)
                .filter(tagEntity -> tagEntity.getImageEntities().size() > 0)
                .map(tagEntity -> new TagDTO(tagEntity.getTagId(), tagEntity.getTag()))
                .collect(Collectors.toList());
    }
}
