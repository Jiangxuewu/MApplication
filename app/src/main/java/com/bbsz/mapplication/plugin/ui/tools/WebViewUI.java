package com.bbsz.mapplication.plugin.ui.tools;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.bbsz.mapplication.R;

public class WebViewUI extends AppCompatActivity implements WebWidget.OnTitleListener {

    private WebWidget mWebWidget;

    private String testUrl = "http://m.iqiyi.com/v_19rrl2o4ag.html?msrc=6_55_97";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWebWidget = new WebWidget(this);
        setContentView(mWebWidget, new FrameLayout.LayoutParams(-1, -1));

        testUrl = getIntent().getStringExtra("url");
        if (TextUtils.isEmpty(testUrl)) {
            finish();
            return;
        }
        mWebWidget.loadUrl(testUrl);

        mWebWidget.setUpdateTitleListener(this);

        android.support.v7.app.ActionBar bar = getSupportActionBar();
        if (null != bar) {
            bar.setHomeButtonEnabled(true);
            bar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mWebWidget.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onBackPressed() {
        if (mWebWidget.canBack()) {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.webview_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateTitle(String title) {
        android.support.v7.app.ActionBar bar = getSupportActionBar();
        if (null != bar) {
            bar.setTitle(title);
        }
    }
}
