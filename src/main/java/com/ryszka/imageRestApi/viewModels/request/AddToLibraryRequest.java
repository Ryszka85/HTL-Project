package com.ryszka.imageRestApi.viewModels.request;

import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

public class AddToLibraryRequest implements Serializable {
    private static final long serialVersionUID = 2303659391909273522L;
    private MultipartFile file, galleryFile, downloadFile;
    private String userId, urlReference, isPublic;

    public AddToLibraryRequest() {
    }

    public MultipartFile getDownloadFile() {
        return downloadFile;
    }

    public void setDownloadFile(MultipartFile downloadFile) {
        this.downloadFile = downloadFile;
    }

    public MultipartFile getGalleryFile() {
        return galleryFile;
    }

    public void setGalleryFile(MultipartFile galleryFile) {
        this.galleryFile = galleryFile;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUrlReference() {
        return urlReference;
    }

    public void setUrlReference(String urlReference) {
        this.urlReference = urlReference;
    }

    public String getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(String isPublic) {
        this.isPublic = isPublic;
    }
}
