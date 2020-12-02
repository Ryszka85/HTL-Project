package com.ryszka.imageRestApi.util.imageScaler;

import com.ryszka.imageRestApi.viewModels.request.CroppedImageValuesRequest;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ResizeDownCroppedImage implements ImageResizer {
    private final Logger logger =
            LoggerFactory.getLogger(ResizeDownCroppedImage.class);
    private final byte[] content;
    private final CroppedImageValuesRequest croppedImgValues;

    public ResizeDownCroppedImage(byte[] content, CroppedImageValuesRequest croppedImgValues) {
        this.content = content;
        this.croppedImgValues = croppedImgValues;
    }


    @Override
    public byte[] resize() throws IOException {
        try (ByteArrayOutputStream bis = new ByteArrayOutputStream()) {
            BufferedImage originalImg = ImageIO.read(new ByteArrayInputStream(content));
            logger.info("Starting to crop image : " +
                    originalImg.getWidth() + " x " + originalImg.getHeight() + " at offset X : " +
                    croppedImgValues.getOffsetX() + ", offset Y : " + croppedImgValues.getOffsetY() +
                    " to size " + croppedImgValues.getSubImageWidth() + " x " + croppedImgValues.getSubImageHeight());

            BufferedImage croppedImage = originalImg.getSubimage(croppedImgValues.getOffsetX(), croppedImgValues.getOffsetY(),
                    croppedImgValues.getSubImageWidth(), croppedImgValues.getSubImageHeight());
            logger.info("Finished cropping image.");
            logger.info("starting to resize image to " +
                    croppedImgValues.getSelectedWidth() + " x " + croppedImgValues.getSelectedHeight());
            try {
                Thumbnails.of(croppedImage)
                        .size(croppedImgValues.getSelectedWidth(), croppedImgValues.getSelectedHeight())
                        .keepAspectRatio(true)
                        .outputFormat("jpg")
                        .toOutputStream(bis);
                logger.info("Finished resizing image with dimensions..");
                return bis.toByteArray();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
        return null;
    }
}
