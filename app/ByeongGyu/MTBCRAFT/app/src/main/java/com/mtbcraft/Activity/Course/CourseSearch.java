package com.mtbcraft.Activity.Course;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.capston.mtbcraft.R;

import org.w3c.dom.Text;

public class CourseSearch extends AppCompatActivity{
    WebView webview;
    private static final String ENTRY_URL = "http://100.92.32.8:8080/riding/Android_CourseSearch";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_search);

        webview = (WebView) findViewById(R.id.searchMap);
        webview.getSettings().setJavaScriptEnabled(true);//자바스크립트 허용
        webview.setWebChromeClient(new WebChromeClient());
        webview.getSettings().setLoadWithOverviewMode(true);

        webview.setWebViewClient(new WebViewClient());
        webview.loadUrl(ENTRY_URL);

    }
}
