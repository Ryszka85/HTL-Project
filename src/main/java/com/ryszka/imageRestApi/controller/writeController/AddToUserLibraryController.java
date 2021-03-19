package com.ryszka.imageRestApi.controller.writeController;

import com.ryszka.imageRestApi.errorHandling.ErrorMessages;
import com.ryszka.imageRestApi.errorHandling.ScalingImageException;
import com.ryszka.imageRestApi.service.serviceV2.writeService.FileDownloadService;
import com.ryszka.imageRestApi.service.serviceV2.writeService.AddToUserLibraryService;
import com.ryszka.imageRestApi.service.dto.ImageDTO;
import com.ryszka.imageRestApi.viewModels.request.AddToLibraryRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("images")
@CrossOrigin(origins = {"88.xxx.xxx.xxx:4200"}, methods = {RequestMethod.GET, RequestMethod.POST})
public class AddToUserLibraryController {
    private final AddToUserLibraryService libraryStorageService;
    private final FileDownloadService fileService;
    private Logger logger = LoggerFactory.getLogger(AddToUserLibraryController.class);

    public AddToUserLibraryController(AddToUserLibraryService imageWriteRequestService, FileDownloadService fileService) {
        this.fileService = fileService;
        this.libraryStorageService = imageWriteRequestService;
    }

    @PostMapping(path = "insert/")
    public void addImageToUserLibrary(@ModelAttribute AddToLibraryRequest request) {
        try {
            this.libraryStorageService.addImageToUserLibrary(new ImageDTO(request));
        } catch (Exception e) {
            throw new ScalingImageException(ErrorMessages.SCALING_ERROR.getMessage());
        }
    }

}
