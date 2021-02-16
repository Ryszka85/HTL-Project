package com.ryszka.imageRestApi.service.serviceV2.writeService;

import com.ryszka.imageRestApi.repository.GoogleCloudRepository;
import com.ryszka.imageRestApi.service.dto.ImageDTO;
import com.ryszka.imageRestApi.util.imageScaler.ImageResizer;
import com.ryszka.imageRestApi.util.imageScaler.ResizeGalleryImage;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class SaveGalleryImg extends GoogleUploadTask{
    private ImageResizer resizer;
    private ImageDTO imageDTO;
    private BufferedImage bi;

    public SaveGalleryImg(GoogleCloudRepository cloudRepository,
                          ImageDTO imageDTO,
                          BufferedImage bi) {
        super(cloudRepository, SaveGalleryImg.class);
        byte[] content = imageDTO.getContent();
        this.resizer = new ResizeGalleryImage(bi.getWidth(), bi.getHeight(), content);
        this.imageDTO = imageDTO;
        this.bi = bi;
    }

    @Override
    public void run() {
        byte[] content;
        try {
            content = this.resizer.resize();
            String path = "gallery" + "/" + imageDTO.getPath();
            this.cloudRepository.storeImage(
                    path,
                    imageDTO.getName(),
                    content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
