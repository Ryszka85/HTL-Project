package com.ryszka.imageRestApi.errorHandling;

import org.aspectj.internal.lang.annotation.ajcDeclareEoW;

public enum ErrorMessages {
    IMAGES_NOT_FOUND_BY_TAG("Unable to find images by given tag."),
    IMAGES_NOT_FOUND_BY_ID("Unable to find images by given id."),
    NOT_FOUND_BY_EID("Unable to find entity by given entityId :"),
    NOT_FOUND_BY_USERNAME("Unable to find entity by given user name :"),
    NOT_FOUND_BY_SESSIONID("Unable to find entity by given sessionId :"),
    LOGIN_FAIL("Failed to login user!"),
    PRIVATE_CONTENT("You are not allowed to view this item."),
    ILLEGAL_IMAGE_SIZE("Image mus have at least 2,07 MP.."),
    LOGIN_ERROR_MESSAGE("Unable to login. Username or password was invalid."),
    SCALING_ERROR("Scaling image failed!"),
    USER_ALREADY_EXISTS("User already exists."),
    USER_SIGNUP_FAILED("Failed to create new user!"),
    SAVE_TO_DB_ERROR("Failed to save entity to db."),
    CONNECTION_ERROR("Failed to establish connection to resource."),
    SAVE_TO_FTP_ERROR("Failed to save entity to FTP"),
    ROLLED_BACK_IN_TX("Rolled back because error occurred."),
    INVALID_ARGUMENTS("Provided arguments are not valid."),
    ADDRESS_NOT_FOUND("Address could not be found!"),
    ACCOUNT_NOT_ACTIVE("Account is not activated!"),
    EMAIL_ALREADY_SENT("Verification email was already send. Please check your inbox"),
    ENTITY_ACCESS_NOT_ALLOWED("Request to access the entity was refused due to privacy reasons!");

    private final String message;

    ErrorMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
