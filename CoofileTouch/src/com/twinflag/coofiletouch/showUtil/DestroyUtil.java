package com.twinflag.coofiletouch.showUtil;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.twinflag.coofiletouch.plug.PhotoPrePlug;
import com.twinflag.coofiletouch.plug.SlidePagePlug;
import com.twinflag.coofiletouch.plug.SolidPagePlug;
import com.twinflag.coofiletouch.plug.WeatherPlug;
import com.twinflag.coofiletouch.view.MyErrorPage;
import com.twinflag.coofiletouch.view.MyImageView;
import com.twinflag.coofiletouch.view.MyTextView;
import com.twinflag.coofiletouch.view.MyVideoView;
import com.twinflag.coofiletouch.view.MyWebView;

final public class DestroyUtil {

	/**
	 * 销毁bitmap
	 * 
	 * @param bitmap
	 */
	public synchronized static void destroyBitmap(Bitmap bitmap) {
		if (bitmap != null && !bitmap.isRecycled()) {
			bitmap.recycle();
			bitmap = null;
		}
	}

	/**
	 * 销毁BitmapDrawable
	 * 
	 * @param drawable
	 */
	public synchronized static void destroyDrawable(BitmapDrawable drawable) {
		if (drawable == null) {
			return;
		}
		final Bitmap bitmap = drawable.getBitmap();
		new Thread(new Runnable() {
			@Override
			public void run() {
				destroyBitmap(bitmap);
			}
		}).start();
	}

