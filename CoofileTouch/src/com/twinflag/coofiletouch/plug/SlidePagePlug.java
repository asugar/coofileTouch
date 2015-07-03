package com.twinflag.coofiletouch.plug;

import java.text.MessageFormat;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.view.ViewPager.PageTransformer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.twinflag.coofiletouch.R;
import com.twinflag.coofiletouch.entity.ElementBean;
import com.twinflag.coofiletouch.entity.PlugBean;
import com.twinflag.coofiletouch.photoview.PhotoView;
import com.twinflag.coofiletouch.showUtil.DestroyUtil;
import com.twinflag.coofiletouch.utils.BitmapUtil;

public class SlidePagePlug extends RelativeLayout implements BasePlug {

	private int width;
	private int height;
	private List<ElementBean> elements;
	private LoadBitmap mloadBitmap;
	private ViewPager mViewPager;
	// private ProgressBar mProgressBar;
	private TextView mPageText;
	private View view;
	private String messageFormat = "第 {1} 页  共 {0} 页";

	public SlidePagePlug(Context context) {
		super(context);
	}

	@Override
	public void load(PlugBean plug) {
		view = View.inflate(getContext(), R.layout.plug_slidepage, this);
		mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
		// mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
		elements = plug.getAlbum().getElements();
		width = plug.getWidth();
		height = plug.getHeight();
		mPageText = (TextView) view.findViewById(R.id.tv_pageflag);
		mViewPager.setAdapter(new MyPagerAdapter());
		mPageText.setText(MessageFormat.format(messageFormat, elements.size(), 1));
		mViewPager.setOnPageChangeListener(new MyPageCHangeListener());
	}

	private class MyPageCHangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int arg0) {
			mPageText.setText(MessageFormat.format(messageFormat, elements.size(), ++arg0));
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	private class MyPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return elements.size(); // Integer.MAX_VALUE; //
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ElementBean element = elements.get(position);
			PhotoView image = new PhotoView(getContext());
			element.setView(image);
			image.setScaleType(ScaleType.FIT_XY);
			container.addView(image);
			// WriteLogToFile.writePlugLog(new
			// PlugLogBean(GlobalVariable.globalProgram,
			// GlobalVariable.globalShowProgram
			// .getCurrentScene(), element));
			// pb.setVisibility(View.VISIBLE);
			mloadBitmap = new LoadBitmap();
			mloadBitmap.execute(element);
			return image;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			ElementBean element = elements.get(position);
			View view = element.getView();
			container.removeView(view);
			element.setView(null);
			if (view instanceof PhotoView) {
				PhotoView image = (PhotoView) view;
				Drawable drawable = image.getDrawable();
				BitmapDrawable bd = (BitmapDrawable) drawable;
				DestroyUtil.destroyDrawable(bd);
				if (image != null) {
					image.destroyDrawingCache();
					image = null;
				}
			}
			view = null;
		}

	}

	private class LoadBitmap extends AsyncTask<ElementBean, Void, Bitmap> {

		private PhotoView image;
		private Bitmap bitmap;

		@Override
		protected Bitmap doInBackground(ElementBean... params) {
			image = (PhotoView) params[0].getView();
			bitmap = BitmapUtil.getBitmap(params[0].getSrc(), width, height);
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			if (image != null) {
				((PhotoView) image).setImageBitmap(result);
				// mProgressBar.setVisibility(View.INVISIBLE);
				// view.setClickable(true);
			}
		}
	}

	@Override
	public void destroy() {

		if (mloadBitmap != null && !mloadBitmap.isCancelled()) {
			mloadBitmap.cancel(true);
			mloadBitmap = null;
		}

		this.removeAllViews();

		if (elements != null && elements.size() > 0) {
			for (ElementBean element : elements) {
				View view = element.getView();

				if (view != null && view instanceof PhotoView) {
					PhotoView image = (PhotoView) view;
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

		if (mPageText != null) {
			mPageText.destroyDrawingCache();
			mPageText = null;
		}

		if (view != null) {
			view = null;
		}
	}

	public class DepthPageTransformer implements PageTransformer {
		private float MIN_SCALE = 0.75f;

		@Override
		public void transformPage(View view, float position) {
			int pageWidth = view.getWidth();
			if (position < -1) {
				view.setAlpha(0);
			} else if (position <= 0) {
				view.setAlpha(1);
				view.setTranslationX(0);
				view.setScaleX(1);
				view.setScaleY(1);
			} else if (position <= 1) {
				view.setAlpha(1 - position);
				view.setTranslationX(pageWidth * -position);
				float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
				view.setScaleX(scaleFactor);
				view.setScaleY(scaleFactor);
			} else {
				view.setAlpha(0);

			}
		}

	}

	//
	public class ZoomOutPageTransformer implements PageTransformer {
		private float MIN_SCALE = 0.85f;
		private float MIN_ALPHA = 0.5f;

		@Override
		public void transformPage(View view, float position) {
			int pageWidth = view.getWidth();
			int pageHeight = view.getHeight();
			if (position < -1) {
				view.setAlpha(0);
			} else if (position <= 1) {
				float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
				float vertMargin = pageHeight * (1 - scaleFactor) / 2;
				float horzMargin = pageWidth * (1 - scaleFactor) / 2;
				if (position < 0) {
					view.setTranslationX(horzMargin - vertMargin / 2);
				} else {
					view.setTranslationX(-horzMargin + vertMargin / 2);
				}
				view.setScaleX(scaleFactor);
				view.setScaleY(scaleFactor);
				view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
			} else {
				view.setAlpha(0);
			}
		}
	}

}
