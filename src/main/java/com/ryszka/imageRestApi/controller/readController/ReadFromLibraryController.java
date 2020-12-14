package com.ryszka.imageRestApi.controller.readController;

import com.google.cloud.Identity;
import com.google.cloud.Policy;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.cloud.storage.StorageRoles;
import com.ryszka.imageRestApi.errorHandling.EntityNotFoundException;
import com.ryszka.imageRestApi.errorHandling.ErrorMessages;
import com.ryszka.imageRestApi.security.AppConfigProperties;
import com.ryszka.imageRestApi.service.dto.TagDTO;
import com.ryszka.imageRestApi.viewModels.request.GetImagesByTagRequest;
import com.ryszka.imageRestApi.viewModels.response.UserImageViewModel;
import com.ryszka.imageRestApi.service.serviceV2.readService.ReadUserLibraryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("library/search-by")
public class ReadFromLibraryController {
    private final ReadUserLibraryService readService;

    public ReadFromLibraryController(ReadUserLibraryService readService) {
        this.readService = readService;
    }

    @GetMapping(value = "user/{userId}")
    public ResponseEntity<Optional<List<UserImageViewModel>>> getImagesFromUserGallery(@PathVariable String userId,
                                                                                       HttpServletRequest request) {
        Optional<List<UserImageViewModel>> output = readService.getUserImages(userId);
        return validateQueryAndGetResponse(output,
                ErrorMessages.NOT_FOUND_BY_EID);
    }


    @PostMapping(value = "tags")
    public List<UserImageViewModel> getImageByTagIDs(@RequestBody testStuff t,
                                                           HttpServletRequest request) {
        List<UserImageViewModel> relatedImagesByTagIds = readService
                .getRelatedImagesByTagIds(
                        t.getTags()
                                .stream()
                                .map(TagDTO::getTagId)
                                .collect(Collectors.toList())
                );
        System.out.println(relatedImagesByTagIds.size());
        return relatedImagesByTagIds;
    }

    @GetMapping(value = "image/{imageId}")
    public ResponseEntity<UserImageViewModel> getImageByID(@PathVariable String imageId,
                                                            HttpServletRequest request) {
        return ResponseEntity.ok(readService.getImageByImageId(imageId, request));
    }

    @GetMapping(value = "image/raw/{imageId}")
    public byte[] getRawImageByID(@PathVariable String imageId,
                                                           HttpServletRequest request) throws IOException {
        return readService.getImageBytesByImgId(imageId, request);
    }

    @PostMapping(value = "tag/")
    public ResponseEntity<Optional<List<UserImageViewModel>>> test(@RequestBody GetImagesByTagRequest request) {
        System.out.println("Hallo");
        System.out.println("foo");
        Optional<List<UserImageViewModel>> output = this.readService
                .getImagesByTagNamePageable(request, 0);
        return validateQueryAndGetResponse(output,
                ErrorMessages.IMAGES_NOT_FOUND_BY_TAG);
    }

    public static ResponseEntity<Optional<List<UserImageViewModel>>> validateQueryAndGetResponse(
            Optional<List<UserImageViewModel>> userImagesOpt,
            ErrorMessages error) {
        if (userImagesOpt.isPresent())
            return ResponseEntity
                    .ok(userImagesOpt);
        throw new EntityNotFoundException(error.getMessage());
    }
}

class testStuff {
    private List<TagDTO> tags;

    public testStuff() {
    }

    public List<TagDTO> getTags() {
        return tags;
    }

    public void setTags(List<TagDTO> tags) {
        this.tags = tags;
    }
}
