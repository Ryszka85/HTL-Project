package com.ryszka.imageRestApi.service.serviceV2.writeService;

import com.ryszka.imageRestApi.config.FtpClientV2;
import com.ryszka.imageRestApi.errorHandling.BadConnectionException;
import com.ryszka.imageRestApi.errorHandling.ErrorMessages;
import com.ryszka.imageRestApi.persistenceEntities.FtpPersistenceEntity;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.function.Function;

public class FtpStorageService {
    private final Logger logger = LoggerFactory.getLogger(FtpStorageService.class);
    public boolean makeDirAndSaveFileToFileSystem(FtpPersistenceEntity entity) {
        FtpClientV2 client = new FtpClientV2();
        return client.ftpInstance(saveFileAndTryMkDir(entity));
    }

    private Function<FTPClient, Boolean> saveFileAndTryMkDir(FtpPersistenceEntity entity) {
        return ftpClient -> {
            if (ftpClient == null)
                throw new BadConnectionException(ErrorMessages.CONNECTION_ERROR.getMessage());
            try {
                tryMakingDirectory(entity.getPath().substring(0, entity.getPath().lastIndexOf("/")), ftpClient);
                this.logger.info("Attempting to store file with path {} to the file system", entity.getPath());
                return ftpClient.storeFile(entity.getPath(), entity.getContent());
            } catch (IOException e) {
                this.logger.error("Error occurred while tying to persist file with path {} to file system", entity.getPath());
            }
            return false;
        };
    }

    private boolean tryMakingDirectory(String path, FTPClient ftpClient) throws IOException {
        boolean status = ftpClient.makeDirectory(path);
        logger.info(status ?
                "Made new Directory [ " + path + " ]" :
                "Using Directory [ " + path + " ] to store file.");
        return status;
    }
}
