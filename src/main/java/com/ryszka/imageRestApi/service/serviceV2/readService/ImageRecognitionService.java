package com.ryszka.imageRestApi.service.serviceV2.readService;

import com.ryszka.imageRestApi.controller.readController.ValidateImageController;
import com.ryszka.imageRestApi.security.AppConfigProperties;
import com.ryszka.imageRestApi.service.dto.TagDTO;
import com.ryszka.imageRestApi.viewModels.request.ImageRecognitionRequest;
import com.ryszka.imageRestApi.viewModels.response.ImageRecognitionTagsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImageRecognitionService {
    private RestTemplate restTemplate;
    private final Logger logger = LoggerFactory
            .getLogger(ImageRecognitionService.class);

    public ImageRecognitionService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public ImageRecognitionTagsResponse getTagsFromImage(MultipartFile img) throws IOException {
        logger.info("Starting [ getTagsFromImage ] .....");
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<ImageRecognitionRequest> entity = new HttpEntity<>(
                new ImageRecognitionRequest(img
                        .getInputStream()
                        .readAllBytes()),
                headers
        );
        logger.info("Starting to call imageRecognition service.....");
        List<String> fetchedTags = restTemplate.exchange(
                AppConfigProperties.IMAGE_RECOGNITION_SERVICE_URL,
                HttpMethod.POST,
                entity,
                List.class).getBody();
        logger.info("Finished request imageRecognition service.....");
        return new ImageRecognitionTagsResponse(
                fetchedTags.stream()
                        .collect(Collectors.toList())
        );
    }

}
