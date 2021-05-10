package com.ryszka.imageRestApi.util.imageScaler;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
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

            Metadata metadata = ImageMetadataReader.readMetadata(new ByteArrayInputStream(content));

            try {


            if (rotateImage(metadata)) {
                logger.info("Need to rotate image..");
                Thumbnails.of(read)
                        .size(newW, newH)
                        .keepAspectRatio(true)
                        .rotate(90)
                        .outputFormat("jpg")
                        .toOutputStream(baos);
                System.out.println("Gallery image was resized " + width + " x " + height +  "\nto : " + newW + " x " + newH);
                return baos.toByteArray();
            } else {
                System.out.println("Gallery image was resized " + width + " x " + height +  "\nto : " + newW + " x " + newH);

                Thumbnails.of(read)
                        .size(newW, newH)
                        .keepAspectRatio(true)
                        .outputFormat("jpg")
                        .toOutputStream(baos);
                logger.info("Finished resizing image " + newWidth + "x" + height);
                return baos.toByteArray();
            }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (ImageProcessingException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    private boolean rotateImage(Metadata metadata) {
        for (Directory directory : metadata.getDirectories()) {
            if (directory.getName().equals("Exif IFD0")) {
                return directory.getTags()
                        .stream()
                        .filter(tag -> tag.getTagName().equals("Orientation"))
                        .map(tag -> tag.getDescription().split(","))
                        .filter(strings -> strings.length >= 2)
                        .filter(strings -> !strings[0].equals("Top") && !strings[1].equals("left side (Horizontal / normal)"))
                        .count() > 0;
            }
        }
        return false;
    }

    /*TODO: 13.01.2021 Implement simpler function -> forLoop is not needed ->
     *  it is enough to calculate 600 / width or height */


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
