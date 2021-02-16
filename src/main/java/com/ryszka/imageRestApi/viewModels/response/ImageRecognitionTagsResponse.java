package com.ryszka.imageRestApi.viewModels.response;

import java.util.List;

public class ImageRecognitionTagsResponse {
    private List<String> imageTags;

    public ImageRecognitionTagsResponse() {
    }

    public ImageRecognitionTagsResponse(List<String> imageTags) {
        this.imageTags = imageTags;
    }

    public List<String> getImageTags() {
        return imageTags;
    }

    public void setImageTags(List<String> imageTags) {
        this.imageTags = imageTags;
    }
}
