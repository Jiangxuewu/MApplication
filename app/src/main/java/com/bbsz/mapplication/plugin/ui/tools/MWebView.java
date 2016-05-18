package com.bbsz.mapplication.plugin.ui.tools;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Administrator on 2016/5/18.
 */
/*public*/ class MWebView extends WebView {
    private JsCallJava jsJavaInterface;

    public MWebView(Context context) {
        super(context);
        init();
    }

    public MWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        WebSettings settings = getSettings();
        // 不让左右滑动
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setUseWideViewPort(true);
        settings.setDefaultTextEncodingName("utf-8");
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setAllowFileAccess(true);
//		settings.setPluginsEnabled(true);
        settings.setLoadWithOverviewMode(true);
        //启用数据库
        settings.setDatabaseEnabled(true);
        String dir = getContext().getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        //启用地理定位
        settings.setGeolocationEnabled(true);
        //设置定位的数据库路径
        settings.setGeolocationDatabasePath(dir);

        //最重要的方法，一定要设置，这就是出不来的主要原因
        settings.setDomStorageEnabled(true);

        setBackgroundColor(0); // 设置背景色


        //JsInterface Method one
        jsJavaInterface = new JsCallJava(getContext());
        addJavascriptInterface(jsJavaInterface, "control");
        //JsBridge  Method two
//        setWebChromeClient(new MWebChromeClient());
        //JsRouter  Method three
//        setWebViewClient(new MWebViewClient());
    }

    @Override
    public void setWebChromeClient(WebChromeClient client) {
        super.setWebChromeClient(client);
    }

    @Override
    public void setWebViewClient(WebViewClient client) {
        super.setWebViewClient(client);
    }

    class MWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }
    }

    class MWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }
    }
}
