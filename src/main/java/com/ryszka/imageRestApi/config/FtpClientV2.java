package com.ryszka.imageRestApi.config;

import com.ryszka.imageRestApi.errorHandling.ErrorMessages;
import com.ryszka.imageRestApi.errorHandling.EntityPersistenceException;
import com.ryszka.imageRestApi.security.AppConfigProperties;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.function.Function;

public class FtpClientV2 {
    private final Logger logger = LoggerFactory.getLogger(FtpClientV2.class);

    public boolean ftpInstance(Function<FTPClient, Boolean> action) {
        FTPClient client = new FTPClient();
        try {
            logger.info("Attempting connection to FTP-Server...");
            configureFtpClient(client);
            logger.info("Successfully connected to ftp-client-server.");
            return action.apply(client);
        } catch (Exception e) {
            logger.error(ErrorMessages.SAVE_TO_FTP_ERROR + "{}", e.getMessage());
            throw new EntityPersistenceException(ErrorMessages.SAVE_TO_FTP_ERROR.getMessage());
        } finally {
            try {
                client.disconnect();
                logger.info("Successfully disconnected from ftp-client-server.");
            } catch (IOException e) {
                logger.error("Error while disconnecting from ftp server {}", e.getMessage());
            }

        }
    }

    private void configureFtpClient(FTPClient client) throws IOException {
        client.connect(AppConfigProperties.HOST);
        client.login(AppConfigProperties.USER, AppConfigProperties.PASSWORD);
        client.enterLocalPassiveMode();
        client.setFileType(FTP.BINARY_FILE_TYPE);
    }
}
