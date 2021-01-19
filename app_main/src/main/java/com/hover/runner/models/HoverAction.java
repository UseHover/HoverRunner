package com.hover.runner.models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class HoverAction {
    private final String TAG = "HoverAction";

    public String name;
    public String root_code;
    public String sim_hni;

    public HoverAction(String n, String r, String hni) {
        name = n;
        root_code = r;
        sim_hni = hni;
    }

    public JSONObject toJson() {
        JSONObject action = new JSONObject();
        try {
            JSONObject j = new JSONObject();
            j.put("name", name);
            j.put("root_code", root_code);
            j.put("transport_type", "ussd");
            j.put("hni", sim_hni);

            action.put("custom_action", j);
        } catch (JSONException je) { Log.e(TAG, "error creating action json", je); }

        return action;
    }
}
