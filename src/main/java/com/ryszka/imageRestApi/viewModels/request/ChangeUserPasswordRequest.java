package com.ryszka.imageRestApi.viewModels.request;

public class ChangeUserPasswordRequest {
    private String userId, newPassword, oldPassword;

    public ChangeUserPasswordRequest() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    @Override
    public String toString() {
        return "ChangeUserPasswordRequest{" +
                "userId='" + userId + '\'' +
                ", newPassword='" + newPassword + '\'' +
                ", oldPassword='" + oldPassword + '\'' +
                '}';
    }
}
