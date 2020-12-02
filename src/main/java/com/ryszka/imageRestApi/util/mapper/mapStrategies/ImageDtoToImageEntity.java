package com.ryszka.imageRestApi.util.mapper.mapStrategies;

import com.ryszka.imageRestApi.persistenceEntities.ImageEntity;
import com.ryszka.imageRestApi.service.dto.ImageDTO;
import com.ryszka.imageRestApi.util.IDHashGenerator;

public class ImageDtoToImageEntity implements MapStrategy<ImageDTO, ImageEntity> {

    @Override
    public ImageEntity map(ImageDTO source) {
        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setPath(source.getPath());
        imageEntity.setName(source.getName());
        imageEntity.setImageId(IDHashGenerator.generate30CharHash());
        imageEntity.setUserEntity(source.getUserEntity());
        imageEntity.setUrlReference(source.getUrlReference());
        imageEntity.setIsPublic(source.isPublic());
        return imageEntity;
    }
}
