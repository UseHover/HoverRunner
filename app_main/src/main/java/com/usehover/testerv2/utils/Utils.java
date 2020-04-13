package com.usehover.testerv2.utils;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.usehover.testerv2.models.RawStepsModel;
import com.usehover.testerv2.models.StreamlinedStepsModel;

import org.json.JSONArray;

import java.util.ArrayList;

public class Utils {
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
}
