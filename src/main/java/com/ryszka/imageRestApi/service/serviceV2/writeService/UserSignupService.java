package com.ryszka.imageRestApi.service.serviceV2.writeService;

import com.ryszka.imageRestApi.dao.UserDAO;
import com.ryszka.imageRestApi.dao.ZipAndRegionDAO;
import com.ryszka.imageRestApi.errorHandling.EntityPersistenceException;
import com.ryszka.imageRestApi.errorHandling.ErrorMessages;
import com.ryszka.imageRestApi.errorHandling.UserRegistrationFailedException;
import com.ryszka.imageRestApi.persistenceEntities.AccountVerificationTokenEntity;
import com.ryszka.imageRestApi.persistenceEntities.UserEntity;
import com.ryszka.imageRestApi.repository.AccountVerificationRepository;
import com.ryszka.imageRestApi.security.AppConfigProperties;
import com.ryszka.imageRestApi.service.serviceV2.EmailService;
import com.ryszka.imageRestApi.standardMesages.StandardMessages;
import com.ryszka.imageRestApi.util.EmailSender;
import com.ryszka.imageRestApi.util.mapper.ObjectMapper;
import com.ryszka.imageRestApi.util.mapper.mapStrategies.UserDTOToUserEntity;
import com.ryszka.imageRestApi.viewModels.response.SignedUpUserDetailsResponse;
import com.ryszka.imageRestApi.service.dto.UserDTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/*import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;*/

import javax.mail.MessagingException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UserSignupService {
    private final UserDAO userDAO;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ZipAndRegionDAO zipAndRegionDAO;
    private final Logger logger = LoggerFactory.getLogger(UserSignupService.class);
    private EmailService emailService;
    private EmailSender emailSender;
    private AccountVerificationRepository verificationRepository;

    public UserSignupService(UserDAO userDAO,
                             BCryptPasswordEncoder passwordEncoder,
                             ZipAndRegionDAO zipAndRegionDAO,
                             EmailService emailService,
                             EmailSender emailSender,
                             AccountVerificationRepository verificationRepository) {
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
        this.zipAndRegionDAO = zipAndRegionDAO;
        this.emailService = emailService;
        this.emailSender = emailSender;
        this.verificationRepository = verificationRepository;
    }

    /*public UserSignupService(UserDAO userDAO,
                             BCryptPasswordEncoder passwordEncoder,
                             ZipAndRegionDAO zipAndRegionDAO) {
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
        this.zipAndRegionDAO = zipAndRegionDAO;
    }*/

    public SignedUpUserDetailsResponse createNewUser(UserDTO userDTO) throws URISyntaxException, MessagingException {
        logger.info("Attempting to create new user {}", userDTO);
        Optional<UserEntity> byEmailOpt = this.userDAO.findByEmail(userDTO.getEmail());

        if (byEmailOpt.isPresent()) throw new UserRegistrationFailedException(ErrorMessages.USER_SIGNUP_FAILED.getMessage() +
                System.lineSeparator() + ErrorMessages.USER_ALREADY_EXISTS.getMessage());

        UserEntity newUser = ObjectMapper.mapByStrategy(userDTO, new UserDTOToUserEntity(passwordEncoder));

        newUser.setLoginType("STANDARD");


        String token = Jwts.builder()
                .setSubject(userDTO.getEmail())
                .setExpiration(new Date(System.currentTimeMillis() + 300000))
                .signWith(SignatureAlgorithm.HS512, AppConfigProperties.JWT_SECRET_SIGNUP)
                .compact();
        AccountVerificationTokenEntity accountVerificationTokenEntity = new AccountVerificationTokenEntity();
        accountVerificationTokenEntity.setTokenId(UUID.randomUUID().toString());
        accountVerificationTokenEntity.setToken(token);
        accountVerificationTokenEntity.setUserEntity(newUser);
        verificationRepository.save(accountVerificationTokenEntity);
        newUser.setAccountVerificationToken(accountVerificationTokenEntity);
        emailSender.sendVerifyTokenEmail("Verify your email",
                "http://localhost:8880/image-app/verify/account/",
                StandardMessages.ACCOUNT_VERIFY_EMAIL_TEXT.getMsg(),
                "Activate-account",
                newUser.getEmail(),
                token);
        try {
            userDAO.saveUserEntity(newUser);
            return new SignedUpUserDetailsResponse(newUser.getUserId(), newUser.getEmail());
        } catch (Exception e) {
            throw new EntityPersistenceException(ErrorMessages.USER_SIGNUP_FAILED.getMessage());
        }
    }

}
