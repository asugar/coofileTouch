package com.twinflag.coofiletouch.show;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.twinflag.coofiletouch.entity.ElementBean;
import com.twinflag.coofiletouch.entity.RegionBean;
import com.twinflag.coofiletouch.parse.ParseText;
import com.twinflag.coofiletouch.showUtil.DestroyUtil;
import com.twinflag.coofiletouch.showUtil.GetNextUtil;
import com.twinflag.coofiletouch.showUtil.LoadUtil;
import com.twinflag.coofiletouch.showUtil.LogUtil;
import com.twinflag.coofiletouch.type.ElementType;
import com.twinflag.coofiletouch.type.LogType;
import com.twinflag.coofiletouch.utils.BitmapUtil;
import com.twinflag.coofiletouch.value.Constant;
import com.twinflag.coofiletouch.value.MessageValue;
import com.twinflag.coofiletouch.view.MyImageView;
import com.twinflag.coofiletouch.view.MyTextView;
import com.twinflag.coofiletouch.view.MyVideoView;

public class ShowRegion extends ShowBase {

	RegionBean region;// 当前区域实体
	ElementBean currentElement;// 当前元素
	ElementBean lastElement;// 上一个元素

	public ShowRegion(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ShowRegion(Context context, RegionBean region) {
		super(context);
		this.region = region;
		currentElement = region.getElements().get(0);
		loadElement(currentElement);
	}

	// 加载元素
	private void loadElement(final ElementBean currentElement) {
		ElementType elementType = currentElement.getType();
		switch (elementType) {
			case 文字:// 文本
				new LoadTextTask().execute(currentElement);
				// LoadUtil.loadTextElement(currentElement, getContext());
				break;
			case 音频:// 音频
				LoadUtil.loadMediaElement(currentElement, getContext());
				break;
			case 图片:// 图片
				loadImageAndOffice();
				break;
			case 视频:// 视频
				LoadUtil.loadVideoElement(currentElement, getContext());
				break;
			case 网页:// 网页
				this.post(new Runnable() {
					@Override
					public void run() {
						LoadUtil.loadWebElement(currentElement, getContext());
					}
				});

				break;
			case flash:
				this.post(new Runnable() {
					@Override
					public void run() {
						LoadUtil.loadFlashElement(currentElement, getContext());
					}
				});
				break;
			case office:
				loadImageAndOffice();
				break;
			case 流媒体:
				break;
			case 未定义:
				LoadUtil.loadErrorTypeElement(currentElement, getContext());
				break;
		}
	}

	private void loadImageAndOffice() {
		final ImageView image1 = new ImageView(getContext());
		image1.setScaleType(ScaleType.FIT_XY);
		currentElement.setView(image1);
		new LoadImageTask().execute(currentElement);
	}

	private class LoadTextTask extends AsyncTask<ElementBean, Void, ElementBean> {
		MyTextView text;

		@Override
		protected void onPreExecute() {
			text = new MyTextView(getContext());
		}

		@Override
		protected ElementBean doInBackground(ElementBean... params) {
			ElementBean element = params[0];
			element.setView(text);
			return ParseText.getText(element);
		}

		@Override
		protected void onPostExecute(ElementBean result) {
			if (result == null) {
				return;
			}
			View view = result.getView();
			if (view != null && view instanceof MyTextView) {
				MyTextView text = (MyTextView) view;
				text.content = result.getContent() == null ? "滚动消息获取失败" : result.getContent();
				text.fontSize = result.getSize() == null ? 35 : result.getSize();
				text.fontColor = Color.parseColor(result.getFgcolor() == null ? "#000000" : result.getFgcolor());
				text.bgColor = Color.parseColor(result.getBgcolor() == null ? "#FFFFFF" : result.getBgcolor());
			}

		}

	}

	private class LoadImageTask extends AsyncTask<ElementBean, Void, Bitmap> {
		ElementBean element;

		@Override
		protected Bitmap doInBackground(ElementBean... params) {
			element = params[0];
			Bitmap mBitmap = BitmapUtil.getBitmap(element.getSrc(), region.getWidth(), region.getHeight());
			return mBitmap;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			View view = element.getView();
			if (view != null && view instanceof ImageView) {
				ViewGroup parent = (ViewGroup) view.getParent();
				if (parent != null) {
					parent.removeView(view);
				}
				ImageView image = (ImageView) view;
				image.setImageBitmap(result);
				// image.bitmap = result;
				addView(image);
			}
		}
	}

	// 处理区域消息
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case MessageValue.DESTROY_ELEMENT:
					// rgionDestroy((ElementBean) msg.obj);
					rgionDestroy(lastElement);
					lastElement = currentElement;
					break;
				case MessageValue.LOAD_ELEMENT:
					regionLoad();
					break;
				case MessageValue.SHOW_ELEMENT:
					regionShow();
					break;
				case MessageValue.IMAGE_LOAD:
					break;
			}
		};
	};

	// 区域元素播放
	private void regionShow() {
		View view = currentElement.getView();
		MediaPlayer media = currentElement.getMedia();
		if (media == null) {
			if (view != null) {
				if (!(view instanceof MyImageView)) {
					ViewGroup parent = (ViewGroup) view.getParent();
					if (parent != null) {
						parent.removeView(view);
					}
					addView(view);
				}
				if (view instanceof MyVideoView) {
					MyVideoView video = (MyVideoView) view;
					video.start();
				}
			} else {
				LogUtil.printApointedLog(ShowRegion.class, "regionShow currentElement.getView is null", LogType.error);
				return;
			}
		}
		// 添加播放日志
		// WriteLogToFile.writeRegionLog(new
		// RegionLogBean(GlobalVariable.globalProgram,
		// GlobalVariable.globalShowProgram
		// .getCurrentScene(), currentElement));
		// 处理销毁的问题,延迟500ms进行销毁
		// Message msg = new Message();
		// msg.what = MessageValue.DESTROY_ELEMENT;
		// msg.obj = lastElement;
		// handler.sendMessageDelayed(msg, Constant.REGION_DELAY_DESTROY_TIME);
		handler.sendEmptyMessageDelayed(MessageValue.DESTROY_ELEMENT, Constant.REGION_DELAY_DESTROY_TIME);
	}

	// 区域销毁
	private void rgionDestroy(ElementBean element) {
		// 第一次销毁的时候上一个元素不存在，所以为空
		if (element != null) {
			View view = element.getView();
			MediaPlayer media = element.getMedia();
			if (view != null) {
				// 移除上一个元素
				element.setView(null);
				this.removeView(view);
				DestroyUtil.destroyView(view);
				view = null;
			}
			if (media != null) {
				element.setMedia(null);
				DestroyUtil.destroyMediaPlayer(media);
				media = null;
			}
		}
		// 提前预加载的时间2s
		handler.sendEmptyMessageDelayed(MessageValue.LOAD_ELEMENT, currentElement.getLife() * 1000
						- Constant.REGION_PRELOAD_TIME);
	}

	// 区域预加载
	private void regionLoad() {
		List<ElementBean> elements = region.getElements();
		if (elements.size() == 1) {
			return;
		}
		currentElement = GetNextUtil.getNextElement(elements, currentElement);
		loadElement(currentElement);
		handler.sendEmptyMessageDelayed(MessageValue.SHOW_ELEMENT, Constant.REGION_PRELOAD_TIME);
	}

	@Override
	public void startShow() {
		regionShow();
	}

	@Override
	public void destroyShow() {
		for (ElementBean element : region.getElements()) {
			View view = element.getView();
			MediaPlayer media = element.getMedia();
			if (view != null) {
				DestroyUtil.destroyView(view);
			}
			if (media != null) {
				DestroyUtil.destroyMediaPlayer(media);
			}
		}

		if (currentElement != null) {
			currentElement.setView(null);
		}
		if (lastElement != null) {
			lastElement.setView(null);
		}
	}

	@Override
	public void stopShow() {
		if (handler != null) {
			handler.removeCallbacksAndMessages(null);
			handler = null;
		}
	}

}