	/**
	 * 销毁插件的方法
	 * 
	 * @param view
	 * @return
	 */
	public synchronized static Boolean destroyPlug(View view) {
		try {
			long time = System.currentTimeMillis();
			if (view instanceof PhotoPrePlug) {
				PhotoPrePlug mgv = (PhotoPrePlug) view;
				mgv.destroy();
				mgv = null;
				view = null;
				LogUtil.printLog(DestroyUtil.class, "destroy time: " + (System.currentTimeMillis() - time));
				return true;
			}
			if (view instanceof SlidePagePlug) {
				SlidePagePlug spp = (SlidePagePlug) view;
				spp.destroy();
				spp = null;
				view = null;
				return true;
			}
			if (view instanceof SolidPagePlug) {
				SolidPagePlug spp2 = (SolidPagePlug) view;
				spp2.destroy();
				spp2 = null;
				view = null;
				return true;
			}
			if (view instanceof WeatherPlug) {
				WeatherPlug weatherView = (WeatherPlug) view;
				weatherView.destroy();
				weatherView = null;
				view = null;
				return true;
			}
			if (view instanceof MyErrorPage) {
				MyErrorPage errorPage = (MyErrorPage) view;
				errorPage.destroyDrawingCache();
				errorPage = null;
				view = null;
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 销毁元素组件
	 * 
	 * @param view
	 */
	public synchronized static void destroyView(View view) {

		try {
			if (view instanceof ImageView) {// 回收ImageView图片
				destroyImageView(view);
				view = null;
				return;
			}
			if (view instanceof MyImageView) {// 回收图片
				destroyImage(view);
				view = null;
				return;
			}
			if (view instanceof MyVideoView) {// 回收视频
				destroyVideo(view);
				view = null;
				return;
			}
			if (view instanceof MyTextView) {// 回收文本
				destroyText(view);
				view = null;
				return;
			}
			if (view instanceof MyWebView) {// 回收网页和Flash
				destroyWebAndFlash(view);
				view = null;
				return;
			}
			if (view instanceof MyErrorPage) {// 回收错误页面
				MyErrorPage errorPage = (MyErrorPage) view;
				errorPage.destroyDrawingCache();
				errorPage = null;
				view = null;
				return;
			}
		} catch (Exception e) {
			Log.e("yi", "destroyView error");
			e.printStackTrace();
		}
	}

	public synchronized static Boolean destroyMediaPlayer(MediaPlayer media) {
		try {
			if (media != null) {
				if (media.isPlaying()) {
					media.stop();
				}
				media.reset();
				media.release();
				media = null;
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Log.w("yi", "destroyButton view error");
			return false;
		}
	}

	/**
	 * 销毁button按钮的方法
	 * 
	 * @param view
	 * @return true：正常销毁；false:view为空
	 */
	public synchronized static Boolean destroyButton(View view, Boolean isHaveBackImg) {
		try {
			Button button = (Button) view;
			Drawable drawable = button.getBackground();
			if (isHaveBackImg) {
				BitmapDrawable db = (BitmapDrawable) drawable;
				if (db != null) {
					destroyDrawable(db);
				}
			}
			drawable = null;
			button = null;
			view = null;
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	public synchronized static Boolean destroyImageView(View view) {
		try {
			ImageView image = (ImageView) view;
			Drawable drawable = image.getDrawable();
			BitmapDrawable bd = (BitmapDrawable) drawable;
			DestroyUtil.destroyDrawable(bd);
			if (image != null) {
				image = null;
			}
			view = null;
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Log.w("yi", "destroyImage view error");
			return false;
		}
	}

	/**
	 * 销毁图片
	 * 
	 * @param view
	 * @return true:正常销毁；false:view为空
	 */
	public synchronized static Boolean destroyImage(View view) {
		try {
			MyImageView image = (MyImageView) view;
			final Bitmap bitmap = image.bitmap;
			new Thread(new Runnable() {
				@Override
				public void run() {
					destroyBitmap(bitmap);
				}
			}).start();
			if (image != null) {
				image = null;
			}
			view = null;
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Log.w("yi", "destroyImage view error");
			return false;
		}

	}

	/**
	 * 销毁视频
	 * 
	 * @param view
	 * @return
	 */
	public synchronized static Boolean destroyVideo(View view) {
		try {
			MyVideoView video = (MyVideoView) view;
			// new Thread(new Runnable() {
			// @Override
			// public void run() {
			//
			// }
			// }).start();
			video.stopPlayback();
			video.destroyDrawingCache();
			video = null;
			view = null;
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Log.w("yi", "destroyVideo view error");
			return false;
		}

	}

	/**
	 * 销毁文本
	 * 
	 * @param view
	 * @return
	 */
	public synchronized static Boolean destroyText(View view) {
		try {
			Log.i("yi", "");
			SurfaceView text = (SurfaceView) view;
			text.destroyDrawingCache();
			text = null;
			view = null;
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Log.w("yi", "destroyText view error");
			return false;
		}

	}

	/**
	 * 销毁网页和Flash
	 * 
	 * @param view
	 * @return
	 */
	public synchronized static Boolean destroyWebAndFlash(View view) {
		try {
			WebView web = (WebView) view;
			// new Thread(new Runnable() {
			// @Override
			// public void run() {
			// }
			// }).start();
			web.stopLoading();
			web.clearHistory();
			web.clearCache(true);
			web.removeAllViews();
			web.destroyDrawingCache();
			web.destroy();
			web = null;
			view = null;
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Log.w("yi", "destroyWebAndFlash view error");
			return false;
		}

	}

	/**
	 * 是所有button失效
	 * 
	 * @param v
	 */

	public static void setAllButtonUnusable(View v) {
		ViewGroup parentView = (ViewGroup) v.getParent();
		ViewGroup rootView = (ViewGroup) parentView.getParent();
		for (int i = 0; i < rootView.getChildCount(); i++) {
			View viewChild = rootView.getChildAt(i);
			if (viewChild instanceof FrameLayout) {
				View view = ((FrameLayout) viewChild).getChildAt(0);
				if (view instanceof Button) {
					view.setEnabled(false);
				}
			}
		}
	}

	/**
	 * 是所有button可用
	 * 
	 * @param v
	 */
	public static void setAllButtonUsable(View v) {
		ViewGroup parentView = (ViewGroup) v.getParent();
		ViewGroup rootView = (ViewGroup) parentView.getParent();
		for (int i = 0; i < rootView.getChildCount(); i++) {
			View viewChild = rootView.getChildAt(i);
			if (viewChild instanceof FrameLayout) {
				View view = ((FrameLayout) viewChild).getChildAt(0);
				if (view instanceof Button) {
					view.setEnabled(true);
				}
			}
		}
	}

}
