package com.ryszka.imageRestApi.service.dto;

import java.io.Serializable;

public class TagDTO implements Serializable {
    private static final long serialVersionUID = -9214038900519085147L;
    private String tagId, tag;

    public TagDTO() {
    }



    public TagDTO(String tagId, String tag) {
        this.tagId = tagId;
        this.tag = tag;
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

    @Override
    public String toString() {
        return "TagDTO{" +
                "tagId='" + tagId + '\'' +
                ", tag='" + tag + '\'' +
                '}';
    }
}
