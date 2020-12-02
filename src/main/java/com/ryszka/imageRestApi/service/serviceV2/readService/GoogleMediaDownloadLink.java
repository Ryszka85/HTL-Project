package com.ryszka.imageRestApi.service.serviceV2.readService;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.ryszka.imageRestApi.config.FireBaseStorageConfig;
import com.ryszka.imageRestApi.controller.writeController.ImageDownloadController;
import com.ryszka.imageRestApi.security.AppConfigProperties;
import com.ryszka.imageRestApi.viewModels.response.ImageDetailViewModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;

public class GoogleMediaDownloadLink {
    private final String path;
    private final FireBaseStorageConfig fireBaseStorageConfig;
    private final Logger logger =
            LoggerFactory.getLogger(GoogleMediaDownloadLink.class);

    public GoogleMediaDownloadLink(String path, FireBaseStorageConfig fireBaseStorageConfig) {
        this.path = path;
        this.fireBaseStorageConfig = fireBaseStorageConfig;
    }

    public ImageDetailViewModel generateLink() throws IOException {
        Storage storage = fireBaseStorageConfig.initAndGetStorage();
        /*List<ImageDetailViewModel> response = new ArrayList<>();*/
        ImageDetailViewModel response = null;
        BlobId blobId = BlobId.of(AppConfigProperties.BUCKET_NAME, path);
        if (Optional.ofNullable(storage.get(blobId))
                .isPresent()) {
            Blob blob = storage.get(blobId);
            byte[] content = blob.getContent();
            BufferedImage bufferedImg = ImageIO.read(new ByteArrayInputStream(content));
            logger.info("Preparing media link for " + path);
            String mediaLink = blob.getMediaLink();
            System.out.println(mediaLink);
            return new ImageDetailViewModel(
                    bufferedImg.getWidth(),
                    bufferedImg.getHeight(),
                    content.length,
                    mediaLink,
                    blob.getContentType());
        }
        return null;
    }
}
