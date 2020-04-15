package com.usehover.testerv2.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.usehover.testerv2.models.ActionVariablesDBModel;
import com.usehover.testerv2.models.RawStepsModel;
import com.usehover.testerv2.models.StreamlinedStepsModel;
import com.usehover.testerv2.ui.action_details.ActionDetailsActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {

    private static final String SHARED_PREFS = "_testerV2";


    private static SharedPreferences getSharedPrefs(Context context) {
        return context.getSharedPreferences(getPackage(context) + SHARED_PREFS, Context.MODE_PRIVATE);
    }

    public static void saveString(String key, String value, Context c) {
        SharedPreferences.Editor editor = Utils.getSharedPrefs(c).edit();
        editor.putString(key, value);
        editor.apply();
    }
    public static void saveInt(String key, int value, Context c) {
        SharedPreferences.Editor editor = Utils.getSharedPrefs(c).edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static String getStringFromSharedPref(Context c, String key) {
        return getSharedPrefs(c).getString(key, "");
    }

    public static int getIntFromSharedPref(Context c, String key) {
        return getSharedPrefs(c).getInt(key, 0);
    }

    private static String getPackage(Context c) {
        try {
            return c.getApplicationContext().getPackageName();
        } catch (NullPointerException e) {
            return "fail";
        }
    }

    public static StreamlinedStepsModel getStreamlinedStepsStepsFromRaw(@NonNull String rootCode,@NonNull  JSONArray jsonArray) {
        Gson gson = new Gson();
        RawStepsModel[] rawStepsModel = gson.fromJson(String.valueOf(jsonArray), RawStepsModel[].class);

        StringBuilder stepSuffix = new StringBuilder();
        ArrayList<String> stepsVariableLabels = new ArrayList<>();
        ArrayList<String> stepsVariableDesc = new ArrayList<>();

        for(RawStepsModel model : rawStepsModel) {
            stepSuffix.append("*").append(model.getValue());
            if(model.isIs_param() || model.getValue().equals("pin")) {
                stepsVariableLabels.add(model.getValue());
                stepsVariableDesc.add(model.getDescription());
            }
        }
        //Taking substring of root code to remove the last #. E.g *737# to become *737
        String readableStep = rootCode.substring(0, rootCode.length()-1) + stepSuffix+"#";
        return new StreamlinedStepsModel(readableStep, stepsVariableLabels, stepsVariableDesc);
    }

    public static void saveActionVariable(Context c,  String label, String value) {
        ActionVariablesDBModel dbModel = ActionVariablesDBModel.create(Utils.getStringFromSharedPref(c, ActionDetailsActivity.actionId));
        Map<String, String> mapper;
        if(dbModel == null) mapper = new HashMap<>();
        else {
            if(dbModel.getVarMap() == null) mapper = new HashMap<>();
            else mapper = dbModel.getVarMap();
        }

        mapper.put(label, value);
        Utils.saveString(ActionDetailsActivity.actionId, new ActionVariablesDBModel(mapper).serialize(), c);
    }

    public static Map<String, String> getInitialVariableData(Context c, String actionId) {
        ActionVariablesDBModel model = ActionVariablesDBModel.create(Utils.getStringFromSharedPref(c, ActionDetailsActivity.actionId));
        if(model == null) return new HashMap<>();
        return model.getVarMap();
    }

    public static boolean validateEmail(String string) {
        if(string == null) return  false;
        return string.matches("(?:[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])");
    }
    public static boolean validatePassword (String string) {
        if(string == null) return  false;
        return string.length() < 40 && string.length() >4 && !string.contains(" ");
    }

    public static String[] convertNormalJSONArrayToStringArray(JSONArray arr) throws JSONException {
        String[] list = new String[arr.length()];
        for(int i = 0; i < arr.length(); i++){
            list[i] = arr.getString(i);
        }
        return list;
    }

}
