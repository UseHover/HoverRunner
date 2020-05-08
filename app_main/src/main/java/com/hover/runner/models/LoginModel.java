package com.hover.runner.models;

import com.hover.runner.enums.HomeEnums;

public class LoginModel {
    private HomeEnums status;
    private String message;

    public LoginModel(HomeEnums status, String message) {
        this.status = status;
        this.message = message;
    }

    public HomeEnums getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
