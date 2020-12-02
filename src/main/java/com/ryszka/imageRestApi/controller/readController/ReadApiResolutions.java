package com.ryszka.imageRestApi.controller.readController;

import com.ryszka.imageRestApi.viewModels.response.ApiResolutionsResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/resolutions")
public class ReadApiResolutions {

    @GetMapping()
    public ApiResolutionsResponse getResolutions() {
        System.out.println("SEAS");
        return new ApiResolutionsResponse();
    }
}
