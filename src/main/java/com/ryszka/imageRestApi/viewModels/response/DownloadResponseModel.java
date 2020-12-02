package com.ryszka.imageRestApi.viewModels.response;

import java.util.ArrayList;
import java.util.List;

public class DownloadResponseModel {
    private List<ImageDetailViewModel> files;
    private UserImageViewModel image;

    public DownloadResponseModel() {
        this.files = new ArrayList<>();
    }

    public DownloadResponseModel(List<ImageDetailViewModel> files, UserImageViewModel image) {
        this(files);
        this.image = image;
    }

    public DownloadResponseModel(List<ImageDetailViewModel> files) {
        this.files = files;
    }

    public UserImageViewModel getImage() {
        return image;
    }

    public void setImage(UserImageViewModel image) {
        this.image = image;
    }

    public List<ImageDetailViewModel> getFiles() {
        return files;
    }

    public void setFiles(List<ImageDetailViewModel> files) {
        this.files = files;
    }
}
