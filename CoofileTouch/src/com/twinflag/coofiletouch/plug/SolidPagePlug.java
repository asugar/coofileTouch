package com.twinflag.coofiletouch.plug;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Scroller;

import com.twinflag.coofiletouch.entity.ElementBean;
import com.twinflag.coofiletouch.entity.PlugBean;
import com.twinflag.coofiletouch.showUtil.DestroyUtil;
import com.twinflag.coofiletouch.utils.BitmapUtil;

/**
 * 立体旋转效果
 */
public class SolidPagePlug extends FrameLayout implements BasePlug {

	private float lastTouchX;
	private float angle = 90;// 旋转的角度

	private Matrix matrix;
	private Camera camera;
	private Scroller scroller;
	private int plugWidth;
	private int plugHeight;

	private List<ElementBean> elements;
	private LoadBitmap mLoadBitmap;
	private int count = 0;//
	private int currentIndex = 0;// 记录当前所在的屏幕
	private int left = 0;

	public SolidPagePlug(Context context) {
		super(context);
	}

	@Override
	public void load(PlugBean plug) {
		matrix = new Matrix();
		camera = new Camera();
		scroller = new Scroller(getContext());

		elements = plug.getAlbum().getElements();
		count = elements.size();
		plugWidth = plug.getWidth();
		plugHeight = plug.getHeight();
		for (int i = 0; i < count; i++) {
			ImageView image = new ImageView(getContext());
			elements.get(i).setView(image);
			image.setScaleType(ScaleType.FIT_XY);
			image.layout(left, 0, left + plugWidth, plugHeight);
			left += plugWidth;
			addView(image);
			if (i < 2) {
				mLoadBitmap = new LoadBitmap();
				mLoadBitmap.execute(elements.get(i));
			}
		}

	}

	private class LoadBitmap extends AsyncTask<ElementBean, Void, Bitmap> {

		private ImageView image;
		private Bitmap bitmap;

		@Override
		protected Bitmap doInBackground(ElementBean... params) {
			image = (ImageView) params[0].getView();
			bitmap = BitmapUtil.getBitmap(params[0].getSrc(), plugWidth, plugHeight);
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			if (image != null) {
				((ImageView) image).setImageBitmap(result);
			}
		}
	}

	/**
	 * 初始化图片的位置使其水平排列
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (changed) {
			int left = 0;
			for (int i = 0; i < getChildCount(); i++) {
				View view = getChildAt(i);
				if (view.isShown()) {
					// 水平滑动
					view.layout(left, 0, left + plugWidth, plugHeight);
					left += plugWidth;
					// 垂直滑动
					// view.layout(0, left, plugWidth, left + plugHeight);
					// left += plugHeight;
				}
			}
		}
	}

	LoadNextImageTask mLoadNextImageTask;
	int screenIndexLast = 2;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (!scroller.isFinished()) {
					scroller.abortAnimation();// 停止动画
				}
				lastTouchX = x;
				break;
			case MotionEvent.ACTION_MOVE:
				scrollBy((int) (lastTouchX - x), 0);
				lastTouchX = x;
				break;
			case MotionEvent.ACTION_UP:
				int screenIndex = (getScrollX() + getWidth() / 2) / getWidth();// 根据位置算出在那个屏幕
				snapToScreen(screenIndex);
				if (screenIndex != screenIndexLast && screenIndex > -1 && screenIndex < count) {
					mLoadNextImageTask = new LoadNextImageTask();
					mLoadNextImageTask.execute(screenIndex);
				}
				screenIndexLast = screenIndex;
				break;
		}
		return true;
	}

	private class LoadNextImageTask extends AsyncTask<Integer, Integer, Void> {

		private ImageView image;

		@Override
		protected Void doInBackground(Integer... params) {
			try {
				Integer screenIndex = params[0];
				if (screenIndex > currentIndex) {// 加载下一张回收上一张
					if (currentIndex + 2 < count) {
						mLoadBitmap = new LoadBitmap();
						mLoadBitmap.execute(elements.get(currentIndex + 2));
					}
					if (currentIndex - 1 >= 0 && currentIndex + 2 < count) {
						image = (ImageView) elements.get(currentIndex - 1).getView();
						Drawable drawable = image.getDrawable();
						BitmapDrawable bd = (BitmapDrawable) drawable;
						DestroyUtil.destroyDrawable(bd);
					}
					currentIndex = screenIndex;

				} else {// 加载上一张回收下一张
					if (currentIndex - 2 >= 0) {
						mLoadBitmap = new LoadBitmap();
						mLoadBitmap.execute(elements.get(currentIndex - 2));
					}
					if (currentIndex + 1 < count && currentIndex - 2 >= 0) {
						image = (ImageView) elements.get(currentIndex + 1).getView();
						Drawable drawable = image.getDrawable();
						BitmapDrawable bd = (BitmapDrawable) drawable;
						DestroyUtil.destroyDrawable(bd);
					}
					currentIndex = screenIndex;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (image != null) {
				image.destroyDrawingCache();
				image.setImageBitmap(null);
			}
		};
	}

	/**
	 * 滚动到指定屏幕
	 */
	private void snapToScreen(int screenIndex) {
		screenIndex = Math.max(0, Math.min(screenIndex, getChildCount() - 1));
		if (getScrollX() != (screenIndex * getWidth())) {
			int delta = screenIndex * getWidth() - getScrollX();
			scroller.startScroll(getScrollX(), 0, delta, 0, Math.abs(delta) * 2);
			postInvalidate();
		}
	}

