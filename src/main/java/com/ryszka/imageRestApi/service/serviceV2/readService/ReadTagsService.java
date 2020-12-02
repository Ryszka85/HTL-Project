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
}
