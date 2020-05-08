package com.usehover.runner.ui.parsers;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.usehover.runner.R;

public class ParsersActivity extends AppCompatActivity {
    static String parserId;
    public static String PARSER_EXTRA = "parser_extra";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getIntent().getExtras() !=null) parserId = getIntent().getExtras().getString(PARSER_EXTRA);

        setContentView(R.layout.parsers_activity);
    }
}
