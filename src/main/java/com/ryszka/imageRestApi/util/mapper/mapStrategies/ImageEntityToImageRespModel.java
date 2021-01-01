package com.ryszka.imageRestApi.util.mapper.mapStrategies;

import com.ryszka.imageRestApi.util.mapper.ObjectMapper;
import com.ryszka.imageRestApi.persistenceEntities.ImageEntity;
import com.ryszka.imageRestApi.viewModels.response.ImageDetailViewModel;
import com.ryszka.imageRestApi.viewModels.response.UserDetailsResponseModel;
import com.ryszka.imageRestApi.persistenceEntities.UserEntity;
import com.ryszka.imageRestApi.viewModels.response.UserImageViewModel;
import com.ryszka.imageRestApi.service.dto.TagDTO;
import com.ryszka.imageRestApi.util.PathGenerator;

import java.util.List;
import java.util.stream.Collectors;

public class ImageEntityToImageRespModel implements MapStrategy<ImageEntity, UserImageViewModel> {

    @Override
    public UserImageViewModel map(ImageEntity source) {
        UserEntity userEntity = source.getUserEntity();
        UserDetailsResponseModel owner = ObjectMapper.mapByStrategy(userEntity, new UserEntityToUserDetailsResponseModel());
        List<TagDTO> tags = ObjectMapper.mapToList(source.getTags(), new TagEntitiesToTagDtoList());

        String fileAccessLink =
                PathGenerator.generateGalleryFileAccessLink(userEntity.getUserId() + "/" + source.getName());

        String originalImgPath = PathGenerator.generateOriginalImgFileAccessLink(
                userEntity.getUserId() + "/" + source.getName());

        List<ImageDetailViewModel> details = source.getImageDetailsEntities()
                .stream()
                .map(detail -> new ImageDetailViewModel(detail.getType(), detail.getWidth(), detail.getHeight(), detail.getSize()))
                .filter(detail -> detail.width != 960)
                .collect(Collectors.toList());

        return new UserImageViewModel(
                source.getName(),
                fileAccessLink,
                source.getImageId(),
                owner,
                tags,
                source.getUrlReference(),
                source.getIsPublic(),
                source.getUserLikesList().size(),
                source.getDownloaded(),
                details,
                source.getUploadDate(),
                originalImgPath,
                fileAccessLink

        );
    }
}
