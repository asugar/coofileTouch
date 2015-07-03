package com.twinflag.coofiletouch.show;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public abstract class ShowBase extends FrameLayout {

	public ShowBase(Context context) {
		super(context);
	}

	public ShowBase(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public abstract void startShow();

	public abstract void stopShow();

	public abstract void destroyShow();

}
