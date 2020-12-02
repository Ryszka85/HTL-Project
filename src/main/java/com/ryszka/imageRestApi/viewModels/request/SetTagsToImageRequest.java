package com.ryszka.imageRestApi.viewModels.request;

import com.ryszka.imageRestApi.service.dto.TagDTO;

import java.io.Serializable;
import java.util.List;

public class SetTagsToImageRequest implements Serializable {
    private static final long serialVersionUID = 3766584050132301410L;
    private String userId, imageId;
    private List<TagDTO> tags;

    public SetTagsToImageRequest() {
    }

    public SetTagsToImageRequest(String userId, String imageId, List<TagDTO> tags) {
        this.userId = userId;
        this.imageId = imageId;
        this.tags = tags;
        /*this.tagId = tagId;*/
    }

    public List<TagDTO> getTags() {
        return tags;
    }

    public void setTags(List<TagDTO> tags) {
        this.tags = tags;
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

    @Override
    public String toString() {
        return "SetTagsToImageRequest{" +
                "userId='" + userId + '\'' +
                ", imageId='" + imageId + '\'' +
                ", tags=" + tags +
                '}';
    }


    /*public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }*/
}
