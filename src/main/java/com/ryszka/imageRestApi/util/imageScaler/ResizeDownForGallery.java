package com.ryszka.imageRestApi.util.imageScaler;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ResizeDownForGallery implements ImageResizer {
    private final Logger logger =
            LoggerFactory.getLogger(ResizeDownForGallery.class);
    private byte[] content;

    public ResizeDownForGallery(byte[] content) {
        this.content = content;
    }

    @Override
    public byte[] resize() throws IOException {
        try (ByteArrayOutputStream bis = new ByteArrayOutputStream()) {
            BufferedImage read = ImageIO.read(new ByteArrayInputStream(content));
                try {
                    Thumbnails.of(read)
                            .outputQuality(0.3)
                            .scale(1)
                            .outputFormat("jpg")
                            .toOutputStream(bis);
                    logger.info("Finished scaling image for gallery");
                    return bis.toByteArray();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
        }
        return null;
    }
}
