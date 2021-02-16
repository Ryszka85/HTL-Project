package com.ryszka.imageRestApi.service.dto;

import com.ryszka.imageRestApi.viewModels.request.AddToLibraryRequest;
import com.ryszka.imageRestApi.viewModels.request.SetTagsToImageRequest;
import com.ryszka.imageRestApi.persistenceEntities.ImageEntity;
import com.ryszka.imageRestApi.persistenceEntities.TagEntity;
import com.ryszka.imageRestApi.viewModels.response.UserDetailsResponseModel;
import com.ryszka.imageRestApi.util.mapper.dbMappers.TaggedImgQueryAttributes;
import com.ryszka.imageRestApi.persistenceEntities.UserEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ImageDTO {
    private String name, path, profileImgPath, username, userId, imageId, email, link, tagId, tag;
    private UserEntity userEntity;
    private List<ImageEntity> imageEntities;
    private UserDetailsResponseModel user;
    private List<TaggedImgQueryAttributes> taggedImgQueries;
    private List<TagDTO> tags;
    private ImageEntity imageEntity;
    private byte[] content, contentGalleryFile, contentDownloadFile;
    private MultipartFile file, galleryFile, downloadFile;
    private String urlReference;
    private boolean isPublic;
    private List<String> tagList;
    private InputStream inputStream, inputStreamGalleryFile, inputStreamDownloadFile;
    List<TagEntity> tagEntities;

    public ImageDTO() {
    }

    public ImageDTO(AddToLibraryRequest request) {
        this(request.getUserId(), request.getFile(), request.getGalleryFile(), request.getDownloadFile());
        this.isPublic = request.getIsPublic().equals("true");
        this.urlReference = request.getUrlReference();
        if (!request.getTags().isEmpty() && request.getTags() != null)
            this.tagList = Arrays.stream(request.getTags()
                    .split("-"))
                    .collect(Collectors.toList());
    }

    public ImageDTO(String userId, MultipartFile file, MultipartFile galleryFile, MultipartFile downloadFile) {
        this(userId, file);
        this.galleryFile = galleryFile;
        this.downloadFile = downloadFile;
        try {
            this.inputStreamDownloadFile = downloadFile.getInputStream();
            this.inputStreamGalleryFile = galleryFile.getInputStream();
            contentGalleryFile = galleryFile.getBytes();
            contentDownloadFile = downloadFile.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ImageDTO(String userId, MultipartFile file) {
        this.userId = userId;
        this.file = file;
        try {
            this.inputStream = file.getInputStream();
            content = file.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.path = userId;
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        int index = originalFilename.lastIndexOf(".");
        String substring = originalFilename.substring(0, index);
        String substring1 = originalFilename.substring(index);
        this.name = substring +
                System.currentTimeMillis() +
                substring1;
    }

    public ImageDTO(UserEntity userEntity, List<ImageEntity> imageEntities) {
        this.userEntity = userEntity;
        this.imageEntities = imageEntities;
    }

    public ImageDTO(String name,
                    String path,
                    String userId,
                    String imageId,
                    String email,
                    String link,
                    String tagId,
                    String tag) {
        this.name = name;
        this.path = path;
        this.userId = userId;
        this.imageId = imageId;
        this.email = email;
        this.link = link;
        this.tagId = tagId;
        this.tag = tag;
    }

    public ImageDTO(String name,
                    String path,
                    String userId,
                    String imageId,
                    String email,
                    byte[] content) {
        this(name, path, userId, content);
        this.imageId = imageId;
        this.email = email;
    }

    public ImageDTO(SetTagsToImageRequest request) {
        this.userId = request.getUserId();
        this.imageId = request.getImageId();
        this.tags = request.getTags();
    }


    public ImageDTO(String name, String path, byte[] content) {
        this.name = name;
        this.path = path;
        this.content = content;
    }

    public ImageDTO(String name, String path, String userId, byte[] content) {
        this(name, path, content);
        this.userId = userId;
    }

    public ImageDTO(String name,
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
        this.content = null;
        this.tags = tags;
    }

    public List<String> getTagList() {
        return tagList;
    }

    public void setTagList(List<String> tagList) {
        this.tagList = tagList;
    }

    public byte[] getContentDownloadFile() {
        return contentDownloadFile;
    }

    public void setContentDownloadFile(byte[] contentDownloadFile) {
        this.contentDownloadFile = contentDownloadFile;
    }

    public MultipartFile getDownloadFile() {
        return downloadFile;
    }

    public void setDownloadFile(MultipartFile downloadFile) {
        this.downloadFile = downloadFile;
    }

    public InputStream getInputStreamDownloadFile() {
        return inputStreamDownloadFile;
    }

    public void setInputStreamDownloadFile(InputStream inputStreamDownloadFile) {
        this.inputStreamDownloadFile = inputStreamDownloadFile;
    }

    public byte[] getContentGalleryFile() {
        return contentGalleryFile;
    }

    public void setContentGalleryFile(byte[] contentGalleryFile) {
        this.contentGalleryFile = contentGalleryFile;
    }

    public InputStream getInputStreamGalleryFile() {
        return inputStreamGalleryFile;
    }

    public void setInputStreamGalleryFile(InputStream inputStreamGalleryFile) {
        this.inputStreamGalleryFile = inputStreamGalleryFile;
    }

    public MultipartFile getGalleryFile() {
        return galleryFile;
    }

    public void setGalleryFile(MultipartFile galleryFile) {
        this.galleryFile = galleryFile;
    }

    public String getUrlReference() {
        return urlReference;
    }

    public void setUrlReference(String urlReference) {
        this.urlReference = urlReference;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public List<TagEntity> getTagEntities() {
        return tagEntities;
    }

    public void setTagEntities(List<TagEntity> tagEntities) {
        this.tagEntities = tagEntities;
    }

    public UserDetailsResponseModel getUser() {
        return user;
    }

    public void setUser(UserDetailsResponseModel user) {
        this.user = user;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
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

    public ImageEntity getImageEntity() {
        return imageEntity;
    }

    public void setImageEntity(ImageEntity imageEntity) {
        this.imageEntity = imageEntity;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public List<TaggedImgQueryAttributes> getTaggedImgQueries() {
        return taggedImgQueries;
    }

    public List<TaggedImgQueryAttributes> getTaggedImages() {
        return taggedImgQueries;
    }

    public void setTaggedImgQueries(List<TaggedImgQueryAttributes> taggedImgQueries) {
        this.taggedImgQueries = taggedImgQueries;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public List<ImageEntity> getImageEntities() {
        return imageEntities;
    }

    public void setImageEntities(List<ImageEntity> imageEntities) {
        this.imageEntities = imageEntities;
    }

    public List<TagDTO> getTags() {
        return tags;
    }

    public void setTags(List<TagDTO> tags) {
        this.tags = tags;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
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

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    @Override
    public int hashCode() {
        return this.getName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ImageDTO) {
            return ((ImageDTO) obj).getName().equals(this.getName());
        } else return false;
    }

    @Override
    public String toString() {
        return "ImageDTO{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", profileImgPath='" + profileImgPath + '\'' +
                ", username='" + username + '\'' +
                ", userId='" + userId + '\'' +
                ", imageId='" + imageId + '\'' +
                ", email='" + email + '\'' +
                ", link='" + link + '\'' +
                ", tagId='" + tagId + '\'' +
                ", tag='" + tag + '\'' +
                ", userEntity=" + userEntity +
                ", imageEntities=" + imageEntities +
                ", taggedImgQueries=" + taggedImgQueries +
                ", tags=" + tags +
                ", imageEntity=" + imageEntity +
                '}';
    }
}
