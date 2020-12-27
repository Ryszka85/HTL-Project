package com.ryszka.imageRestApi.service.serviceV2.readService;

import com.ryszka.imageRestApi.dao.SessionDAO;
import com.ryszka.imageRestApi.dao.UserDAO;
import com.ryszka.imageRestApi.errorHandling.EntityNotFoundException;
import com.ryszka.imageRestApi.errorHandling.ErrorMessages;
import com.ryszka.imageRestApi.viewModels.response.UserDetailsResponseModel;
import com.ryszka.imageRestApi.persistenceEntities.SessionEntity;
import com.ryszka.imageRestApi.persistenceEntities.UserEntity;
import com.ryszka.imageRestApi.util.mapper.ObjectMapper;
import com.ryszka.imageRestApi.util.mapper.mapStrategies.UserEntityToUserDetailsResponseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@Service
public class UserAuthService implements UserDetailsService {
    private final UserDAO userDAO;
    private final SessionDAO sessionDAO;
    private final Logger logger =
            LoggerFactory.getLogger(UserAuthService.class);

    public UserAuthService(UserDAO userDAO, SessionDAO sessionDAO) {
        this.userDAO = userDAO;
        this.sessionDAO = sessionDAO;
    }

    public UserDetailsResponseModel getLoggedUserDetailsBySessionID(HttpServletRequest request) {
        logger.info("Attempting [ getLoggedUserDetailsBySessionID ]..");
        SessionEntity sessionEntity = this.sessionDAO
                .findUserBySessionID(request.getSession().getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        ErrorMessages.NOT_FOUND_BY_SESSIONID.getMessage()));
        UserEntity userEntity = userDAO.findByEmail(sessionEntity.getPrincipal())
                .orElseThrow(() -> new EntityNotFoundException(
                        ErrorMessages.NOT_FOUND_BY_USERNAME.getMessage()));
        UserDetailsResponseModel userDetailsResponseModel = ObjectMapper.mapByStrategy(userEntity,
                new UserEntityToUserDetailsResponseModel());
        userDetailsResponseModel.setThirdPartyLogin(userEntity.getLoginType().equals("GOOGLE"));
        return userDetailsResponseModel;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Attempting [ loadUserByUsername ]...");
        UserEntity userEntity = userDAO.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException(
                        ErrorMessages.NOT_FOUND_BY_USERNAME.getMessage()));
        if (userEntity == null)
            throw new UsernameNotFoundException("Could not find user to login..");
        return new User(userEntity.getEmail(),
                userEntity.getPassword(),
                userEntity.isAccountVerified(),
                true,
                true,
                true,
                new ArrayList<>());
    }
}
