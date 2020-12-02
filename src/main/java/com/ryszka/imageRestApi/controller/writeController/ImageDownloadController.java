package com.ryszka.imageRestApi.controller.writeController;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.ryszka.imageRestApi.config.FireBaseStorageConfig;
import com.ryszka.imageRestApi.dao.ImageDAO;
import com.ryszka.imageRestApi.dao.SessionDAO;
import com.ryszka.imageRestApi.errorHandling.EntityNotFoundException;
import com.ryszka.imageRestApi.errorHandling.ErrorMessages;
import com.ryszka.imageRestApi.persistenceEntities.ImageEntity;
import com.ryszka.imageRestApi.persistenceEntities.SessionEntity;
import com.ryszka.imageRestApi.persistenceEntities.UserEntity;
import com.ryszka.imageRestApi.security.AppConfigProperties;
import com.ryszka.imageRestApi.service.serviceV2.readService.GoogleMediaDownloadLink;
import com.ryszka.imageRestApi.service.serviceV2.writeService.FileDownloadService;
import com.ryszka.imageRestApi.util.imageScaler.ResizeByIndividualResolution;
import com.ryszka.imageRestApi.util.imageScaler.ResizeDownCroppedImage;
import com.ryszka.imageRestApi.viewModels.request.CroppedImageValuesRequest;
import com.ryszka.imageRestApi.viewModels.request.DownloadImgByIndividualResolution;
import com.ryszka.imageRestApi.viewModels.response.ImageDetailViewModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URISyntaxException;
import java.util.Optional;

@RestController
@RequestMapping("library/download")
public class ImageDownloadController {
    private final FileDownloadService fileService;
    private final FireBaseStorageConfig fireBaseStorageConfig;
    private final ImageDAO imageDAO;
    private final SessionDAO sessionDAO;
    private final Logger logger =
            LoggerFactory.getLogger(ImageDownloadController.class);

    public ImageDownloadController(FileDownloadService fileService,
                                   FireBaseStorageConfig fireBaseStorageConfig,
                                   ImageDAO imageDAO,
                                   SessionDAO sessionDAO) {
        this.fileService = fileService;
        this.fireBaseStorageConfig = fireBaseStorageConfig;
        this.imageDAO = imageDAO;
        this.sessionDAO = sessionDAO;
    }



