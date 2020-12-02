package com.ryszka.imageRestApi.viewModels.response;

import java.io.Serializable;

public class ResponseBodyModel<T> implements Serializable {
    private static final long serialVersionUID = 3610981812985475569L;
    private T payload;
    private String statusMessage;
    private int status;

    public ResponseBodyModel(String statusMessage, int status) {
        this.statusMessage = statusMessage;
        payload = null;
        this.status = status;
    }

    public ResponseBodyModel(T payload) {
        this.payload = payload;
        statusMessage = "";
        status = -1;
    }

    public ResponseBodyModel(T payload, String statusMessage, int status) {
        this.payload = payload;
        this.statusMessage = statusMessage;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public T getPayload() {
        return payload;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}
