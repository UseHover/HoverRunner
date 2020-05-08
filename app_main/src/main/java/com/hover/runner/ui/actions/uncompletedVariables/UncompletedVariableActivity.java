package com.hover.runner.ui.actions.uncompletedVariables;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hover.runner.R;
import com.hover.runner.models.ActionsModel;
import com.hover.runner.utils.UIHelper;

import java.util.List;

public class UncompletedVariableActivity extends AppCompatActivity {
    public static List<ActionsModel> uncompletedVariableActionList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getIntent().getExtras() == null) finish();
        else {
            uncompletedVariableActionList = getIntent().getExtras().getParcelableArrayList("data");
            setContentView(R.layout.uncompleted_variable_layout);
            UIHelper.changeStatusBarColor(this, getResources().getColor(R.color.colorRed));
        }
    }
}