    @PostMapping(value = "resized/file")
    public byte[] downloadByIndividualResolution(@RequestBody DownloadImgByIndividualResolution request) throws IOException {
        ImageEntity imageEntity = this.imageDAO
                .getImageByImageId(request.getImageId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.NOT_FOUND_BY_EID.getMessage()));
        String originalFilePath = "original/" + imageEntity.getUserEntity().getUserId() + "/" + imageEntity.getName();
        BlobId blobId = BlobId.of(AppConfigProperties.BUCKET_NAME, originalFilePath);
        Storage storage = fireBaseStorageConfig.initAndGetStorage();
        byte[] content = storage.get(blobId).getContent();
        imageEntity.setDownloaded(imageEntity.getDownloaded() + 1);
        imageDAO.saveImage(imageEntity);
        return new ResizeByIndividualResolution( (int) request.getWidth(), (int) request.getHeight(), content).resize();
    }

    @PostMapping(value = "cropped/file/")
    public byte[] downloadCroppedImage(@RequestBody CroppedImageValuesRequest request) throws IOException {
        // check if imageEntity exists
        ImageEntity imageEntity = this.imageDAO.getImageByImageId(request.getImageId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.INVALID_ARGUMENTS.getMessage()));
        String originalFilePath = "original/" + imageEntity.getUserEntity().getUserId() + "/" + imageEntity.getName();
        BlobId blobId = BlobId.of(AppConfigProperties.BUCKET_NAME, originalFilePath);
        Storage storage = fireBaseStorageConfig.initAndGetStorage();
        byte[] content = storage.get(blobId).getContent();
        imageEntity.setDownloaded(imageEntity.getDownloaded() + 1);
        imageDAO.saveImage(imageEntity);
        return new ResizeDownCroppedImage(content, request).resize();
    }


    @PostMapping(value = "/file/{imageId}")
    public ImageDetailViewModel downloadFile(@PathVariable String imageId,
                                             @RequestBody ImageDetailViewModel imageDetailViewModel,
                                             HttpServletRequest httpReq) throws IOException, URISyntaxException {
        /*return fileService.getFileSystem(imageId, userId, httpResp);*/
        // validate first if image is in library
        Optional<ImageEntity> imageByImageId = imageDAO.getImageByImageId(imageId);
        imageByImageId
                .orElseThrow(() -> new EntityNotFoundException(
                        ErrorMessages.IMAGES_NOT_FOUND_BY_ID.getMessage()));
        ImageEntity imageEntity = imageByImageId.get();


        // validate if user is owner of the picture
        // when yes let user download private images
        // when user is not owner than only public images can be downloaded
        Optional<SessionEntity> userBySessionIDOpt =
                sessionDAO.findUserBySessionID(httpReq.getSession().getId());
        UserEntity userEntity = imageEntity.getUserEntity();


        Storage storage = fireBaseStorageConfig.initAndGetStorage();
        /*List<ImageDetailViewModel> response = new ArrayList<>();*/
        ImageDetailViewModel response = null;

        System.out.println(imageDetailViewModel.getWidth());

        String downloadPathWidth = "download/" + imageDetailViewModel.getWidth() + "/" + userEntity.getUserId() + "/" + imageEntity.getName();
        String downloadPathHeight = "download/" + imageDetailViewModel.getHeight() + "/" + userEntity.getUserId() + "/" + imageEntity.getName();
        String originalFilePath = "original/" + userEntity.getUserId() + "/" + imageEntity.getName();

        GoogleMediaDownloadLink googleMediaDownloadLinkWidth = new GoogleMediaDownloadLink(downloadPathWidth, fireBaseStorageConfig);
        GoogleMediaDownloadLink googleMediaDownloadLinkHeight = new GoogleMediaDownloadLink(downloadPathHeight, fireBaseStorageConfig);

        Optional<ImageDetailViewModel> resp = Optional.empty();
        if (googleMediaDownloadLinkWidth.generateLink() == null && googleMediaDownloadLinkHeight.generateLink() == null) {
            resp = Optional.ofNullable(new GoogleMediaDownloadLink(originalFilePath, fireBaseStorageConfig).generateLink());
        } else if (googleMediaDownloadLinkWidth.generateLink() == null) {
            resp = Optional.ofNullable(googleMediaDownloadLinkHeight.generateLink());
        } else resp = Optional.ofNullable(googleMediaDownloadLinkWidth.generateLink());


        if (resp.isPresent()) {
            logger.info("is public ? : {}", imageEntity.getIsPublic());
            if (userBySessionIDOpt.isPresent() &&
                    userBySessionIDOpt.get().getPrincipal() != null &&
                    userBySessionIDOpt.get().getPrincipal().equals(userEntity.getEmail())) {
                this.logger.info("User is owner");
                imageEntity.setDownloaded(imageEntity.getDownloaded() + 1);
                imageDAO.saveImage(imageEntity);
                return resp.get();
            } else if (imageEntity.getIsPublic()) {
                this.logger.info("User is not owner but image is public");
                imageEntity.setDownloaded(imageEntity.getDownloaded() + 1);
                imageDAO.saveImage(imageEntity);
                return resp.get();
            }
            logger.error("Invalid download image request");
        }
        return null;
    }

}


class TestShit implements Serializable {
    public String foo;

    public TestShit() {
    }

    public String getFoo() {
        return foo;
    }

    public void setFoo(String foo) {
        this.foo = foo;
    }
}
