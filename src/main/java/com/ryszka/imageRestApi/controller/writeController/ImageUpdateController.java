package com.ryszka.imageRestApi.controller.writeController;

import com.ryszka.imageRestApi.service.dto.ImageDTO;
import com.ryszka.imageRestApi.viewModels.request.SetTagsToImageRequest;
import com.ryszka.imageRestApi.service.serviceV2.writeService.ModifyImageFromLibraryService;
import com.ryszka.imageRestApi.viewModels.response.UserImageViewModel;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "image")
public class ImageUpdateController {
    private final ModifyImageFromLibraryService modifyImageFromLibraryService;

    public ImageUpdateController(ModifyImageFromLibraryService modifyImageFromLibraryService) {
        this.modifyImageFromLibraryService = modifyImageFromLibraryService;
    }

    @PostMapping(value = "delete")
    public void deleteFromLibrary(@RequestBody UserImageViewModel request) {
        this.modifyImageFromLibraryService.deleteImageFromLibrary(request);
    }

    @PostMapping(value = "update/details")
    public void updateImageDetails(@RequestBody UserImageViewModel updateRequest) {
        modifyImageFromLibraryService.changeImageDetails(updateRequest);
        /*updateImageService.setTags(new ImageDTO(updateRequest));*/
    }

    @PostMapping(value = "update/tags")
    public void setImageTags(@RequestBody SetTagsToImageRequest updateRequest) {
        System.out.println(updateRequest);
        modifyImageFromLibraryService.setTags(new ImageDTO(updateRequest));
    }

}
