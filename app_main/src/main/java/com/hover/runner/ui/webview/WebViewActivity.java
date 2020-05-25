package com.hover.runner.ui.webview;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hover.runner.R;

import java.util.Objects;

public class WebViewActivity extends AppCompatActivity {
    public static final String TITLE = "title";
    public static final String URL = "url";

    @SuppressLint("SetJavaScriptEnabled")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        String title = Objects.requireNonNull(getIntent().getExtras()).getString(TITLE);
        String url = getIntent().getExtras().getString(URL);

        setContentView(R.layout.webview);
        new Handler().postDelayed(() -> findViewById(R.id.fakeLoading).setVisibility(View.GONE), 2000);

        TextView titleView = findViewById(R.id.webviewTitle);
        titleView.setText(title);
        titleView.setOnClickListener(v-> finish());

        WebView webView = findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }
}