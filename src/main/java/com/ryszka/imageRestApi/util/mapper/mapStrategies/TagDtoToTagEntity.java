package com.ryszka.imageRestApi.util.mapper.mapStrategies;

import com.ryszka.imageRestApi.persistenceEntities.TagEntity;
import com.ryszka.imageRestApi.service.dto.TagDTO;

public class TagDtoToTagEntity implements MapStrategy<TagDTO, TagEntity> {
    @Override
    public TagEntity map(TagDTO source) {
        TagEntity tagEntity = new TagEntity();
        tagEntity.setTagId(source.getTagId());
        tagEntity.setTag(source.getTag());
        return tagEntity;
    }
}
