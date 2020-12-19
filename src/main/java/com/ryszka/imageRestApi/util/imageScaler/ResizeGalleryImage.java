package com.ryszka.imageRestApi.util.imageScaler;

import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

public class ResizeGalleryImage implements ImageResizer{
    private final Logger logger =
            LoggerFactory.getLogger(ResizeGalleryImage.class);
    private final int width, height;
    private final byte[] content;


    public ResizeGalleryImage(int width, int height, byte[] content) {
        this.width = width;
        this.height = height;
        this.content = content;
    }

    @Override
    public byte[] resize() throws IOException {
        logger.info("Starting to resize gallery-file");
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            BufferedImage read = ImageIO.read(new ByteArrayInputStream(content));
            Map<String, Integer> newDimension = calcDimensions(width, height);
            double newWidth = width * 0.4;
            assert newDimension != null;
            int newW = newDimension.get("X");
            double newHeight = height * 0.4;
            int newH = newDimension.get("Y");
            try {
                Thumbnails.of(read)
                        .size(newW, newH)
                        .keepAspectRatio(true)
                        .outputFormat("jpg")
                        .toOutputStream(baos);
                logger.info("Finished resizing image " + newWidth + "x" + height);
                return baos.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } 
        return new byte[0];
    }

    private Map<String, Integer> calcDimensions(int width, int height) {
        float adder = 0.1f;
        for (float f = 0.1f; f <= 1; f += adder) {
            float widthTimesFac = height > width ? width * f : height * f;
            System.out.println(f / 10);
            if (widthTimesFac >= 600) {
                if (widthTimesFac <= 700) {
                    return Map.of("X", (int) (width * f), "Y", (int) (height * f));
                } else {
                    f /= 10;
                    adder /= 10;
                }
            }
        }
        return null;
    }
}
