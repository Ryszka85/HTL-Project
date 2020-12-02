package com.ryszka.imageRestApi.viewModels.response;

import java.io.Serializable;

public class ErrorResponse implements Serializable {
    private static final long serialVersionUID = 1237395762924583440L;
    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
