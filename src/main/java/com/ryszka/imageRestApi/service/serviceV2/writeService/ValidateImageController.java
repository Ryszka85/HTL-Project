package com.ryszka.imageRestApi.service.serviceV2.writeService;

import com.google.api.gax.rpc.InvalidArgumentException;
import com.ryszka.imageRestApi.config.AuthSecurityConfig;
import com.ryszka.imageRestApi.errorHandling.ErrorMessages;
import com.ryszka.imageRestApi.errorHandling.ImageToSmallException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

@RestController
@RequestMapping(value = "library")
public class ValidateImageController {
    private final Logger logger = LoggerFactory.getLogger(ValidateImageController.class);
    @PostMapping(value = "validate-image")
    public ValidateImageResponse validateImage(@RequestBody MultipartFile file) throws IOException {
        BufferedImage image = ImageIO.read(file.getInputStream());
        logger.info("Starting to validate image " +
                file.getOriginalFilename() + ": " +
                image.getWidth() + " x " + image.getHeight() +
                ", total : " + image.getHeight() * image.getWidth());
        final int resReq = image.getWidth() * image.getHeight();
        final int minRes = 1920 * 1080;
        if (image.getWidth() >= 500 && image.getHeight() >= 460 && resReq < minRes) {
            return new ValidateImageResponse(ErrorMessages.ILLEGAL_IMAGE_SIZE.getMessage(),
                    400);
        }
        logger.info("Image was valid");
        return new ValidateImageResponse("Image was valid",
                200, image.getWidth(), image.getHeight());
    }
}

class ValidateImageResponse {
    private String message;
    private int status, width, height;

    public ValidateImageResponse(String message, int status, int width, int height) {
        this.message = message;
        this.status = status;
        this.width = width;
        this.height = height;
    }

    public ValidateImageResponse(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
