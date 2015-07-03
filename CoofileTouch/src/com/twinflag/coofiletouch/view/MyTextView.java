package com.twinflag.coofiletouch.view;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class MyTextView extends SurfaceView implements Runnable {

	public String content;
	public int fontSize;
	public int fontColor;
	public int bgColor;
	private int startY;
	private int contentHeight;
	private String[] autoSplit;
	private SurfaceHolder holder;
	private Paint mPaint = null;
	private int mViewWidth = 0;
	private int mViewHeight = 0;
	private boolean mDrawOnce = false;
	private boolean mNeedRecycleDraw = true;

	public MyTextView(Context context) {
		super(context);
		init();
	}

	private void init() {
		setBackgroundColor(Color.TRANSPARENT);
		// setZOrderOnTop(true);
		holder = getHolder();// 滚动字幕的显示容器
		holder.setFormat(PixelFormat.TRANSPARENT);// 窗口支持透明度
		holder.addCallback(new Callback() {

			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
			}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				mViewHeight = getHeight();
				mViewWidth = getWidth();
				autoSplit = autoSplit();
				if (mViewHeight < contentHeight) {
					startY = mViewHeight;
				} else {
					startY = 5 + fontSize;
					mDrawOnce = true;
				}
				mNeedRecycleDraw = true;
				new Thread(MyTextView.this).start();
			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

			}

		});
	}

	@Override
	public void run() {
		while (mNeedRecycleDraw) {
			synchronized (holder) {
				if (holder == null || TextUtils.isEmpty(content)) {
					return;
				}
				Canvas canvas = holder.lockCanvas();
				if (canvas == null) {
					return;
				}
				// canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR);// 清屏
				Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
				paint.setColor(bgColor);
				canvas.drawRect(0, 0, mViewWidth, mViewHeight, paint);
				paint.setTextSize(fontSize);
				paint.setColor(fontColor);
				int y = 0;
				for (int i = 0; i < autoSplit.length; i++) {
					y = startY + fontSize * i;
					if (y > 0 && y < mViewHeight + fontSize) {
						canvas.drawText(autoSplit[i], 0, y, paint);
					}
				}
				holder.unlockCanvasAndPost(canvas);
				if (startY < -contentHeight) {
					startY = mViewHeight + fontSize;
				} else {
					startY -= 2;
				}
			}

			if (mDrawOnce) {
				mNeedRecycleDraw = false;
			}
			SystemClock.sleep(20);
		}
	}

	private String[] autoSplit() {
		int lineCount = 0;
		float widths[];
		int charactorWidth = 0;
		int lineCharsWidth = 0;
		int contentLen = 0;
		int areaWidth = 0;
		int lineStartPos = 0;
		char charactor = 0;
		String strCharactor = null;
		ArrayList<String> contentList = new ArrayList<String>();
		if (content == null || content.length() == 0) {
			return null;
		}
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setTextSize(fontSize);
		areaWidth = mViewWidth; // getWidth();
		contentLen = content.length();
		widths = new float[1];
		for (int index = 0; index < contentLen; ++index) {
			charactor = content.charAt(index);
			strCharactor = String.valueOf(charactor);
			mPaint.getTextWidths(strCharactor, widths);
			if (charactor == '\n') {
				lineCount++;
				lineCharsWidth = 0;
				contentList.add(content.substring(lineStartPos, index));
				lineStartPos = index + 1;
			} else {
				charactorWidth = (int) Math.ceil(widths[0]);
				lineCharsWidth += charactorWidth;
				if (areaWidth < lineCharsWidth) {
					lineCount++;
					lineCharsWidth = 0;
					contentList.add(content.substring(lineStartPos, index));
					lineStartPos = index;
					index--;
				} else {
					if (index == contentLen - 1) {
						lineCount++;
						contentList.add(content.substring(lineStartPos, contentLen));
					}
				}
			}
		}
		contentHeight = lineCount * fontSize + 2;
		String[] lines = new String[lineCount];
		for (int i = 0; i < lineCount; i++) {
			lines[i] = contentList.get(i);
		}
		return lines;
	}

}
