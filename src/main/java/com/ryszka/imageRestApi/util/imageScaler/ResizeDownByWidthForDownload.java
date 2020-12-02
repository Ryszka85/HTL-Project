package com.ryszka.imageRestApi.util.imageScaler;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class ResizeDownByWidthForDownload implements ImageResizer {
    private final Logger logger =
            LoggerFactory.getLogger(ResizeDownByWidthForDownload.class);
    private byte[] content;
    private int width;

    public ResizeDownByWidthForDownload(byte[] content, int width) {
        this.content = content;
        this.width = width;
    }

    @Override
    public byte[] resize() throws IOException {
        try (ByteArrayOutputStream bis = new ByteArrayOutputStream()) {
            BufferedImage read = ImageIO.read(new ByteArrayInputStream(content));
            double ratio = read.getWidth() / (double) read.getHeight();
            double newH = width / ratio;
            int newHeight = (int) newH;
            if (width != read.getWidth()) {
                try {
                    Thumbnails.of(read)
                            .size(width, newHeight)
                            .keepAspectRatio(true)
                            .outputFormat("jpg")
                            .toOutputStream(bis);
                    logger.info("Finished scaling image " + width + "x" + newH);
                    return bis.toByteArray();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
