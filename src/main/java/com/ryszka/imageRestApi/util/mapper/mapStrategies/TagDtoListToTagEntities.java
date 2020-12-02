package com.ryszka.imageRestApi.util.mapper.mapStrategies;

import com.ryszka.imageRestApi.persistenceEntities.TagEntity;
import com.ryszka.imageRestApi.service.dto.TagDTO;
import com.ryszka.imageRestApi.util.mapper.ObjectMapper;

import java.util.List;
import java.util.stream.Collectors;

public class TagDtoListToTagEntities
        implements MapStrategy<List<TagDTO>, List<TagEntity>> {
    @Override
    public List<TagEntity> map(List<TagDTO> source) {
        return source
                .stream()
                .map(tagDTO -> ObjectMapper.mapByStrategy(
                        tagDTO, new TagDtoToTagEntity()))
                .collect(Collectors.toList());
    }
}
