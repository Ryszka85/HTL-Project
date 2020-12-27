package com.ryszka.imageRestApi.service.serviceV2.writeService;

import com.ryszka.imageRestApi.dao.UserDAO;
import com.ryszka.imageRestApi.dao.ZipAndRegionDAO;
import com.ryszka.imageRestApi.errorHandling.AddressNotFoundException;
import com.ryszka.imageRestApi.errorHandling.EntityPersistenceException;
import com.ryszka.imageRestApi.errorHandling.ErrorMessages;
import com.ryszka.imageRestApi.errorHandling.UserRegistrationFailedException;
import com.ryszka.imageRestApi.persistenceEntities.UserAddressEntity;
import com.ryszka.imageRestApi.persistenceEntities.UserEntity;
import com.ryszka.imageRestApi.security.AppConfigProperties;
import com.ryszka.imageRestApi.security.JWTVerifier;
import com.ryszka.imageRestApi.service.serviceV2.EmailService;
import com.ryszka.imageRestApi.util.mapper.ObjectMapper;
import com.ryszka.imageRestApi.util.mapper.mapStrategies.SetAddressToUserEntity;
import com.ryszka.imageRestApi.util.mapper.mapStrategies.UserDTOToUserEntity;
import com.ryszka.imageRestApi.viewModels.response.SignedUpUserDetailsResponse;
import com.ryszka.imageRestApi.persistenceEntities.ZipAndRegionEntity;
import com.ryszka.imageRestApi.service.dto.UserDTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/*import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;*/

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Optional;

@Service
@Transactional
public class UserSignupService {
    private final UserDAO userDAO;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ZipAndRegionDAO zipAndRegionDAO;
    private final Logger logger = LoggerFactory.getLogger(UserSignupService.class);
    private EmailService emailService;

    public UserSignupService(UserDAO userDAO, BCryptPasswordEncoder passwordEncoder, ZipAndRegionDAO zipAndRegionDAO, EmailService emailService) {
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
        this.zipAndRegionDAO = zipAndRegionDAO;
        this.emailService = emailService;
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
        /*UserAddressEntity userAddressEntity = ObjectMapper.mapByStrategy(userDTO, new SetAddressToUserEntity());
        newUser.setUserAddress(userAddressEntity);
        Optional<ZipAndRegionEntity> byZipCodeOpt = zipAndRegionDAO.getByZipCode(userDTO.getAddressDTO().getZipcode());

        if (byZipCodeOpt.isEmpty()) throw new AddressNotFoundException(
                ErrorMessages.ADDRESS_NOT_FOUND.getMessage());

        ZipAndRegionEntity zipAndRegionEntity = byZipCodeOpt.get();
        userAddressEntity.setZipAndRegionEntity(zipAndRegionEntity);
        newUser.setUserAddress(userAddressEntity);*/
        newUser.setLoginType("STANDARD");


        String token = Jwts.builder()
                .setSubject(userDTO.getEmail())
                .setExpiration(new Date(System.currentTimeMillis() + 300000))
                .signWith(SignatureAlgorithm.HS512, AppConfigProperties.JWT_SECRET_SIGNUP)
                .compact();
        newUser.setAccountVerificationToken(token);

        System.out.println(token);

        MimeMessage mimeMessage = emailService.getJavaMailSender().createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        helper.setFrom("adrian.ryszka@gmx.net");
        helper.setTo(newUser.getEmail());
        helper.setSubject("Verify your email");
        StringBuilder sb = new StringBuilder("");
        URI redirectUrl = new URI("http://localhost:8880/image-app/verify/account/" + token);
        helper.setText( sb.append("<body>")
                        .append("<h1>Welcome to SpecShots</h1>")
                        .append("<p>In order to user our services you have to click on the provided link for account verification purposes.</p>")
                        .append("<a href=\"")
                        .append(redirectUrl.toString())
                        .append("\">")
                        .append("Activate account")
                        .append("</a>")
                        .append("</body>")
                .toString(),
                true  );

        emailService.getJavaMailSender().send(mimeMessage);
        try {
            userDAO.saveUserEntity(newUser);
            return new SignedUpUserDetailsResponse(newUser.getUserId(), newUser.getEmail());
        } catch (Exception e) {
            throw new EntityPersistenceException(ErrorMessages.USER_SIGNUP_FAILED.getMessage());
        }
    }

}
