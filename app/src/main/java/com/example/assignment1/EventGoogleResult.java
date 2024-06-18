package com.example.assignment1;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class EventGoogleResult extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_search);

        WebView webView = findViewById(R.id.webView);

        String eventName = getIntent().getExtras().getString("eventName");

        String googlePageURL = "https://www.google.com/search?q=" + eventName;

        webView.setWebViewClient(new WebViewClient()); //to use WebView rather than default web browser
        webView.loadUrl(googlePageURL);

    }
}