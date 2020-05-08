package com.usehover.runner.models;

import com.google.gson.Gson;

import java.util.Map;

public class ActionVariablesDBModel {
    private Map<String, String> varMap;
    private boolean skipped;

    public ActionVariablesDBModel(Map<String, String> label, boolean skipped) {
        this.varMap = label;
        this.skipped = skipped;
    }

    public Map<String, String> getVarMap() {
        return varMap;
    }

    public boolean isSkipped() {
        return skipped;
    }

    public String serialize() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    static public ActionVariablesDBModel create(String serializedData) {
        Gson gson = new Gson();
        return gson.fromJson(serializedData, ActionVariablesDBModel.class);
    }
}
