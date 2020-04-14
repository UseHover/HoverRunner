package com.usehover.testerv2.ui.parsers;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.usehover.testerv2.R;

public class ParsersActivity extends AppCompatActivity {
    static String parserId;
    public static String PARSER_EXTRA = "parser_extra";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            parserId = getIntent().getExtras().getString(PARSER_EXTRA);
        }catch (Exception e) {}

        setContentView(R.layout.parsers_activity);
    }
}
