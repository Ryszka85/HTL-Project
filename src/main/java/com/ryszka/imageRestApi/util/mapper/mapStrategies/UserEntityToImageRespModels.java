package com.ryszka.imageRestApi.util.mapper.mapStrategies;

import com.ryszka.imageRestApi.util.mapper.ObjectMapper;
import com.ryszka.imageRestApi.viewModels.response.ImageDetailViewModel;
import com.ryszka.imageRestApi.viewModels.response.UserImageViewModel;
import com.ryszka.imageRestApi.viewModels.response.UserDetailsResponseModel;
import com.ryszka.imageRestApi.persistenceEntities.UserEntity;
import com.ryszka.imageRestApi.service.dto.TagDTO;
import com.ryszka.imageRestApi.util.PathGenerator;

import java.util.List;
import java.util.stream.Collectors;

public class UserEntityToImageRespModels implements MapStrategy<UserEntity, List<UserImageViewModel>> {

    @Override
    public List<UserImageViewModel> map(UserEntity source) {
        return source
                .getImageEntities()
                .stream()
                .map(imageEntity -> {
                    String fileAccessLink = PathGenerator.generateGalleryFileAccessLink(
                            imageEntity.getPath() + "/" + imageEntity.getName());

                    String originalImgPath = PathGenerator.generateOriginalImgFileAccessLink(
                            imageEntity.getPath() + "/" + imageEntity.getName());

                    UserDetailsResponseModel owner = ObjectMapper.mapByStrategy(source,
                            new UserEntityToUserDetailsResponseModel());

                    List<TagDTO> tags = ObjectMapper.mapToList(imageEntity.getTags(),
                            new TagEntitiesToTagDtoList());


                    List<ImageDetailViewModel> details = imageEntity.getImageDetailsEntities()
                            .stream()
                            .map(detail -> new ImageDetailViewModel(detail.getType(), detail.getWidth(), detail.getHeight(), detail.getSize()))
                            .filter(detail -> detail.width != 960)
                            .collect(Collectors.toList());

                    return new UserImageViewModel(imageEntity.getName(),
                            fileAccessLink,
                            imageEntity.getImageId(),
                            owner,
                            tags,
                            imageEntity.getUrlReference(),
                            imageEntity.getIsPublic(),
                            imageEntity.getUserLikesList().size(),
                            imageEntity.getDownloaded(),
                            details,
                            imageEntity.getUploadDate(),
                            originalImgPath);
                })
                .collect(Collectors.toList());
    }
}
