package com.ryszka.imageRestApi.util.imageScaler;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ResizeDownByHeightForDownload implements ImageResizer {
    private final Logger logger =
            LoggerFactory.getLogger(ResizeDownByWidthForDownload.class);
    private byte[] content;
    private int height;

    public ResizeDownByHeightForDownload(byte[] content, int height) {
        this.content = content;
        this.height = height;
    }

    @Override
    public byte[] resize() throws IOException {
        try (ByteArrayOutputStream bis = new ByteArrayOutputStream()) {
            BufferedImage read = ImageIO.read(new ByteArrayInputStream(content));
            double ratio = read.getWidth() / (double) read.getHeight();
            double newW = height * ratio;
            int newWidth = (int) newW;
            if (height != read.getHeight()) {
                try {
                    Thumbnails.of(read)
                            .size(newWidth, height)
                            .keepAspectRatio(true)
                            .outputFormat("jpg")
                            .toOutputStream(bis);
                    logger.info("Finished scaling image " + newWidth + "x" + height);
                    return bis.toByteArray();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
