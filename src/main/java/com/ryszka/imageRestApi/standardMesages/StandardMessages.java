package com.ryszka.imageRestApi.standardMesages;

public enum StandardMessages {
    LOGIN_SUCCESS("Successfully logged in user"),
    LOGOUT_SUCCESS("Successfully logged out"),
    ACCOUNT_VERIFY_EMAIL_TEXT("In order to user our services you have to click on the provided link for account verification purposes."),
    PASSWORD_RESET_TEXT("In order to reset your password you have to click on the provided link for verification purposes.");


    private String msg;
    StandardMessages(String msg) {
        System.out.println("FUCK OFFF");
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
