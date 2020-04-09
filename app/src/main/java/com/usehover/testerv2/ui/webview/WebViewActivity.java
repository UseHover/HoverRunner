package com.usehover.testerv2.ui.webview;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.usehover.testerv2.R;

public class WebViewActivity extends AppCompatActivity {

    private WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        String title = getIntent().getExtras().getString("title");
        String url = getIntent().getExtras().getString("url");

        setContentView(R.layout.webview);
        webView = (WebView) findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }
}