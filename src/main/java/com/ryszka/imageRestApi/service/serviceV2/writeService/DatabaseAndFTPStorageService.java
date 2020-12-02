package com.ryszka.imageRestApi.service.serviceV2.writeService;

import com.ryszka.imageRestApi.config.FireBaseStorageConfig;
import com.ryszka.imageRestApi.dao.ImageDAO;
import com.ryszka.imageRestApi.errorHandling.ErrorMessages;
import com.ryszka.imageRestApi.persistenceEntities.ImageDetailsEntity;
import com.ryszka.imageRestApi.persistenceEntities.ImageEntity;
import com.ryszka.imageRestApi.repository.GoogleCloudRepository;
import com.ryszka.imageRestApi.security.AppPossibleLibraryResolutions;
import com.ryszka.imageRestApi.service.dto.ImageDTO;
import com.ryszka.imageRestApi.repository.ImageRepository;
import com.ryszka.imageRestApi.util.imageScaler.ImageResizer;
import com.ryszka.imageRestApi.util.imageScaler.ResizeDownByHeightForDownload;
import com.ryszka.imageRestApi.util.imageScaler.ResizeDownByWidthForDownload;
import com.ryszka.imageRestApi.util.imageScaler.ResizeDownForGallery;
import com.ryszka.imageRestApi.util.mapper.ObjectMapper;
import com.ryszka.imageRestApi.util.mapper.mapStrategies.ImageDtoToImageEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class DatabaseAndFTPStorageService {
    private final Logger logger =
            LoggerFactory.getLogger(DatabaseAndFTPStorageService.class);
    private final ImageRepository imageRepository;
    private final TransactionTemplate transactionTemplate;
    private final ImageDAO imageDAO;
    private final FireBaseStorageConfig storageConfig;
    private final GoogleCloudRepository cloudRepository;

    public DatabaseAndFTPStorageService(ImageRepository imageRepository,
                                        TransactionTemplate transactionTemplate,
                                        ImageDAO imageDAO, FireBaseStorageConfig storageConfig,
                                        GoogleCloudRepository cloudRepository) {
        this.imageRepository = imageRepository;
        this.transactionTemplate = transactionTemplate;
        this.imageDAO = imageDAO;
        this.storageConfig = storageConfig;
        this.cloudRepository = cloudRepository;
    }

    /*public DatabaseAndFTPStorageService(ImageRepository imageRepository,
                                        TransactionTemplate transactionTemplate,
                                        ImageDAO imageDAO,
                                        FireBaseStorageConfig storageConfig) {
        this.imageRepository = imageRepository;
        this.transactionTemplate = transactionTemplate;
        this.imageDAO = imageDAO;
        this.storageConfig = storageConfig;
    }*/

    /*public DatabaseAndFTPStorageService(ImageRepository imageRepository,
                                        TransactionTemplate transactionTemplate,
                                        ImageDAO imageDAO) {
        this.imageRepository = imageRepository;
        this.transactionTemplate = transactionTemplate;
        this.imageDAO = imageDAO;
    }*/

    public void storeToDbAndFTPInTransaction(ImageDTO imageDTO) {
        transactionTemplate.execute(savFile(imageDTO));
    }

    @Transactional
    public TransactionCallback<Boolean> savFile(ImageDTO imageDTO) {
        return transactionStatus -> {
            FtpStorageService storageService = new FtpStorageService();
            boolean storeFtpStatus = false;
            boolean dbStoreStatus = false;
            try {
                /*storeFtpStatus = storageService.makeDirAndSaveFileToFileSystem(
                        new FtpPersistenceEntity(imageDTO.getPath() + "/" + imageDTO.getName(), imageDTO.getInputStream())
                );*/
                /*imageScaler.scaleDown(imageDTO.getInputStream())*/
                List<ImageDetailsEntity> imageDetailsEntityList = new ArrayList<>();

                BufferedImage bi = ImageIO.read(imageDTO.getInputStream());
                ImageEntity imageEntity = ObjectMapper.mapByStrategy(imageDTO, new ImageDtoToImageEntity());
                imageDetailsEntityList.add(new ImageDetailsEntity(
                        bi.getWidth(),
                        bi.getHeight(),
                        imageDTO.getFile().getContentType(),
                        imageDTO.getFile().getSize(),
                        imageEntity));

                cloudRepository.storeImage("original/" + imageDTO.getPath(), imageDTO.getName(), imageDTO.getContent());

                ImageResizer galleryScaler = new ResizeDownForGallery(imageDTO.getContentGalleryFile());
                byte[] galleryContent = galleryScaler.resize();
                cloudRepository.storeImage(
                        "gallery" + "/" + imageDTO.getPath(),
                        imageDTO.getName(),
                        galleryContent);

                cloudRepository.storeImage(
                        "downloadView" + "/" + imageDTO.getPath(),
                        imageDTO.getName(),
                        imageDTO.getContentDownloadFile());


                if (bi.getWidth() > bi.getHeight()) {
                    for (int width : AppPossibleLibraryResolutions.WIDTH_LIST.getResolutions()) {
                        logger.info("Starting to resize image " + width + "...");
                        byte[] content1 = imageDTO.getContent();
                        ImageResizer imgScaler = new ResizeDownByWidthForDownload(content1, width);
                        /*BufferedImage read = ImageIO.read(new ByteArrayInputStream(imageDTO.getContent()));*/

                        if (bi.getWidth() > width) {
                            byte[] downloadContent = imgScaler.resize();
                            // generating file details
                            BufferedImage temp = ImageIO.read(new ByteArrayInputStream(downloadContent));
                            imageDetailsEntityList.add(new ImageDetailsEntity(
                                    width,
                                    temp.getHeight(),
                                    imageDTO.getFile().getContentType(),
                                    downloadContent.length,
                                    imageEntity));

                            // store file to cloud
                            cloudRepository.storeImage(
                                    "download/" + width + "/" + imageDTO.getPath(),
                                    imageDTO.getName(),
                                    downloadContent);
                        }
                    }
                } else {
                    for (int height : AppPossibleLibraryResolutions.HEIGHT_LIST.getResolutions()) {
                        logger.info("Starting scaling image " + height + "...");
                        byte[] content1 = imageDTO.getContent();
                        ImageResizer imgScaler = new ResizeDownByHeightForDownload(content1, height);
                        /*BufferedImage read = ImageIO.read(new ByteArrayInputStream(imageDTO.getContent()));*/

                        if (bi.getHeight() > height) {
                            byte[] downloadContent = imgScaler.resize();
                            // generating file details
                            BufferedImage temp = ImageIO.read(new ByteArrayInputStream(downloadContent));
                            System.out.println(height);
                            imageDetailsEntityList.add(new ImageDetailsEntity(
                                    temp.getWidth(),
                                    height,
                                    imageDTO.getFile().getContentType(),
                                    downloadContent.length,
                                    imageEntity));

                            // store file to cloud
                            cloudRepository.storeImage(
                                    "download/" + height + "/" + imageDTO.getPath(),
                                    imageDTO.getName(),
                                    downloadContent);
                        }
                    }
                }
                imageEntity.setImageDetailsEntities(imageDetailsEntityList);
                /*DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                LocalDate now = LocalDate.now();*/

                imageEntity.setUploadDate(Date.valueOf(LocalDate.now()));
                logger.info("Starting storing file " + imageDTO.getName() + " to db...");
                dbStoreStatus = imageDAO.saveImage(imageEntity);
                logger.info("Finished storing file " + imageDTO.getName() + " to db...");
            } catch (Exception e) {
                logger.error(e.getMessage());
                transactionStatus.setRollbackOnly();
                logger.error(ErrorMessages.ROLLED_BACK_IN_TX.getMessage());
            }
            return storeFtpStatus && dbStoreStatus;
        };
    }
}
