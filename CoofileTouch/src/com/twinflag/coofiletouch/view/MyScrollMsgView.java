package com.twinflag.coofiletouch.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.os.Handler;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import com.twinflag.coofiletouch.CoofileTouchApplication;
import com.twinflag.coofiletouch.entity.MessageBean;
import com.twinflag.coofiletouch.type.MsgType;

public class MyScrollMsgView extends SurfaceView {

	// 标记是否停止
	private Boolean isStop = false;
	private SurfaceHolder holder;
	// 记录当前滚动消息所处位置
	private MsgType msgType = null;
	// 字体宽度
	private float textWidth = 0f;
	// 字体高度
	private float textHeight = 0f;
	// 字体宽度+屏宽
	// private float viewWidth_plus_textLength = 0.0f;
	// 记录滚动消息x轴位置
	public float textX = 0.0f;
	// 记录滚动消息y轴位置
	private float textY = 0f;
	// 滚动消息内容
	private String text;
	// 滚动消息字体大小
	private float textSize = 0f;
	// 滚动字体的速度
	private int speed;
	private Paint paint;
	private Boolean isDraw = true;
	private Handler mHandler;

	public MyScrollMsgView(Context context) {
		super(context);
	}

	public MyScrollMsgView(Context context, Handler handler) {
		super(context);
		// 一次配置一直使用
		this.mHandler = handler;
		initConstant();
	}

	// 初始化常量，一次实例化终身使用，初始化完才能初始化变量
	private void initConstant() {
		paint = new Paint();
		paint.setAntiAlias(true);
		holder = getHolder();
		holder.setFormat(PixelFormat.TRANSPARENT);
		holder.addCallback(msgCallback);
	}

	// 初始化变量，每个消息都需要执行一边该方法
	public void initVariable(MessageBean currentMsg) {
		// 设置字体内容
		text = currentMsg.getContent();
		// 设置字体大小
		textSize = currentMsg.getSize();
		if (textSize <= 0) {
			textSize = 50;
		} else if (textSize > 100) {
			textSize = 100;
		}
		paint.setTextSize(textSize);

		textSize = currentMsg.getSize();
		// 设置字体颜色,由前景色和背景色之分
		paint.setColor(Color.parseColor(currentMsg.getFgcolor() == null ? "#000000" : currentMsg.getFgcolor()));
		// 设置字体类型setTypeface;
		// paint.setTypeface(currentMsg.getFont());
		// 设置字体滚动速度
		speed = currentMsg.getSpeed();
		if (speed <= 0) {
			speed = 10;
		} else if (speed > 50) {
			speed = 50;
			// 解决速度如果小于1的时候的问题
			// int speedTemp = (int) (speed * 0.2);
			// this.speed = speedTemp < 1 ? 1 : speedTemp;
		}
		// 设置字体位置
		msgType = currentMsg.getPosition();
		switch (msgType) {
			case 上边:
				textX = CoofileTouchApplication.getScreenWidth();
				textWidth = paint.measureText(text);
				textY = 100 - 5;
				break;
			case 下边:
				textX = CoofileTouchApplication.getScreenWidth();
				textWidth = paint.measureText(text);
				textY = 100 - 5;
				break;
			case 左边:
				textY = CoofileTouchApplication.getScreenHeight();
				textHeight = getFontHeight(textSize) * text.length();
				textX = 5;
				break;
			case 右边:
				textY = CoofileTouchApplication.getScreenHeight();
				textHeight = getFontHeight(textSize) * text.length();
				textX = (100 - getFontHeight(textSize)) > 0 ? (100 - getFontHeight(textSize)) / 2 : 0;
				break;
		}
	}

	public Runnable msgRun = new Runnable() {
		@Override
		public void run() {
			while (!isStop && holder != null) {
				synchronized (holder) {
					Canvas canvas = holder.lockCanvas();
					if (canvas == null) {
						return;
					}
					canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR);
					if (isDraw) {
						switch (msgType) {
							case 上边:
								canvas.drawText(text, textX, textY, paint);
								holder.unlockCanvasAndPost(canvas);
								if (textX < -textWidth) {
									textX = CoofileTouchApplication.getScreenWidth();
									mHandler.sendEmptyMessage(0x123);

								} else {
									textX -= speed;
								}
								break;
							case 下边:
								canvas.drawText(text, textX, textY, paint);
								holder.unlockCanvasAndPost(canvas);
								if (textX < -textWidth) {
									textX = CoofileTouchApplication.getScreenWidth();
									mHandler.sendEmptyMessage(0x123);

								} else {
									textX -= speed;
								}
								break;
							case 左边:
								for (int i = 0; i < text.length(); i++) {
									canvas.drawText(text.charAt(i) + "", textX, textY + (i + 1)
													* getFontHeight(textSize), paint);
								}
								holder.unlockCanvasAndPost(canvas);
								if (textY < -textHeight) {
									textY = CoofileTouchApplication.getScreenHeight();
									mHandler.sendEmptyMessage(0x123);
								} else {
									textY -= speed;
								}
								break;
							case 右边:
								for (int i = 0; i < text.length(); i++) {
									canvas.drawText(text.charAt(i) + "", textX, textY + (i + 1)
													* getFontHeight(textSize), paint);
								}
								holder.unlockCanvasAndPost(canvas);
								if (textY < -textHeight) {
									textY = CoofileTouchApplication.getScreenHeight();
									mHandler.sendEmptyMessage(0x123);
								} else {
									textY -= speed;
								}
								break;
						}

					} else {
						holder.unlockCanvasAndPost(canvas);
					}
				}

				SystemClock.sleep(100);

			}

		}
	};

	private Callback msgCallback = new Callback() {
		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			isStop = true;
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			new Thread(msgRun).start();
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		}
	};

	// 获取字体的大小
	public int getFontHeight(float fontSize) {
		Paint paint = new Paint();
		paint.setTextSize(fontSize);
		FontMetrics fm = paint.getFontMetrics();
		// return (int)Math.ceil(fm.bottom - fm.top);
		return (int) Math.ceil(fm.descent - fm.ascent);
		// return DensityUtil.px2dip(context, (int) Math.ceil(fm.descent -
		// fm.ascent));
	}

	// 停止消息滚动
	public void pauseMsg() {
		isStop = true;
	}

	// 继续消息滚动
	public void continueMsg() {
		isStop = false;
		new Thread(msgRun).start();
	}

	private float relativeDistanceX = 0f;
	private float relativeDistanceY = 0f;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				float dx1 = event.getX();
				float dy1 = event.getY();
				// 获得相对距离
				relativeDistanceX = Math.abs(textX - dx1);
				relativeDistanceY = Math.abs(textY - dy1);
				break;

			case MotionEvent.ACTION_UP:
				break;
			case MotionEvent.ACTION_MOVE:
				float x = event.getX();
				float y = event.getY();
				switch (msgType) {
					case 上边:
						textX = x - relativeDistanceX;
						break;
					case 下边:
						textX = x - relativeDistanceX;
						break;
					case 左边:
						textY = y - relativeDistanceY;
						break;
					case 右边:
						textY = y - relativeDistanceY;
						break;
				}
				break;
		}

		return false;
	}

}
