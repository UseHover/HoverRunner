package com.usehover.testerv2.models;

import com.usehover.testerv2.enums.HomeEnums;

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
