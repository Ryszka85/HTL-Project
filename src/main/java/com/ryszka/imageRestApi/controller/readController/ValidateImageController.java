package com.ryszka.imageRestApi.controller.readController;

import com.ryszka.imageRestApi.errorHandling.ErrorMessages;
import com.ryszka.imageRestApi.service.serviceV2.readService.ImageRecognitionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

@RestController
@RequestMapping(value = "library")
public class ValidateImageController {
    @Autowired
    private RestTemplate restTemplate;
    private ImageRecognitionService imageRecognitionService;


    public ValidateImageController(ImageRecognitionService imageRecognitionService) {
        this.imageRecognitionService = imageRecognitionService;
    }

    private final Logger logger = LoggerFactory.getLogger(ValidateImageController.class);

    @PostMapping(value = "validate-image")
    public ValidateImageResponse validateImage(@RequestBody MultipartFile file) throws IOException {


        /*String url = "http://212.186.15.55:8080/getlabels/";
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Req> entity = new HttpEntity<Req>(new Req(file.getInputStream().readAllBytes()) ,headers);
        List<String> body = restTemplate.exchange(url, HttpMethod.POST, entity, List.class).getBody();

        body.forEach(System.out::println);*/



        /*HttpHeaders headers = new HttpHeaders();
         *//*headers.setContentType(MediaType.MULTIPART_FORM_DATA);*//*

        MultiValueMap<String, Object> body
                = new LinkedMultiValueMap<>();
        *//*byte[] bytes = file.getInputStream().readAllBytes();*//*
        body.add("img", file.getInputStream());

        HttpEntity<MultiValueMap<String, Object>> requestEntity
                = new HttpEntity<>(body, headers);

        String serverUrl = "http://localhost:8082/spring-rest/fileserver/multiplefileupload/";

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate
                .postForEntity(url, requestEntity, String.class);*/




        /*restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        String s = restTemplate.postForObject("http://212.186.15.55:8080/getlabels", file, String.class);


        System.out.println(s);*/

        BufferedImage image = ImageIO.read(file.getInputStream());
        logger.info("Starting to validate image " +
                file.getOriginalFilename() + ": " +
                image.getWidth() + " x " + image.getHeight() +
                ", total : " + image.getHeight() * image.getWidth());
        final int resReq = image.getWidth() * image.getHeight();
        final int minRes = 1920 * 1080;
        if ((image.getWidth() < 500 || image.getHeight() < 460) &&
                resReq < minRes) {
            return new ValidateImageResponse(ErrorMessages.ILLEGAL_IMAGE_SIZE.getMessage(),
                    400);
        }
        logger.info("Image was valid");
        List<String> imageTags = imageRecognitionService
                .getTagsFromImage(file)
                .getImageTags();
        return new ValidateImageResponse(
                "Image was valid",
                200,
                image.getWidth(),
                image.getHeight(),
                imageTags
        );
    }
}

class ValidateImageResponse {
    private String message;
    private int status, width, height;
    private List<String> tags;

    public ValidateImageResponse(String message, int status, int width, int height) {
        this.message = message;
        this.status = status;
        this.width = width;
        this.height = height;
    }

    public ValidateImageResponse(String message, int status,
                                 int width, int height,
                                 List<String> tags) {
        this.message = message;
        this.status = status;
        this.width = width;
        this.height = height;
        this.tags = tags;
    }

    public ValidateImageResponse(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}


class Req implements Serializable {
    private byte[] img;

    public Req() {
    }

    public Req(byte[] img) {
        this.img = img;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }
}


class Test {
    private String s;

    public Test() {
    }

    public Test(String s) {
        this.s = s;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }
}

