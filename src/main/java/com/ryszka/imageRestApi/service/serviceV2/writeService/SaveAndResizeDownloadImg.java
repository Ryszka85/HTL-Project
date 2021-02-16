package com.ryszka.imageRestApi.service.serviceV2.writeService;

import com.ryszka.imageRestApi.persistenceEntities.ImageDetailsEntity;
import com.ryszka.imageRestApi.persistenceEntities.ImageEntity;
import com.ryszka.imageRestApi.repository.GoogleCloudRepository;
import com.ryszka.imageRestApi.security.AppPossibleLibraryResolutions;
import com.ryszka.imageRestApi.service.dto.ImageDTO;
import com.ryszka.imageRestApi.util.imageScaler.ImageResizer;
import com.ryszka.imageRestApi.util.imageScaler.ResizeDownByHeightForDownload;
import com.ryszka.imageRestApi.util.imageScaler.ResizeDownByWidthForDownload;
import com.ryszka.imageRestApi.viewModels.ImageDetailsList;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public class SaveAndResizeDownloadImg extends GoogleUploadTask{
    private BufferedImage bi;
    private ImageDetailsList imageDetailsList;
    private boolean isLandscape;
    private ImageDTO imageDTO;
    private ImageEntity imageEntity;

    public SaveAndResizeDownloadImg(GoogleCloudRepository cloudRepository,
                                    BufferedImage bi,
                                    ImageDetailsList imageDetailsList,
                                    ImageDTO imageDTO, ImageEntity imageEntity) {
        super(cloudRepository, SaveAndResizeDownloadImg.class);
        this.bi = bi;
        this.imageDetailsList = imageDetailsList;
        this.isLandscape = bi.getWidth() > bi.getHeight();
        this.imageDTO = imageDTO;
        this.imageEntity = imageEntity;
    }

    @Override
    public void run() {
        if (isLandscape) {
            for (int width : AppPossibleLibraryResolutions.WIDTH_LIST.getResolutions()) {
                this.logger.info("Starting to resize image " + width + "...");
                byte[] content1 = imageDTO.getContent();
                ImageResizer imgScaler = new ResizeDownByWidthForDownload(content1, width);
                /*BufferedImage read = ImageIO.read(new ByteArrayInputStream(imageDTO.getContent()));*/

                if (bi.getWidth() > width) {
                    byte[] downloadContent;
                    try {
                        downloadContent = imgScaler.resize();
                        BufferedImage temp = ImageIO.read(new ByteArrayInputStream(downloadContent));
                        this.imageDetailsList.addDetail(new ImageDetailsEntity(
                                width,
                                temp.getHeight(),
                                imageDTO.getFile().getContentType(),
                                downloadContent.length,
                                imageEntity));

                        // store file to cloud
                        cloudRepository.storeImage(
                                "download/landscape/" + width + "/" + imageDTO.getPath(),
                                imageDTO.getName(),
                                downloadContent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // generating file details
                }
            }
        } else {
            for (int height : AppPossibleLibraryResolutions.HEIGHT_LIST.getResolutions()) {
                logger.info("Starting scaling image " + height + "...");
                byte[] content1 = imageDTO.getContent();
                ImageResizer imgScaler = new ResizeDownByHeightForDownload(content1, height);
                /*BufferedImage read = ImageIO.read(new ByteArrayInputStream(imageDTO.getContent()));*/

                if (bi.getHeight() > height) {
                    byte[] downloadContent;
                    try {
                        downloadContent = imgScaler.resize();
                        BufferedImage temp = ImageIO.read(new ByteArrayInputStream(downloadContent));
                        System.out.println(height);
                        this.imageDetailsList.addDetail(new ImageDetailsEntity(
                                temp.getWidth(),
                                height,
                                imageDTO.getFile().getContentType(),
                                downloadContent.length,
                                imageEntity));

                        // store file to cloud
                        cloudRepository.storeImage(
                                "download/portrait/" + height + "/" + imageDTO.getPath(),
                                imageDTO.getName(),
                                downloadContent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // generating file details
                }
            }
        }
    }
}
