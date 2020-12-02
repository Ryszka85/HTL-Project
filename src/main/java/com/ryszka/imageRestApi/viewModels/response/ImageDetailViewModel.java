package com.ryszka.imageRestApi.viewModels.response;

public class ImageDetailViewModel {
    public String downloadLink, contentType;
    public int width, height;
    private long size;



    public ImageDetailViewModel() {
    }

    public ImageDetailViewModel(String contentType, int width, int height, long size) {
        this.contentType = contentType;
        this.width = width;
        this.height = height;
        this.size = size;
    }

    public ImageDetailViewModel(int width,
                                int height,
                                long size,
                                String downloadLink,
                                String contentType) {
        this(downloadLink);
        this.contentType = contentType;
        this.width = width;
        this.height = height;
        this.size = size;
    }

    public ImageDetailViewModel(String downloadLink) {
        this.downloadLink = downloadLink;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }
}
