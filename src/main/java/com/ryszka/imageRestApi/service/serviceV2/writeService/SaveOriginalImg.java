package com.ryszka.imageRestApi.service.serviceV2.writeService;

import com.ryszka.imageRestApi.persistenceEntities.ImageDetailsEntity;
import com.ryszka.imageRestApi.persistenceEntities.ImageEntity;
import com.ryszka.imageRestApi.repository.GoogleCloudRepository;
import com.ryszka.imageRestApi.service.dto.ImageDTO;
import com.ryszka.imageRestApi.util.mapper.ObjectMapper;
import com.ryszka.imageRestApi.util.mapper.mapStrategies.ImageDtoToImageEntity;
import com.ryszka.imageRestApi.viewModels.ImageDetailsList;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class SaveOriginalImg extends GoogleUploadTask {
    private ImageDetailsList imageDetailsList;
    private ImageDTO imageDTO;
    private BufferedImage bi;
    private ImageEntity imageEntity;

    public SaveOriginalImg(GoogleCloudRepository cloudRepository,
                           ImageDetailsList imageDetailsList,
                           ImageDTO imageDTO,
                           BufferedImage bi,
                           ImageEntity imageEntity) {
        super(cloudRepository, SaveOriginalImg.class);
        this.imageDetailsList = imageDetailsList;
        this.imageDTO = imageDTO;
        this.bi = bi;
        this.imageEntity = imageEntity;
    }

    @Override
    public void run() {
        try {
            this.imageDetailsList.addDetail(new ImageDetailsEntity(
                    bi.getWidth(),
                    bi.getHeight(),
                    imageDTO.getFile().getContentType(),
                    imageDTO.getFile().getSize(),
                    imageEntity));

            String path = "original/" + imageDTO.getPath();
            cloudRepository.storeImage(path,
                    imageDTO.getName(),
                    imageDTO.getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
