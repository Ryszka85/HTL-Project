package com.ryszka.imageRestApi.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.ryszka.imageRestApi.security.AppConfigProperties;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

@Service
public class FireBaseStorageConfig {
    public void initFireBaseStorage(Consumer<Storage> func) throws IOException {
        FileInputStream serviceAccount =
                new FileInputStream(Objects.requireNonNull(AppConfigProperties.FIRE_BASE_CONFIG_FILE_PATH));
        func.accept(StorageOptions
                .newBuilder()
                .setProjectId(AppConfigProperties.FIREBASE_PROJECT_ID)
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build()
                .getService());
    }

    public Storage initAndGetStorage() throws IOException {
        FileInputStream serviceAccount =
                new FileInputStream(Objects.requireNonNull(AppConfigProperties.FIRE_BASE_CONFIG_FILE_PATH));
        return StorageOptions
                .newBuilder()
                .setProjectId(AppConfigProperties.FIREBASE_PROJECT_ID)
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build()
                .getService();
    }
}
