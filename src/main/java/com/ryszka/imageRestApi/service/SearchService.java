package com.ryszka.imageRestApi.service;

import com.ryszka.imageRestApi.service.dto.TagDTO;
import com.ryszka.imageRestApi.service.serviceV2.readService.ReadTagsService;
import com.ryszka.imageRestApi.service.serviceV2.readService.ReadUserDetailsService;
import com.ryszka.imageRestApi.service.serviceV2.writeService.AddToUserLibraryService;
import com.ryszka.imageRestApi.util.mapper.dbMappers.ZipCodesAndCitiesByRegion;
import com.ryszka.imageRestApi.viewModels.response.UserDetailsResponseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService {
    public final ReadTagsService readTagsService;
    public final AddressService addressService;
    private final ReadUserDetailsService userDetailsService;
    private final Logger logger = LoggerFactory.getLogger(SearchService.class);

    public SearchService(ReadTagsService readTagsService,
                         AddressService addressService,
                         ReadUserDetailsService userDetailsService) {
        this.readTagsService = readTagsService;
        this.addressService = addressService;
        this.userDetailsService = userDetailsService;
    }

    public List<ZipCodesAndCitiesByRegion> searchByZipAndRegion(String region, String zip) {
        logger.info("Preparing search by zip and region ..");
        return addressService.validateAddress(zip, region).join();
    }

    public List<TagDTO> searchByTag(String tag) {
        logger.info("Preparing search by tag ..");
        return readTagsService.getTagsLikeSearchTerm(tag);
    }

    public List<UserDetailsResponseModel> searchUsers(String email) {
        logger.info("Preparing search by user email ..");
        List<UserDetailsResponseModel> allByEmail = userDetailsService.findAllByEmail(email);
        return allByEmail.stream()
                .map(byEmail -> userDetailsService.getUserDetailsByUserId(byEmail.getUserId()))
                .collect(Collectors.toList());
    }
}
