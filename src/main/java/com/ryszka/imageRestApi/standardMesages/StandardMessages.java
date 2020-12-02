package com.ryszka.imageRestApi.standardMesages;

public enum StandardMessages {
    LOGIN_SUCCESS("Successfully logged in user"),
    LOGOUT_SUCCESS("Successfully logged out");

    private String msg;
    StandardMessages(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
