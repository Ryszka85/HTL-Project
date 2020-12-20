package com.ryszka.imageRestApi.viewModels.response;

public class ChangePasswordResponse {
    private boolean status;
    private String errorMsg;

    public ChangePasswordResponse() {
    }

    public ChangePasswordResponse(boolean status, String errorMsg) {
        this.status = status;
        this.errorMsg = errorMsg;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
