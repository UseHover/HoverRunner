package com.usehover.testerv2.ui.action_details;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.usehover.testerv2.R;
import com.usehover.testerv2.api.Apis;
import com.usehover.testerv2.enums.StatusEnums;

public class ActionDetailsActivity extends AppCompatActivity {
    static public String actionId;
    static public String actionTitle;
    static public StatusEnums statusEnums;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.action_details);
        actionId = getIntent().getExtras().getString(Apis.ACTION_ID);
        actionTitle = getIntent().getExtras().getString(Apis.ACTION_TITLE);
        statusEnums = (StatusEnums) getIntent().getExtras().get(Apis.ACTION_STATUS);
    }

}
