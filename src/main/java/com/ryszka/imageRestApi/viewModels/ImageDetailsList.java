package com.ryszka.imageRestApi.viewModels;

import com.ryszka.imageRestApi.persistenceEntities.ImageDetailsEntity;

import java.util.ArrayList;
import java.util.List;

public class ImageDetailsList {
    private List<ImageDetailsEntity> entityList;
    private Object lock;

    public ImageDetailsList() {
        this.entityList = new ArrayList<>();
        this.lock = new Object();
    }

    public void addDetail(ImageDetailsEntity entity) {
        synchronized (lock) {
            this.entityList.add(entity);
        }
    }

    public List<ImageDetailsEntity> getDetailList() {
        synchronized (lock) {
            return this.entityList;
        }
    }
}
