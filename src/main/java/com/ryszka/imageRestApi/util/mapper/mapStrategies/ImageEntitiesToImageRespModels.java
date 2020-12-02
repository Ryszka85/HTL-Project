package com.ryszka.imageRestApi.util.mapper.mapStrategies;

import com.ryszka.imageRestApi.util.mapper.ObjectMapper;
import com.ryszka.imageRestApi.viewModels.response.UserImageViewModel;
import com.ryszka.imageRestApi.persistenceEntities.ImageEntity;

import java.util.List;
import java.util.stream.Collectors;

public class ImageEntitiesToImageRespModels implements MapStrategy<List<ImageEntity>, List<UserImageViewModel>> {

    @Override
    public List<UserImageViewModel> map(List<ImageEntity> source) {
        return source.stream()
                .map(imageEntity -> ObjectMapper.mapByStrategy(
                        imageEntity, new ImageEntityToImageRespModel()))
                .collect(Collectors.toList());
    }
}
