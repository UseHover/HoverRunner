package com.usehover.testerv2.ui.actions.uncompletedVariables;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.usehover.testerv2.R;
import com.usehover.testerv2.models.ActionsModel;
import com.usehover.testerv2.utils.UIHelper;

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