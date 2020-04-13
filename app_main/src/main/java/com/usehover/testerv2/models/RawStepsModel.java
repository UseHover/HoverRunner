package com.usehover.testerv2.models;

public class RawStepsModel {
     private String value, description;
     private boolean is_param;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isIs_param() {
        return is_param;
    }

    public void setIs_param(boolean is_param) {
        this.is_param = is_param;
    }
}
