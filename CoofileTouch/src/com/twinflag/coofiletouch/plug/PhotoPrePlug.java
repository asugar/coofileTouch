package com.twinflag.coofiletouch.plug;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.twinflag.coofiletouch.R;
import com.twinflag.coofiletouch.entity.ElementBean;
import com.twinflag.coofiletouch.entity.PlugBean;
import com.twinflag.coofiletouch.showUtil.DestroyUtil;
import com.twinflag.coofiletouch.utils.BitmapUtil;

/**
 * 相册预览效果
 */
public class PhotoPrePlug extends RelativeLayout implements BasePlug {

	private ImageView gallery_iv;
	private HorizontalScrollView gallery_hsv;
	private LinearLayout gallery_ll;
	private ProgressBar gallery_pb;
	private int plugWidth;
	private int plugHeight;
	private int sampleWidth;
	private int sampleHeight;
	private boolean isShowing;
	private boolean isLoading;
	private List<ElementBean> elements;
	private ElementBean currentElement;
	private LoadPlugImageTask loadPlugImageTask;

	int count = 0;
	LoadBitmap mLoadBitmap;
	int currentIndex = 0;
	int lastIndex = 0;
	int futureIndex = 0;
	LoadNextImageTask mLoadNextImageTask;

	public PhotoPrePlug(Context context) {
		super(context);
	}

