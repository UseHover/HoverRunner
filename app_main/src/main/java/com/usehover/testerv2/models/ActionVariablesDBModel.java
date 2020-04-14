package com.usehover.testerv2.models;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Map;

public class ActionVariablesDBModel {
    private Map<String, String> varMap;

    public ActionVariablesDBModel(Map<String, String> label) {
        this.varMap = label;
    }

    public Map<String, String> getVarMap() {
        return varMap;
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
