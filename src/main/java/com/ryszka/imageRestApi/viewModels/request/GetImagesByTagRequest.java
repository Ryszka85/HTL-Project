package com.ryszka.imageRestApi.viewModels.request;

import java.io.Serializable;

public class GetImagesByTagRequest implements Serializable {
    private String tagName;
    private int filterByDays;
    private boolean mostDownloaded, mostLiked;

    public GetImagesByTagRequest() {
    }

    public GetImagesByTagRequest(String tagName, int filterByDays, boolean mostDownloaded, boolean mostLiked) {
        this.tagName = tagName;
        this.filterByDays = filterByDays;
        this.mostDownloaded = mostDownloaded;
        this.mostLiked = mostLiked;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public int getFilterByDays() {
        return filterByDays;
    }

    public void setFilterByDays(int filterByDays) {
        this.filterByDays = filterByDays;
    }

    public boolean isMostDownloaded() {
        return mostDownloaded;
    }

    public void setMostDownloaded(boolean mostDownloaded) {
        this.mostDownloaded = mostDownloaded;
    }

    public boolean isMostLiked() {
        return mostLiked;
    }

    public void setMostLiked(boolean mostLiked) {
        this.mostLiked = mostLiked;
    }
}
