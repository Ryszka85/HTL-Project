package com.ryszka.imageRestApi.errorHandling;

public enum ImageDaoErrorMessages {
    SAVING_ERROR("Failed to save image-entity to db");

    private String message;

    ImageDaoErrorMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
