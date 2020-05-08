package com.hover.runner.models;

import java.util.List;

public class StreamlinedStepsModel {
    private String fullUSSDCodeStep;
    private List<String> stepVariableLabel;
    private List<String> stepsVariableDesc;

    public StreamlinedStepsModel(String fullUSSDCodeStep, List<String> stepVariableLabel, List<String> stepsVariableDesc) {
        this.fullUSSDCodeStep = fullUSSDCodeStep;
        this.stepVariableLabel = stepVariableLabel;
        this.stepsVariableDesc = stepsVariableDesc;
    }

    public String getFullUSSDCodeStep() {
        return fullUSSDCodeStep;
    }

    public List<String> getStepVariableLabel() {
        return stepVariableLabel;
    }

    public List<String> getStepsVariableDesc() {
        return stepsVariableDesc;
    }
}
