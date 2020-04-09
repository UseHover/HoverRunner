package com.usehover.testerv2.models;

public class LoginModel {
private int status;
private String message;

public LoginModel(int status, String message) {
	this.status = status;
	this.message = message;
}

public int getStatus() {
	return status;
}

public String getMessage() {
	return message;
}
}
