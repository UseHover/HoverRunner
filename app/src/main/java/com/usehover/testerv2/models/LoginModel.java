package com.usehover.testerv2.models;

import com.usehover.testerv2.enums.HoverEnums;

public class LoginModel {
    private HoverEnums status;
    private String message;

    public LoginModel(HoverEnums status, String message) {
        this.status = status;
        this.message = message;
    }

    public HoverEnums getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
