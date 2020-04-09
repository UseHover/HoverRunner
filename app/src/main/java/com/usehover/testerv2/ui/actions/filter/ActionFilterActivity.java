package com.usehover.testerv2.ui.actions.filter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ActionFilterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("idn", "enteredTextValue");
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        }, 2000);
    }
}
