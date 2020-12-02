package com.ryszka.imageRestApi.persistenceEntities;

import java.io.InputStream;

public class FtpPersistenceEntity {
    private String path;
    private InputStream content;

    public FtpPersistenceEntity(String path, InputStream content) {
        this.path = path;
        this.content = content;
    }

    public String getPath() {
        return path;
    }

    public InputStream getContent() {
        return content;
    }
}
