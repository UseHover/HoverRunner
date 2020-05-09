package com.hover.runner.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.hover.runner.enums.StatusEnums;

import org.json.JSONArray;

public class ActionsModel implements Parcelable{

    private String actionId, actionTitle, rootCode, jsonArrayToString="", country, network_name;
    private JSONArray steps;
    private StatusEnums actionEnum;

    public ActionsModel(String actionId, String actionTitle, String rootCode, JSONArray steps, StatusEnums actionEnum) {
        this.actionId = actionId;
        this.actionTitle = actionTitle;
        this.rootCode = rootCode;
        this.steps = steps;
        this.actionEnum = actionEnum;
    }


    private ActionsModel(Parcel in) {
        actionId = in.readString();
        actionTitle = in.readString();
        rootCode = in.readString();
        jsonArrayToString = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(actionId);
        dest.writeString(actionTitle);
        dest.writeString(rootCode);
        dest.writeString(jsonArrayToString);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ActionsModel> CREATOR = new Creator<ActionsModel>() {
        @Override
        public ActionsModel createFromParcel(Parcel in) {
            return new ActionsModel(in);
        }

        @Override
        public ActionsModel[] newArray(int size) {
            return new ActionsModel[size];
        }
    };

    public String getRootCode() {
        return rootCode;
    }

    public JSONArray getSteps() {
        return steps;
    }

    public String getActionId() {
        return actionId;
    }

    public String getActionTitle() {
        return actionTitle;
    }

    public StatusEnums getActionEnum() {
        return actionEnum;
    }

    public void setJsonArrayToString(String jsonArrayToString) {
        this.jsonArrayToString = jsonArrayToString;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public String getNetwork_name() {
        return network_name;
    }

    public void setNetwork_name(String network_name) {
        this.network_name = network_name;
    }

    public String getJsonArrayToString() {
        return jsonArrayToString;
    }
}
