package com.bbsz.mapplication.plugin.ui.tools;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.http.SslError;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.JsPromptResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bbsz.mapplication.permissions.PermissionUtil;

/**
 * Created by Administrator on 2016/5/18.
 * <p>加载WebView</p>
 */
/*public*/ class WebWidget extends FrameLayout {

    private static final String TAG = WebWidget.class.getSimpleName();
    private MWebView webView;
    private WebViewProgress progress;
    private View errorView;

    private boolean isLoadFailed = false;
    private PermissionUtil mPermissionUtil;


    private WebViewClient client = new WebViewClient() {

        //JsRouter  Method three
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d(TAG, "shouldOverrideUrlLoading, url = " + url);
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.d(TAG, "onPageStarted, url = " + url);
            isLoadFailed = false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.d(TAG, "onPageFinished, url = " + url);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            Log.w(TAG, "onReceivedError");
            mayByLoadFailed();
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
            Log.w(TAG, "onReceivedHttpError, errorResponse = " + errorResponse.toString());
            mayByLoadFailed();
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            Log.w(TAG, "onReceivedSslError, error = " + error.toString());
            mayByLoadFailed();
        }
    };


    private WebChromeClient chrome = new WebChromeClient() {
        //JsBridge  Method two
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            Log.d(TAG, "onJsPrompt, url = " + url);
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            Log.d(TAG, "onProgressChanged(), newProgress = " + newProgress);
            progress.updateProgress(newProgress / 100.f);
            if (newProgress >= 40 && !isLoadFailed && webView.getVisibility() != View.VISIBLE) {
                webView.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            Log.d(TAG, "onReceivedTitle(), title = " + title);
            if (null != listener) {
                if (isLoadFailed) {
                    title = "";
                }
                listener.updateTitle(title);
            }
        }

        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);
            Log.d(TAG, "onReceivedIcon()");
        }

        @Override
        public void onReceivedTouchIconUrl(WebView view, String url, boolean recomposed) {
            super.onReceivedTouchIconUrl(view, url, recomposed);
            Log.d(TAG, "onReceivedTouchIconUrl(), url = " + url + ", recomposed = " + recomposed);

        }

        @Override
        public void onGeolocationPermissionsHidePrompt() {
            super.onGeolocationPermissionsHidePrompt();
            Log.d(TAG, "onGeolocationPermissionsHidePrompt");
        }

        @Override
        public void onGeolocationPermissionsShowPrompt(final String origin,
                                                       final GeolocationPermissions.Callback callback) {
            Log.d(TAG, "onGeolocationPermissionsShowPrompt(), origin = " + origin);

            if (mContext instanceof Activity) {
                mPermissionUtil.requestPermission((Activity) mContext, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, new PermissionUtil.OnCheckPermissionCallback() {
                    @Override
                    public void requestPermissionSuccess() {
                        Log.d(TAG, "onGeolocationPermissionsShowPrompt(), per  success");
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("Allow to access location information?");
                        DialogInterface.OnClickListener dialogButtonOnClickListener = new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int clickedButton) {
                                if (DialogInterface.BUTTON_POSITIVE == clickedButton) {
                                    Log.d(TAG, "onGeolocationPermissionsShowPrompt(), per dialog allow");
                                    callback.invoke(origin, true, true);
                                } else if (DialogInterface.BUTTON_NEGATIVE == clickedButton) {
                                    callback.invoke(origin, false, false);
                                    Log.d(TAG, "onGeolocationPermissionsShowPrompt(), per dialog deny");
                                }
                            }
                        };
                        builder.setPositiveButton("Allow", dialogButtonOnClickListener);
                        builder.setNegativeButton("Deny", dialogButtonOnClickListener);
                        builder.show();
                    }

                    @Override
                    public void requestPermissionFailed() {
                        Log.d(TAG, "onGeolocationPermissionsShowPrompt(), per  failed");
                        callback.invoke(origin, false, false);
                    }
                });
            }

            super.onGeolocationPermissionsShowPrompt(origin, callback);
        }
    };
    private OnClickListener errorViewListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (null != webView)
                webView.reload();
            v.setVisibility(View.GONE);
        }
    };
    private Context mContext;
    private OnTitleListener listener;

    public WebWidget(Context context) {
        super(context);
        init(context);
    }

    public WebWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WebWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        mContext = context;
        mPermissionUtil = new PermissionUtil();

        webView = new MWebView(context);
        progress = new WebViewProgress(context);
        errorView = new TextView(context);
        ((TextView) errorView).setText("轻触屏幕重新加载");
        ((TextView) errorView).setGravity(Gravity.CENTER);
        errorView.setBackgroundColor(Color.WHITE);
        errorView.setVisibility(View.GONE);
        addView(webView, new FrameLayout.LayoutParams(-1, -1));
        addView(progress, new FrameLayout.LayoutParams(-1, dp2px(context, 2)));
        addView(errorView, new FrameLayout.LayoutParams(-1, -1));
    }

    private int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    private void mayByLoadFailed() {
        if (progress.curProgress() >= 0.5f) {//如果加载了50%则显示出来
            return;
        }
        if (null != errorView) {
            errorView.setVisibility(View.VISIBLE);
            errorView.setOnClickListener(errorViewListener);
        }
        webView.setVisibility(View.GONE);
        isLoadFailed = true;
    }

    public void loadUrl(String url) {
        webView.loadUrl(url);
        webView.setWebViewClient(client);
        webView.setWebChromeClient(chrome);
        webView.setVisibility(View.GONE);
    }

    public boolean canBack() {
        if (webView.canGoBack()) {
            webView.goBack();
            return false;
        }
        return true;
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mPermissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void setUpdateTitleListener(OnTitleListener listener) {
        this.listener = listener;
    }

    public interface OnTitleListener {

        void updateTitle(String title);

    }
}
