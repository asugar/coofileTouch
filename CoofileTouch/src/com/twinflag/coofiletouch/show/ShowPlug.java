package com.twinflag.coofiletouch.show;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.twinflag.coofiletouch.entity.PlugBean;
import com.twinflag.coofiletouch.showUtil.DestroyUtil;
import com.twinflag.coofiletouch.showUtil.LoadUtil;

public class ShowPlug extends ShowBase {

	private PlugBean mPlug;

	public ShowPlug(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ShowPlug(Context context, PlugBean _Plug) {
		super(context);
		this.mPlug = _Plug;
	}

	@Override
	public void startShow() {
		LoadUtil.loadPlug(mPlug, getContext());
		addView(mPlug.getView());
	}

	@Override
	public void stopShow() {
		View view = mPlug.getView();
		if (view != null) {
			view.setEnabled(false);
		}

	}

	@Override
	public void destroyShow() {
		// this.removeAllViews();
		View view = mPlug.getView();
		mPlug.setView(null);
		if (view != null) {
			this.removeView(view);
			DestroyUtil.destroyPlug(view);
			view = null;
		}
	}
}
