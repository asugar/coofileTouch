package com.twinflag.coofiletouch.view;

import android.content.Context;
import android.widget.VideoView;

public class MyVideoView extends VideoView {

	public int errorCount;// 视频播放出错次数

	public MyVideoView(Context context) {
		super(context);
		// setZOrderOnTop(true);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
	}

}
