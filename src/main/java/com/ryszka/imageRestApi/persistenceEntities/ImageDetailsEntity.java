package com.ryszka.imageRestApi.persistenceEntities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity(name = "image_details")
public class ImageDetailsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int width, height;
    private String type;
    private long size;
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    @JsonBackReference
    private ImageEntity imageEntity;

    public ImageDetailsEntity() {
    }

    public ImageDetailsEntity(int width, int height, String type, long size, ImageEntity imageEntity) {
        this.width = width;
        this.height = height;
        this.type = type;
        this.size = size;
        this.imageEntity = imageEntity;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public ImageEntity getImageEntity() {
        return imageEntity;
    }

    public void setImageEntity(ImageEntity imageEntity) {
        this.imageEntity = imageEntity;
    }
}