	@Override
	public void computeScroll() {
		if (scroller.computeScrollOffset()) {// 保证图片显示完全
			scrollTo(scroller.getCurrX(), scroller.getCurrY());
			postInvalidate();
		}
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		for (int i = 0; i < getChildCount(); i++) {
			drawScreen(canvas, i, getDrawingTime());
		}
	}

	/*
	 * 立体效果的实现函数 ,screen为哪一个子View
	 */
	private void drawScreen(Canvas canvas, int screen, long drawingTime) {
		int width = getWidth();// 得到当前子View的宽度
		int scrollWidth = screen * width;
		int scrollX = this.getScrollX();
		if (scrollWidth > scrollX + width || scrollWidth + width < scrollX) {
			return;
		}
		View child = getChildAt(screen);
		int faceIndex = screen;
		float currentDegree = getScrollX() * (angle / getMeasuredWidth());
		float faceDegree = currentDegree - faceIndex * angle;
		if (faceDegree > 90 || faceDegree < -90) {
			return;
		}
		float centerX = (scrollWidth < scrollX) ? scrollWidth + width : scrollWidth;
		float centerY = getHeight() / 2;
		canvas.save();
		camera.save();
		camera.rotateY(-faceDegree);
		camera.getMatrix(matrix);
		camera.restore();
		matrix.preTranslate(-centerX, -centerY);
		matrix.postTranslate(centerX, centerY);
		canvas.concat(matrix);
		drawChild(canvas, child, drawingTime);
		canvas.restore();
	}

	@Override
	public void destroy() {
		if (mLoadNextImageTask != null) {
			mLoadNextImageTask.cancel(true);
			mLoadNextImageTask = null;
		}
		if (mLoadBitmap != null) {
			mLoadBitmap.cancel(true);
			mLoadBitmap = null;
		}

		this.removeAllViews();

		if (elements != null && elements.size() > 0) {
			for (ElementBean element : elements) {
				View view = element.getView();

				if (view != null && view instanceof ImageView) {
					ImageView image = (ImageView) view;
					Drawable drawable = image.getDrawable();
					BitmapDrawable bd = (BitmapDrawable) drawable;
					DestroyUtil.destroyDrawable(bd);
					if (image != null) {
						image = null;
					}
					element.setView(null);
				}
			}
			elements = null;
		}
	}

}
