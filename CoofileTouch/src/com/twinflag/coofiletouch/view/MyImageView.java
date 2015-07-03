package com.twinflag.coofiletouch.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class MyImageView extends SurfaceView {

	public Bitmap bitmap;// 要绘制的图片内容
	private Paint paint;
	private SurfaceHolder holder;

	public MyImageView(Context context) {
		super(context);
		init();
	}

	public void init() {
//		setZOrderOnTop(true);
		paint = new Paint();
		paint.setAntiAlias(true);
		holder = getHolder();
		holder.setFormat(PixelFormat.TRANSPARENT);
		holder.addCallback(new Callback() {
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
			}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
//				if(bitmap == null){
//					SystemClock.sleep(1000);
//					surfaceCreated(holder);
//				}else{
					drawImage();
//				}
				
			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			}
		});
	}

	private void drawImage() {
		new Thread() {
			public void run() {
				Canvas canvas = holder.lockCanvas();
				if (canvas == null || bitmap == null) {
					return;
				}
				int w = getWidth();
				int h = getHeight();
				int bW = bitmap.getWidth();
				int bH = bitmap.getHeight();
				canvas.drawBitmap(bitmap, new Rect(0, 0, bW, bH), new Rect(0, 0, w, h), paint);
				holder.unlockCanvasAndPost(canvas);
			};
		}.start();
	}

}
