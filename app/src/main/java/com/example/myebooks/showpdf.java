package com.example.myebooks;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.security.auth.callback.Callback;

public class showpdf extends AppCompatActivity {

    WebView showpdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showpdf);

        showpdf = findViewById(R.id.showpdf);

        String filename = getIntent().getStringExtra("filename");
        String fileurl = getIntent().getStringExtra("fileurl");

        ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle(filename);
        pd.setMessage("PDF opening...");

        showpdf.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pd.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pd.dismiss();
            }
        });

        String url = "";
        try {
            url = URLEncoder.encode(fileurl,"UTF-8");

        } catch (Exception ex) {

        }

        if(fileurl.contains(".pdf")){
            showpdf.getSettings().setSupportZoom(true);
            showpdf.getSettings().setJavaScriptEnabled(true);
            showpdf.getSettings().setPluginState(WebSettings.PluginState.ON);

            showpdf.loadUrl("http://docs.google.com/gview?embedded=true&url=" + url);
        }
        else{
            Toast.makeText(getApplicationContext(),"Selected file is not pdf",Toast.LENGTH_SHORT).show();
        }

    }
}