package com.bbsz.mapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bbsz.mapplication.plugin.ui.tools.WebViewUI;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent i = new Intent(this, WebViewUI.class);
        i.putExtra("url", "http://www.bb-sz.com");
        startActivity(i);
    }
}
