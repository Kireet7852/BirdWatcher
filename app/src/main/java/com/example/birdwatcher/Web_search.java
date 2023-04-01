package com.example.birdwatcher;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class Web_search extends AppCompatActivity {

    private WebView webView;
    private ActionBar actionBar;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_search);

        webView = findViewById(R.id.web_View);
        actionBar = getSupportActionBar();

        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);

//        String query=" ";// Your search query
        Intent intent = getIntent();
//        intent.putExtra("clickedName", query);
        String query = getIntent().getStringExtra("querys");

        actionBar.setTitle(query);


        String url = "https://www.google.com/search?q=" + query;
        webView.loadUrl(url);
    }


    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}