package com.twinflag.coofiletouch.view;

import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.widget.TextView;

public class ScrollTextView extends TextView {

	public ScrollTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setEllipsize(TruncateAt.MARQUEE);
		setFocusable(true);
		setFocusableInTouchMode(true);
	}

	@Override
	public boolean isFocused() {
		return true;
	}

}
