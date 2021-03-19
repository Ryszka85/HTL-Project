package com.ryszka.imageRestApi.service.serviceV2.writeService;

import com.ryszka.imageRestApi.repository.GoogleCloudRepository;
import com.ryszka.imageRestApi.service.dto.ImageDTO;
import com.ryszka.imageRestApi.viewModels.ImageDetailsList;

import java.io.IOException;

public class SaveDownloadViewImg extends GoogleUploadTask{
    private ImageDTO imageDTO;
    public SaveDownloadViewImg(GoogleCloudRepository cloudRepository,
                               ImageDTO imageDTO) {
        super(cloudRepository, SaveDownloadViewImg.class);
        this.imageDTO = imageDTO;
    }

    @Override
    public void run() {
        try {
            cloudRepository.storeImage(
                    "downloadView" + "/" + imageDTO.getPath(),
                    imageDTO.getName(),
                    imageDTO.getContentDownloadFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
