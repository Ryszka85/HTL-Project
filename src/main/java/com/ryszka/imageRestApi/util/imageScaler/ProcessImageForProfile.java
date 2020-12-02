package com.ryszka.imageRestApi.util.imageScaler;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ProcessImageForProfile implements ImageResizer{
    private final Logger logger =
            LoggerFactory.getLogger(ProcessImageForProfile.class);
    private byte[] content;

    public ProcessImageForProfile(byte[] content) {
        this.content = content;
    }

    @Override
    public byte[] resize() throws IOException {
        try (ByteArrayOutputStream bis = new ByteArrayOutputStream()) {
            BufferedImage originalImg = ImageIO.read(new ByteArrayInputStream(content));
            logger.info("Starting to resize image..." );
            try {
                Thumbnails.of(originalImg)
                        .forceSize(200, 200)
                        .outputFormat("jpg")
                        .toOutputStream(bis);
                logger.info("Finished resizing image with.");
                return bis.toByteArray();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
        return new byte[0];
    }
}
