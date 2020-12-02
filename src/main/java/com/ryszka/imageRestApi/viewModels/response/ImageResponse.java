package com.ryszka.imageRestApi.viewModels.response;

import com.ryszka.imageRestApi.service.dto.ImageDTO;
import com.ryszka.imageRestApi.service.dto.TagDTO;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ImageResponse implements Serializable {
    private static final long serialVersionUID = -2390856141352036471L;
    private String name, path, userId, imageId, email, link, profileImgPath, username;
    private UserDetailsResponseModel user;
    private List<TagDTO> tags;

    public ImageResponse(Map.Entry<ImageDTO, List<TagDTO>> dtoListEntry) {
        this.name = dtoListEntry.getKey().getName();
        this.path = dtoListEntry.getKey().getPath();
        this.userId = dtoListEntry.getKey().getUserId();
        this.imageId = dtoListEntry.getKey().getImageId();
        this.profileImgPath = dtoListEntry.getKey().getProfileImgPath();
        this.username = dtoListEntry.getKey().getUsername();
        this.email = dtoListEntry.getKey().getEmail();
        this.link = dtoListEntry.getKey().getLink();
        this.tags = dtoListEntry.getKey().getTags();
        this.tags = dtoListEntry.getValue();
        this.user = new UserDetailsResponseModel(
                "",
                "",
                this.userId,
                this.email,
                this.username,
                this.profileImgPath);
    }


    public ImageResponse(ImageDTO imageDTO, List<TagDTO> tags) {
        this.name = imageDTO.getName();
        this.path = imageDTO.getPath();
        this.userId = imageDTO.getUserId();
        this.imageId = imageDTO.getImageId();
        this.email = imageDTO.getEmail();
        this.link = imageDTO.getLink();
        this.tags = imageDTO.getTags();
        this.tags = tags;
    }

    public ImageResponse(ImageDTO imageDTO) {
        this.name = imageDTO.getName();
        this.path = imageDTO.getPath();
        this.userId = imageDTO.getUserId();
        this.imageId = imageDTO.getImageId();
        this.email = imageDTO.getEmail();
        this.link = imageDTO.getLink();
        this.tags = imageDTO.getTags();
    }


    public ImageResponse(String name,
                         String path,
                         String userId,
                         String imageId,
                         String email,
                         String link,
                         List<TagDTO> tags) {
        this.name = name;
        this.path = path;
        this.userId = userId;
        this.imageId = imageId;
        this.email = email;
        this.link = link;
        this.tags = tags;
    }

    public UserDetailsResponseModel getUser() {
        return user;
    }

    public void setUser(UserDetailsResponseModel user) {
        this.user = user;
    }

    public String getProfileImgPath() {
        return profileImgPath;
    }

    public void setProfileImgPath(String profileImgPath) {
        this.profileImgPath = profileImgPath;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<TagDTO> getTags() {
        return tags;
    }

    public void setTags(List<TagDTO> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "ImageResponse{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", userId='" + userId + '\'' +
                ", imageId='" + imageId + '\'' +
                ", userName='" + email + '\'' +
                ", link='" + link + '\'' +
                ", tags=" + tags +
                '}';
    }
}
