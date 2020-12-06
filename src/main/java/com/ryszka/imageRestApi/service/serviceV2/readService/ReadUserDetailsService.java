package com.ryszka.imageRestApi.service.serviceV2.readService;

import com.ryszka.imageRestApi.dao.SessionDAO;
import com.ryszka.imageRestApi.dao.UserDAO;
import com.ryszka.imageRestApi.errorHandling.EntityNotFoundException;
import com.ryszka.imageRestApi.errorHandling.ErrorMessages;
import com.ryszka.imageRestApi.persistenceEntities.ImageEntity;
import com.ryszka.imageRestApi.persistenceEntities.SessionEntity;
import com.ryszka.imageRestApi.repository.UserRepository;
import com.ryszka.imageRestApi.util.mapper.mapStrategies.ImageEntitiesToImageRespModels;
import com.ryszka.imageRestApi.util.mapper.mapStrategies.UserEntityToImageRespModels;
import com.ryszka.imageRestApi.viewModels.response.UserDetailsResponseModel;
import com.ryszka.imageRestApi.persistenceEntities.UserEntity;
import com.ryszka.imageRestApi.util.mapper.ObjectMapper;
import com.ryszka.imageRestApi.util.mapper.mapStrategies.UserEntityToUserDetailsResponseModel;
import com.ryszka.imageRestApi.viewModels.response.UserImageViewModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
public class ReadUserDetailsService {
    private final Logger logger =
            LoggerFactory.getLogger(ReadUserDetailsService.class);
    private final UserDAO userDAO;
    private final SessionDAO sessionDAO;
    private UserRepository userRepository;

    public ReadUserDetailsService(UserDAO userDAO, SessionDAO sessionDAO, UserRepository userRepository) {
        this.userDAO = userDAO;
        this.sessionDAO = sessionDAO;
        this.userRepository = userRepository;
    }

    public UserDetailsResponseModel getUserDetailsByUserId(String userId) {
        logger.info("Attempting [ getUserDetailsByUserId.. ]");
        Optional<UserEntity> optUserEntity = userDAO.findUserEntityByUserId(userId);
        if (optUserEntity.isEmpty())
            throw new EntityNotFoundException(
                    ErrorMessages.NOT_FOUND_BY_EID.getMessage());
        List<UserImageViewModel> likes = new ArrayList<>();
        UserEntity userEntity = optUserEntity.get();
        System.out.println(userEntity.getEmail());
        return ObjectMapper.mapByStrategy(userEntity, new UserEntityToUserDetailsResponseModel());

    }

    public List<UserDetailsResponseModel> getUserDetailsByUserName(String userName) {
        return userRepository.findByUsername(userName)
                .stream()
                .map(userEntity -> ObjectMapper.mapByStrategy(userEntity, new UserEntityToUserDetailsResponseModel()))
                .collect(Collectors.toList());
    }

    public UserDetailsResponseModel getUserDetailsByUID(String userId, HttpServletRequest request) {
        logger.info("Attempting [ getUserDetailsByUID.. ]");
        Optional<UserEntity> optUserEntity = userDAO.findUserEntityByUserId(userId);
        if (optUserEntity.isEmpty())
            throw new EntityNotFoundException(
                    ErrorMessages.NOT_FOUND_BY_EID.getMessage());
        List<UserImageViewModel> likes = new ArrayList<>();
        UserEntity userEntity = optUserEntity.get();
        UserDetailsResponseModel responseModel =
                ObjectMapper.mapByStrategy(userEntity, new UserEntityToUserDetailsResponseModel());


        List<UserImageViewModel> userImages = new ArrayList<>();
        if (request.getSession().getId() != null) {
            Optional<SessionEntity> bySessionIdOpt = sessionDAO.findUserBySessionID(request.getSession().getId());
            // check if user is owner and show private and public images
            if (bySessionIdOpt.isPresent() && bySessionIdOpt.get().getPrincipal() != null &&
                    bySessionIdOpt.get().getPrincipal().equals(userEntity.getEmail())) {
                logger.info("User is principal..Preparing user images");
                userImages = ObjectMapper.mapByStrategy(userEntity, new UserEntityToImageRespModels());
                likes = ObjectMapper.mapByStrategy(userEntity.getLikes(), new ImageEntitiesToImageRespModels());
            } else {
                logger.info("User is not principal..Preparing only public user images");
                // don t show public images
                List<ImageEntity> publicImages = userEntity.getImageEntities()
                        .stream()
                        .filter(ImageEntity::getIsPublic)
                        .collect(Collectors.toList());
                List<ImageEntity> publicLikes = userEntity.getLikes()
                        .stream()
                        .filter(ImageEntity::getIsPublic)
                        .collect(Collectors.toList());
                userImages =
                        ObjectMapper.mapByStrategy(publicImages, new ImageEntitiesToImageRespModels());
                likes = ObjectMapper.mapByStrategy(publicLikes,
                        new ImageEntitiesToImageRespModels());
            }
        }


        responseModel.setImages(userImages);
        responseModel.setLikes(likes);
        return responseModel;
    }

    public List<UserDetailsResponseModel> findAllByEmail(String email) {
        logger.info("Attempting [ findAllByEmail.. ]");
        Optional<List<UserEntity>> allInEmail = userDAO.getAllInEmail(email);
        if (allInEmail.isEmpty())
            throw new EntityNotFoundException(ErrorMessages.NOT_FOUND_BY_USERNAME.getMessage());
        List<UserEntity> userEntities = allInEmail.get();
        return userEntities.stream()
                .map(userEntity ->
                        new UserDetailsResponseModel(
                                userEntity.getFirstName(),
                                userEntity.getLastName(),
                                userEntity.getUserId(),
                                userEntity.getEmail()))
                .collect(Collectors.toList());
    }

    public UserDetailsResponseModel findUserByEmail(String email) {
        logger.info("Attempting [ findUserByEmail.. ]");
        Optional<UserEntity> byEmailOpt = userDAO.findByEmail(email);
        if (byEmailOpt.isEmpty()) throw new EntityNotFoundException(
                ErrorMessages.NOT_FOUND_BY_USERNAME.getMessage());
        UserEntity userEntity = byEmailOpt.get();
        return ObjectMapper.mapByStrategy(userEntity,
                new UserEntityToUserDetailsResponseModel());
    }

}