	@Override
	public void load(PlugBean plug) {
		View view = View.inflate(getContext(), R.layout.plug_photopre, this);
		gallery_iv = (ImageView) view.findViewById(R.id.plug_gallery_iv);// 背景图
		gallery_hsv = (HorizontalScrollView) view.findViewById(R.id.plug_gallery_hsv);// 滚动条
		gallery_ll = (LinearLayout) view.findViewById(R.id.plug_gallery_ll);// 线性布局
		gallery_pb = (ProgressBar) view.findViewById(R.id.plug_gallery_pb);// 滚动条

		plugWidth = plug.getWidth();// 原图显示的宽度
		plugHeight = plug.getHeight();// 原图显示的高度
		sampleWidth = plugWidth / 5;// 缩略图显示的宽度
		sampleHeight = plugHeight / 5;// 缩略图显示的宽度

		elements = plug.getAlbum().getElements();// 展示的元素
		count = elements.size();
		for (int i = 0; i < count; i++) {
			ImageView image = new ImageView(getContext());
			image.setOnClickListener(new SmallImageClickListener());
			// image.setOnTouchListener(smallImageListner);
			elements.get(i).setView(image);
			image.setId(i);
			image.setScaleType(ScaleType.FIT_XY);
			image.setPadding(0, 0, 0, 0);
			image.setLayoutParams(new LayoutParams(sampleWidth, sampleHeight));
			gallery_ll.addView(image);
			if (i < 10) {
				mLoadBitmap = new LoadBitmap();
				mLoadBitmap.execute(elements.get(i));
			}
		}
		currentElement = elements.get(0);// 当前显示的元素
		loadPlugImageTask = new LoadPlugImageTask();// 显示背景图的任务
		loadPlugImageTask.execute();

		gallery_iv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (gallery_hsv.isShown()) {
					gallery_hsv.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.gallery_hsv_gone));
					gallery_hsv.setVisibility(View.GONE);
				} else {
					gallery_hsv.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.gallery_hsv_visible));
					gallery_hsv.setVisibility(View.VISIBLE);
				}
			}
		});

		gallery_hsv.setOnTouchListener(new OnTouchListener() {
			Boolean isMove = false;

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						// getParent().requestDisallowInterceptTouchEvent(true);
						break;
					case MotionEvent.ACTION_MOVE:
						isMove = true;
						break;
					case MotionEvent.ACTION_UP:
						futureIndex = (gallery_hsv.getScrollX() + sampleWidth / 2) / sampleWidth;
						Log.i("yi", "gallery_hsv.getScrollX(): " + gallery_hsv.getScrollX() + " futureIndex: "
										+ futureIndex);
						if (futureIndex != lastIndex && futureIndex > -1 && futureIndex < count) {
							if (futureIndex > lastIndex) {// next
								for (int i = 1; i <= futureIndex - lastIndex; i++) {
									mLoadNextImageTask = new LoadNextImageTask();
									mLoadNextImageTask.execute(lastIndex + i);
									Log.i("yi", "-----------------");
									// lastIndex++;
								}
							} else {// last
								for (int i = 1; i <= lastIndex - futureIndex; i++) {
									mLoadNextImageTask = new LoadNextImageTask();
									mLoadNextImageTask.execute(lastIndex - i);
								}
								// lastIndex--;
							}

						}
						lastIndex = futureIndex;
						break;
				}
				if (isMove) {
					return false;
				} else {
					return true;
				}
			}

		});
	}

	private class LoadNextImageTask extends AsyncTask<Integer, Integer, Void> {

		private ImageView image;

		@Override
		protected Void doInBackground(Integer... params) {
			Integer futureIndex = params[0];
			if (futureIndex > currentIndex) {// 加载下一张回收上一张
				if (currentIndex + 10 < count) {
					Log.i("yi", "加载第 " + (currentIndex + 10) + " 张");
					mLoadBitmap = new LoadBitmap();
					mLoadBitmap.execute(elements.get(currentIndex + 10));
				}
				if (currentIndex - 5 >= 0 && currentIndex + 10 < count) {
					Log.i("yi", "销毁第 " + (currentIndex - 5) + " 张");
					image = (ImageView) elements.get(currentIndex - 5).getView();
					Drawable drawable = image.getDrawable();
					BitmapDrawable bd = (BitmapDrawable) drawable;
					DestroyUtil.destroyDrawable(bd);
				}
				currentIndex = futureIndex;

			} else {// 加载上一张回收下一张
				if (currentIndex - 6 >= 0) {
					Log.i("yi", "加载第 " + (currentIndex - 6) + " 张");
					mLoadBitmap = new LoadBitmap();
					mLoadBitmap.execute(elements.get(currentIndex - 6));
				}
				if (currentIndex + 9 < count && currentIndex >= 0) {
					Log.i("yi", "销毁第 " + (currentIndex + 9) + " 张");
					image = (ImageView) elements.get(currentIndex + 9).getView();
					Drawable drawable = image.getDrawable();
					BitmapDrawable bd = (BitmapDrawable) drawable;
					DestroyUtil.destroyDrawable(bd);
				}
				currentIndex = futureIndex;
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

	private class SmallImageClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			if (!isShowing) {
				ElementBean element = elements.get(v.getId());// 获取被点击的元素
				if (currentElement != element) {
					currentElement = element;
					loadPlugImageTask = new LoadPlugImageTask();
					loadPlugImageTask.execute();
					// if
					// ((currentElement.getSrc()).equals(v.getTag().toString()))
					// {// 判断元素对应的背景图是否一样
					// }
				}
			}
		}
	}

	private class LoadBitmap extends AsyncTask<ElementBean, Void, Bitmap> {
		private ImageView image;
		private Bitmap bitmap;

		@Override
		protected Bitmap doInBackground(ElementBean... params) {
			image = (ImageView) params[0].getView();
			bitmap = BitmapUtil.getBitmap(params[0].getSrc(), sampleWidth, sampleHeight);
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			if (image != null) {
				((ImageView) image).setImageBitmap(result);
			}
		}
	}

	private class LoadPlugImageTask extends AsyncTask<Void, Void, Bitmap> {

		@Override
		protected void onPreExecute() {
			setClickable(false);
			if (!gallery_pb.isShown()) {
				gallery_pb.setVisibility(View.VISIBLE);
			}
			gallery_iv.setImageDrawable(null);
			isShowing = true;
		}

		@Override
		protected Bitmap doInBackground(Void... params) {
			// publishProgress();
			BitmapDrawable drawable = (BitmapDrawable) gallery_iv.getDrawable();
			if (drawable != null) {
				Bitmap bitmap = drawable.getBitmap();
				if (bitmap != null && !bitmap.isRecycled()) {
					bitmap.recycle();
					bitmap = null;
				}
			}
			// DestroyUtil.destroyDrawable(drawable);

			return BitmapUtil.getBitmap(currentElement.getSrc(), plugWidth, plugHeight);
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			gallery_iv.setImageDrawable(null);
			gallery_iv.setImageBitmap(null);
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			gallery_iv.setImageBitmap(bitmap);
			// WriteLogToFile.writePlugLog(new
			// PlugLogBean(GlobalVariable.globalProgram,
			// GlobalVariable.globalShowProgram
			// .getCurrentScene(), currentElement));
			if (gallery_pb.isShown() && !isLoading) {
				gallery_pb.setVisibility(View.GONE);
			}
			isShowing = false;
			setClickable(true);
		}

	}

	@Override
	public void destroy() {
		if (loadPlugImageTask != null) {
			loadPlugImageTask.cancel(true);
		}

		BitmapDrawable bd = (BitmapDrawable) gallery_iv.getDrawable();
		DestroyUtil.destroyDrawable(bd);
		for (ElementBean element : elements) {
			View view = element.getView();
			element.setView(null);
			if (view != null && view instanceof ImageView) {
				BitmapDrawable bd2 = (BitmapDrawable) ((ImageView) view).getDrawable();
				DestroyUtil.destroyDrawable(bd2);
			}
		}

	}

}
