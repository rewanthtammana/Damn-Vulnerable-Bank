package com.app.damnvulnerablebank;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class CurrencyRates extends AppCompatActivity {
    String getToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currencyrates);
        Bundle extras = getIntent().getExtras();
        if(extras == null){
            WebView vulnerable =(WebView) findViewById(R.id.loads);
            WebSettings webSettings = vulnerable.getSettings();
            webSettings.setJavaScriptEnabled(true);
            vulnerable.setWebChromeClient(new WebChromeClient());
            WebViewClientImpl webViewClient = new WebViewClientImpl(this);
            vulnerable.setWebViewClient(webViewClient);
            vulnerable.loadUrl("https://www.xe.com/"); }

        else{
            getToken =getIntent().getData().getQueryParameter("url");
            WebView vulnerable =(WebView) findViewById(R.id.loads);
            WebSettings webSettings = vulnerable.getSettings();
            webSettings.setJavaScriptEnabled(true);
            vulnerable.setWebChromeClient(new WebChromeClient());
            WebViewClientImpl webViewClient = new WebViewClientImpl(this);
            vulnerable.setWebViewClient(webViewClient);
            vulnerable.loadUrl(getToken);}
    }
}



class WebViewClientImpl extends WebViewClient {

    private Activity activity = null;

    public WebViewClientImpl(Activity activity) {
        this.activity = activity;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView webView, String url) {
        return false;

    }

}