package com.ryszka.imageRestApi.repository;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.ryszka.imageRestApi.config.FireBaseStorageConfig;
import com.ryszka.imageRestApi.security.AppConfigProperties;
import com.ryszka.imageRestApi.service.serviceV2.writeService.DatabaseAndFTPStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Repository
public class GoogleCloudRepository {
    private final Logger logger =
            LoggerFactory.getLogger(GoogleCloudRepository.class);
    private final FireBaseStorageConfig storageConfig;

    public GoogleCloudRepository(FireBaseStorageConfig storageConfig) {
        this.storageConfig = storageConfig;
    }

    public void storeImage(String path, String name, byte[] content) throws IOException {
        storageConfig.initFireBaseStorage(storage -> {
            logger.info("Starting storing file " + name + " to google cloud...");
            BlobId blobId = BlobId.of(AppConfigProperties.BUCKET_NAME,
                    path + "/" + name);
            BlobInfo info = BlobInfo.newBuilder(blobId).setContentType("image/jpeg").build();
            storage.create(info, content);
            logger.info("Finished storing file " + path + "/" + name + " to google cloud.");
        });
    }
}
