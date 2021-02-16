package com.ryszka.imageRestApi.service.serviceV2.writeService;

import com.ryszka.imageRestApi.repository.GoogleCloudRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class GoogleUploadTask implements Runnable {
    protected final GoogleCloudRepository cloudRepository;
    protected final Logger logger;

    public GoogleUploadTask(GoogleCloudRepository cloudRepository,
                            Class loggerClass) {
        this.cloudRepository = cloudRepository;
        this.logger = LoggerFactory
                .getLogger(loggerClass);
    }
}


