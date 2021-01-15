package com.hover.runner.models;

public class TokenModel {
    private String auth_token, api_key;
    private int org_id;

    public String getAuth_token() {
        return auth_token;
    }

    public int getOrgId() {
        return org_id;
    }

    public String getApiKey() {
        return api_key;
    }
}
