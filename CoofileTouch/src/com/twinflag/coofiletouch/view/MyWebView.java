package com.twinflag.coofiletouch.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MyWebView extends WebView {

	public MyWebView(Context context) {
		super(context);
		long time = System.currentTimeMillis();
		Log.i("yi", "before init: " + time);
		init();
		Log.i("yi", "after init: " + (System.currentTimeMillis() - time));
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void init() {
		getSettings().setJavaScriptEnabled(true);
		getSettings().setPluginState(PluginState.ON);
		setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		return true;
	}

}
