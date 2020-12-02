package com.ryszka.imageRestApi.util.mapper.mapStrategies;

import com.ryszka.imageRestApi.persistenceEntities.TagEntity;
import com.ryszka.imageRestApi.service.dto.TagDTO;

import java.util.List;
import java.util.stream.Collectors;

public class TagEntitiesToTagDtoList implements MapStrategy<List<TagEntity>, List<TagDTO>> {
    @Override
    public List<TagDTO> map(List<TagEntity> source) {
        return source.stream()
                .map(tagEntity -> new TagDTO(tagEntity.getTagId(), tagEntity.getTag()))
                .collect(Collectors.toList());
    }
}
