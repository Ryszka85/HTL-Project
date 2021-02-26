package com.ryszka.imageRestApi.service.serviceV2.writeService;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.ryszka.imageRestApi.config.FireBaseStorageConfig;
import com.ryszka.imageRestApi.dao.ImageDAO;
import com.ryszka.imageRestApi.dao.TagDAO;
import com.ryszka.imageRestApi.errorHandling.ErrorMessages;
import com.ryszka.imageRestApi.persistenceEntities.ImageDetailsEntity;
import com.ryszka.imageRestApi.persistenceEntities.ImageEntity;
import com.ryszka.imageRestApi.persistenceEntities.TagEntity;
import com.ryszka.imageRestApi.repository.GoogleCloudRepository;
import com.ryszka.imageRestApi.repository.TagRepository;
import com.ryszka.imageRestApi.security.AppPossibleLibraryResolutions;
import com.ryszka.imageRestApi.service.dto.ImageDTO;
import com.ryszka.imageRestApi.repository.ImageRepository;
import com.ryszka.imageRestApi.util.imageScaler.*;
import com.ryszka.imageRestApi.util.mapper.ObjectMapper;
import com.ryszka.imageRestApi.util.mapper.mapStrategies.ImageDtoToImageEntity;
import com.ryszka.imageRestApi.viewModels.ImageDetailsList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class DatabaseAndFTPStorageService {
    private final Logger logger =
            LoggerFactory.getLogger(DatabaseAndFTPStorageService.class);
    private final ImageRepository imageRepository;
    private final TransactionTemplate transactionTemplate;
    private final ImageDAO imageDAO;
    private TagDAO tagDao;
    private final FireBaseStorageConfig storageConfig;
    private final GoogleCloudRepository cloudRepository;

    public DatabaseAndFTPStorageService(ImageRepository imageRepository,
                                        TransactionTemplate transactionTemplate,
                                        ImageDAO imageDAO, FireBaseStorageConfig storageConfig,
                                        GoogleCloudRepository cloudRepository,
                                        TagDAO tagDAO) {
        this.imageRepository = imageRepository;
        this.transactionTemplate = transactionTemplate;
        this.imageDAO = imageDAO;
        this.storageConfig = storageConfig;
        this.cloudRepository = cloudRepository;
        this.tagDao = tagDAO;
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

    public void storeToDbAndFTPInTransaction(ImageDTO imageDTO,
                                             TagDAO tagDAO) {
        this.tagDao = tagDAO;
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


                BufferedImage bi = ImageIO.read(imageDTO.getInputStream());
                ImageEntity imageEntity = ObjectMapper.mapByStrategy(imageDTO, new ImageDtoToImageEntity());

                List<ImageDetailsEntity> imageDetailsEntityList = new ArrayList<>();

                ImageDetailsList imageDetailsList = new ImageDetailsList();
                List<GoogleUploadTask> saveTasks = new ArrayList<>();
                /*saveTasks.add(new SaveOriginalImg(
                        cloudRepository,
                        imageDetailsList,
                        imageDTO,
                        bi,
                        imageEntity));*/



                /*saveTasks.add(new SaveOriginalImg(cloudRepository, imageDetailsList, imageDTO, bi, imageEntity));
                saveTasks.add(new SaveDownloadViewImg(cloudRepository, imageDTO));
                saveTasks.add(new SaveGalleryImg(cloudRepository, imageDTO, bi));
                saveTasks.add(new SaveAndResizeDownloadImg(cloudRepository, bi, imageDetailsList, imageDTO, imageEntity));
                new GoogleUploadTaskProcessor(saveTasks)
                        .processTasks();*/


                List<Runnable> runnables = new ArrayList<>();

                imageDetailsList.addDetail(new ImageDetailsEntity(
                        bi.getWidth(),
                        bi.getHeight(),
                        imageDTO.getFile().getContentType(),
                        imageDTO.getFile().getSize(),
                        imageEntity));



                new Thread(() -> {

                    try {
                        cloudRepository.storeImage("original/" + imageDTO.getPath(), imageDTO.getName(), imageDTO.getContent());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();

                /*imageDetailsEntityList.add(new ImageDetailsEntity(
                        bi.getWidth(),
                        bi.getHeight(),
                        imageDTO.getFile().getContentType(),
                        imageDTO.getFile().getSize(),
                        imageEntity));

                cloudRepository.storeImage("original/" + imageDTO.getPath(), imageDTO.getName(), imageDTO.getContent());*/

                /*ImageResizer galleryScaler = new ResizeDownForGallery(imageDTO.getContentGalleryFile());*/

                new Thread(() -> {
                    byte[] bytes = imageDTO.getContent();
                    ImageResizer galleryScaler = new ResizeGalleryImage(bi.getWidth(), bi.getHeight(), bytes);
                    byte[] galleryContent = new byte[0];
                    try {
                        galleryContent = galleryScaler.resize();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        cloudRepository.storeImage(
                                "gallery" + "/" + imageDTO.getPath(),
                                imageDTO.getName(),
                                galleryContent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
                /*byte[] bytes = imageDTO.getContent();
                ImageResizer galleryScaler = new ResizeGalleryImage(bi.getWidth(), bi.getHeight(), bytes);
                byte[] galleryContent = galleryScaler.resize();
                cloudRepository.storeImage(
                        "gallery" + "/" + imageDTO.getPath(),
                        imageDTO.getName(),
                        galleryContent);*/

                new Thread(() -> {
                    try {
                        cloudRepository.storeImage(
                                "downloadView" + "/" + imageDTO.getPath(),
                                imageDTO.getName(),
                                imageDTO.getContentDownloadFile());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();

                /*cloudRepository.storeImage(
                        "downloadView" + "/" + imageDTO.getPath(),
                        imageDTO.getName(),
                        imageDTO.getContentDownloadFile());*/


                if (bi.getWidth() > bi.getHeight()) {
                    for (int width : AppPossibleLibraryResolutions.WIDTH_LIST.getResolutions()) {
                        runnables.add(() -> {
                            logger.info("Starting to resize image " + width + "...");
                            byte[] content1 = imageDTO.getContent();
                            ImageResizer imgScaler = new ResizeDownByWidthForDownload(content1, width);
                            try {
                                BufferedImage read = ImageIO.read(new ByteArrayInputStream(imageDTO.getContent()));
                                if (bi.getWidth() > width) {
                                    byte[] downloadContent = imgScaler.resize();
                                    // generating file details
                                    BufferedImage temp = ImageIO.read(new ByteArrayInputStream(downloadContent));
                                    imageDetailsList.addDetail(new ImageDetailsEntity(
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
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        });
                        /*logger.info("Starting to resize image " + width + "...");
                        byte[] content1 = imageDTO.getContent();
                        ImageResizer imgScaler = new ResizeDownByWidthForDownload(content1, width);
                        BufferedImage read = ImageIO.read(new ByteArrayInputStream(imageDTO.getContent()));

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
                                    "download/landscape/" + width + "/" + imageDTO.getPath(),
                                    imageDTO.getName(),
                                    downloadContent);
                        }*/
                    }
                } else {
                    for (int height : AppPossibleLibraryResolutions.HEIGHT_LIST.getResolutions()) {
                        System.out.println(height);
                        runnables.add(() -> {
                            logger.info("Starting scaling image " + height + "...");
                            byte[] content1 = imageDTO.getContent();
                            ImageResizer imgScaler = new ResizeDownByHeightForDownload(content1, height);
                            try {
                                BufferedImage read = ImageIO.read(new ByteArrayInputStream(imageDTO.getContent()));
                                if (bi.getHeight() > height) {
                                    byte[] downloadContent = imgScaler.resize();
                                    // generating file details
                                    BufferedImage temp = ImageIO.read(new ByteArrayInputStream(downloadContent));
                                    System.out.println(height);
                                    imageDetailsList.addDetail(new ImageDetailsEntity(
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
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        });
                        /*logger.info("Starting scaling image " + height + "...");
                        byte[] content1 = imageDTO.getContent();
                        ImageResizer imgScaler = new ResizeDownByHeightForDownload(content1, height);
                        BufferedImage read = ImageIO.read(new ByteArrayInputStream(imageDTO.getContent()));

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
                                    "download/portrait/" + height + "/" + imageDTO.getPath(),
                                    imageDTO.getName(),
                                    downloadContent);
                        }*/
                    }
                }


                AtomicInteger index = new AtomicInteger(0);
                CountDownLatch latch = new CountDownLatch(3);
                System.out.println(runnables.size());
                for (int i = 0; i < 3; i++) {
                    new Thread(() -> {
                        int tempIndex = index.getAndIncrement();
                        while (tempIndex < (runnables).size()) {
                            runnables.get(tempIndex).run();
                            tempIndex = index.getAndIncrement();
                        }
                        latch.countDown();
                    }).start();
                }
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                /*imageEntity.setImageDetailsEntities(imageDetailsEntityList);*/

                imageEntity.setImageDetailsEntities(imageDetailsList.getDetailList());


                /*DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                LocalDate now = LocalDate.now();*/

                imageEntity.setUploadDate(Date.valueOf(LocalDate.now()));


                // TODO: 16.02.2021 Change save tags because duplicate tags occur in database!!! 

                if (!imageDTO.getTagList().isEmpty()) {

                    List<TagEntity> tagEntities = this.tagDao
                            .findAllByTag(imageDTO.getTagList());
                    List<TagEntity> newTagNames = new ArrayList<>();
                    if (tagEntities.size() > 0) {
                        System.out.println(newTagNames.size());

                        newTagNames = imageDTO.getTagList()
                                .stream()
                                .filter(newTag -> !tagEntities.contains(newTag))
                                .map(newTag -> new TagEntity(newTag))
                                .collect(Collectors.toList());

                        /*this.tagDao.saveAllTags(newTagNames);*/

                        List<TagEntity> allByTag = this.tagDao.findAllByTag(imageDTO.getTagList());

                        imageEntity.setTags(allByTag);
                        /*this.tagDao.saveAllTags(newTagNames);*/
                    } else {
                        newTagNames = imageDTO.getTagList()
                                .stream()
                                .map(TagEntity::new)
                                .collect(Collectors.toList());
                        /*this.tagDao.saveAllTags(newTagNames);*/
                        imageEntity.setTags(newTagNames);
                    }
                }


                /*imageEntity.setTags();*/
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
