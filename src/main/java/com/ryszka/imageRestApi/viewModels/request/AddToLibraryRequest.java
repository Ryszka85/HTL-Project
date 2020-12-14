package com.ryszka.imageRestApi.viewModels.request;

import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

public class AddToLibraryRequest implements Serializable {
    private static final long serialVersionUID = 2303659391909273522L;
    private MultipartFile file, galleryFile, downloadFile;
    private String userId, urlReference, isPublic;
    private int offsetXGallery, offsetYGallery, offsetXDownload, offsetYDownload;

    public AddToLibraryRequest() {
    }

    public int getOffsetXGallery() {
        return offsetXGallery;
    }

    public void setOffsetXGallery(int offsetXGallery) {
        this.offsetXGallery = offsetXGallery;
    }

    public int getOffsetYGallery() {
        return offsetYGallery;
    }

    public void setOffsetYGallery(int offsetYGallery) {
        this.offsetYGallery = offsetYGallery;
    }

    public int getOffsetXDownload() {
        return offsetXDownload;
    }

    public void setOffsetXDownload(int offsetXDownload) {
        this.offsetXDownload = offsetXDownload;
    }

    public int getOffsetYDownload() {
        return offsetYDownload;
    }

    public void setOffsetYDownload(int offsetYDownload) {
        this.offsetYDownload = offsetYDownload;
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
