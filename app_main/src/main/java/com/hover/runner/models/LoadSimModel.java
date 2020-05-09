package com.hover.runner.models;

public class LoadSimModel {
    private  String sim1Name, sim2Name;

    public LoadSimModel(String sim1Name, String sim2Name) {
        this.sim1Name = sim1Name;
        this.sim2Name = sim2Name;
    }

    public String getSim1Name() {
        return sim1Name;
    }

    public String getSim2Name() {
        return sim2Name;
    }
}
