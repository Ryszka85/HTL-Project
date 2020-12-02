package com.ryszka.imageRestApi.controller.writeController;

import com.ryszka.imageRestApi.service.dto.ImageDTO;
import com.ryszka.imageRestApi.service.serviceV2.writeService.UpdateUserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "user/set")
public class UserUpdateController {
    private final UpdateUserService updateUserService;

    public UserUpdateController(UpdateUserService updateUserService) {
        this.updateUserService = updateUserService;
    }

    @PostMapping(path = "/profile-image/{userId}", consumes = {"multipart/form-data"})
    public void setUserProfile(@PathVariable String userId,
                               @RequestParam("file") MultipartFile file) {
        /*return this.userService.setUserProfileImg(new ImageDTO(userId, file));*/
        this.updateUserService.setUserProfileImage(new ImageDTO(userId, file));
    }

    @PostMapping(value = "/likes/{imageId}/{userId}")
    public void addUserLike(@PathVariable String imageId,
                            @PathVariable String userId) {
        updateUserService.addUserLike(imageId, userId);
    }
}
