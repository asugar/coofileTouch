package com.twinflag.coofiletouch.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.twinflag.coofiletouch.R;

public class MyErrorPage extends FrameLayout {

	public MyErrorPage(Context context) {
		super(context);
		init();
	}

	public MyErrorPage(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		View.inflate(getContext(), R.layout.error_page, this);
	}
}
