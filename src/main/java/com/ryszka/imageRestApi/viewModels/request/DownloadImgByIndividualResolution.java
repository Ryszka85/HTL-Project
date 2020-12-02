package com.ryszka.imageRestApi.viewModels.request;

import java.io.Serializable;

public class DownloadImgByIndividualResolution implements Serializable {
    private static final long serialVersionUID = -3599237490025114062L;
    private String imageId;
    private float width;
    private float height;

    public DownloadImgByIndividualResolution() {
    }

    public DownloadImgByIndividualResolution(String imageId, int width, int height) {
        this.imageId = imageId;
        this.width = width;
        this.height = height;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}
