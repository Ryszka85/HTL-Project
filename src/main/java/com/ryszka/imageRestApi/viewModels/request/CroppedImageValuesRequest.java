package com.ryszka.imageRestApi.viewModels.request;

public class CroppedImageValuesRequest {
    private String imageId;
    private int offsetX,offsetY,
            subImageWidth, subImageHeight,
            selectedWidth, selectedHeight;

    public CroppedImageValuesRequest() {
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    public int getSubImageWidth() {
        return subImageWidth;
    }

    public void setSubImageWidth(int subImageWidth) {
        this.subImageWidth = subImageWidth;
    }

    public int getSubImageHeight() {
        return subImageHeight;
    }

    public void setSubImageHeight(int subImageHeight) {
        this.subImageHeight = subImageHeight;
    }

    public int getSelectedWidth() {
        return selectedWidth;
    }

    public void setSelectedWidth(int selectedWidth) {
        this.selectedWidth = selectedWidth;
    }

    public int getSelectedHeight() {
        return selectedHeight;
    }

    public void setSelectedHeight(int selectedHeight) {
        this.selectedHeight = selectedHeight;
    }
}
