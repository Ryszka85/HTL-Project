package com.ryszka.imageRestApi.controller.readController;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.ryszka.imageRestApi.security.AppConfigProperties;
import com.ryszka.imageRestApi.service.dto.TagDTO;
import com.ryszka.imageRestApi.service.serviceV2.readService.ReadTagsService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("tags")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:8080", "http://178.112.217.30:8080"}, methods = {RequestMethod.GET, RequestMethod.POST})
public class ReadTagsController {
    private final ReadTagsService tagService;

    public ReadTagsController(ReadTagsService tagService) {
        this.tagService = tagService;
    }

    @GetMapping(value = "oida")
    public String foo() {
        return "OIDAAAA!!";
    }

    @GetMapping(path = "search/{term}")
    public List<TagDTO> getTagsLikeTerm(@PathVariable String term) throws IOException {
        return tagService.getTagsLikeSearchTerm(term);
    }

    @GetMapping
    public List<TagDTO> getAllTags() {
        return tagService.getAllTags();
    }


}
