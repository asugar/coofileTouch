package com.twinflag.coofiletouch.showUtil;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.util.Log;
import android.widget.Button;

import com.twinflag.coofiletouch.CoofileTouchApplication;
import com.twinflag.coofiletouch.entity.AlbumBean;
import com.twinflag.coofiletouch.entity.ButtonBean;
import com.twinflag.coofiletouch.entity.ElementBean;
import com.twinflag.coofiletouch.entity.PlugBean;
import com.twinflag.coofiletouch.parse.ParseText;
import com.twinflag.coofiletouch.plug.ClockPlug;
import com.twinflag.coofiletouch.plug.PhotoPrePlug;
import com.twinflag.coofiletouch.plug.SlidePagePlug;
import com.twinflag.coofiletouch.plug.SolidPagePlug;
import com.twinflag.coofiletouch.plug.WeatherPlug;
import com.twinflag.coofiletouch.type.ElementType;
import com.twinflag.coofiletouch.type.PlugType;
import com.twinflag.coofiletouch.view.MyErrorPage;
import com.twinflag.coofiletouch.view.MyImageView;
import com.twinflag.coofiletouch.view.MyTextView;
import com.twinflag.coofiletouch.view.MyVideoView;
import com.twinflag.coofiletouch.view.MyWebView;

public class LoadUtil {

	/**
	 * 加载plug插件
	 * 
	 * @param plug
	 * @param context
	 * @return true:加载成功；false:加载失败
	 */
	public static Boolean loadPlug(PlugBean plug, Context context) {
		boolean isSucces = false;
		// BasePlug basePlug;
		PlugType plugType = plug.getType();
		switch (plugType) {
			case 图片插件:
				AlbumBean album = plug.getAlbum();
				switch (album.getStyle()) {
					case 相册预览效果:
						PhotoPrePlug mgv = new PhotoPrePlug(context);
						plug.setView(mgv);
						mgv.load(plug);
						isSucces = true;
						break;
					case 水平滑动效果:
						SlidePagePlug spp = new SlidePagePlug(context);
						plug.setView(spp);
						spp.load(plug);
						isSucces = true;
						break;
					case 立体旋转效果:
						SolidPagePlug spp2 = new SolidPagePlug(context);
						plug.setView(spp2);
						spp2.load(plug);
						isSucces = true;
						break;
					default:
						SlidePagePlug spp1 = new SlidePagePlug(context);
						plug.setView(spp1);
						spp1.load(plug);
						isSucces = true;
						break;
				}
				break;
			case 天气插件:
				WeatherPlug weatherView = new WeatherPlug(context);
				plug.setView(weatherView);
				weatherView.load(plug);
				isSucces = true;
				break;
			// 时钟插件未添加
			case 时钟插件:
				ClockPlug clock = new ClockPlug(context);
				plug.setView(clock);
				clock.load(plug);
				isSucces = true;
				break;
			case office插件:
				SlidePagePlug spp1 = new SlidePagePlug(context);
				plug.setView(spp1);
				spp1.load(plug);
				isSucces = true;
				break;
			case 未定义:
				// 处理默认情况
				MyErrorPage text = new MyErrorPage(context);
				plug.setView(text);
				isSucces = true;
				break;
		}
		return isSucces;
	}

	/**
	 * 加载Button组件
	 * 
	 * @param button
	 * @param context
	 * @return true:加载成功；false:加载失败
	 */
	public static synchronized Boolean loadButton(final ButtonBean button, final Context context) {
		String url = button.getBackImg();
		if (!IsSrcExist(1, url, context)) {
			return false;
		}
		final Button buttonView = new Button(context);
		button.setView(buttonView);

		buttonView.post(new Runnable() {
			@Override
			public void run() {
				Bitmap mBitmap = BitmapFactory.decodeFile(button.getBackImg());
				// Drawable background =
				// Drawable.createFromPath(button.getSrc());
				buttonView.setBackground(new BitmapDrawable(context.getResources(), mBitmap));
				buttonView.setText(button.getValue());
			}
		});
		// buttonView.setVisibility(View.INVISIBLE);
		// buttonView.setId(button.getId());

		return true;
	}

	/**
	 * 加载音频播放
	 * 
	 * @param element
	 * @param context
	 * @return
	 */
	public static synchronized Boolean loadMediaElement(final ElementBean element, Context context) {
		try {
			String url = CoofileTouchApplication.getAppResBasePath() + element.getSrc();
			if (!IsSrcExist(2, url, context)) {
				return false;
			}
			final MediaPlayer media = new MediaPlayer();
			element.setMedia(media);
			media.setDataSource(url);
			media.prepareAsync();
			media.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					if (element.getLife() * 1000 - media.getDuration() > 0) {
						media.seekTo(0);
						media.start();
					}
				}
			});
			media.setOnPreparedListener(new OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mp) {
					mp.start();
				}
			});

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 加载文本
	 * 
	 * @param element
	 * @param context
	 * @return true:加载成功；false:加载失败
	 */
	public static synchronized Boolean loadTextElement(ElementBean element, Context context) {
		try {
			MyTextView text = new MyTextView(context);
			element.setView(text);
			ElementBean parser = ParseText.getText(element);
			text.content = parser.getContent();
			text.fontSize = parser.getSize();
			text.fontColor = Color.parseColor(parser.getFgcolor());
			text.bgColor = Color.parseColor(parser.getBgcolor());
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	/**
	 * 加载flash
	 * 
	 * @param element
	 * @param context
	 * @return true:加载成功；false:加载失败
	 */
	public static synchronized Boolean loadErrorTypeElement(ElementBean element, Context context) {
		try {
			MyErrorPage text = new MyErrorPage(context);
			element.setView(text);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * 加载flash
	 * 
	 * @param element
	 * @param context
	 * @return true:加载成功；false:加载失败
	 */
	public static synchronized Boolean loadFlashElement(ElementBean element, Context context) {
		try {
			String url = CoofileTouchApplication.getAppResBasePath() + element.getSrc();
			if (!IsSrcExist(5, url, context)) {
				return false;
			}
			MyWebView flash = new MyWebView(context);
			element.setView(flash);
			flash.loadUrl("file:" + url);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * 加载网页
	 * 
	 * @param element
	 * @param context
	 * @return true:加载成功；false:加载失败
	 */
	public static synchronized Boolean loadWebElement(ElementBean element, Context context) {
		String url = element.getWebSrc();
		if (IsWebSrcNull(url, context)) {
			return false;
		}
		MyWebView web = new MyWebView(context);
		element.setView(web);
		web.loadUrl(url);
		return true;
	}

	/**
	 * 加载视频
	 * 
	 * @param element
	 * @param context
	 * @return true:加载成功；false:加载失败
	 */
	public static synchronized Boolean loadVideoElement(ElementBean element, Context context) {
		final String url = CoofileTouchApplication.getAppResBasePath() + element.getSrc();
		if (!IsSrcExist(2, url, context)) {
			return false;
		}
		final MyVideoView video = new MyVideoView(context);
		video.setVideoPath(url);
		video.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				video.setVideoPath(url);
			}
		});
		video.setOnPreparedListener(new OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				video.start();
			}
		});
		video.setOnErrorListener(new OnErrorListener() {
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				Log.e("yi", "视频播放出错！！");
				if (video.isPlaying()) {
					video.stopPlayback();
				}
				video.setVideoPath(url);
				return true;
			}
		});
		element.setView(video);
		return true;
	}

	/**
	 * 加载图片（暂时没有使用）
	 * 
	 * @param element
	 * @param context
	 * @return true:加载成功；false:加载失败
	 */
	public static synchronized Boolean loadImageElement(ElementBean element, Context context) {
		final String url = element.getSrc();
		if (!IsSrcExist(1, url, context)) {
			return false;
		}
		final MyImageView imageView = new MyImageView(context);
		element.setView(imageView);
		imageView.post(new Runnable() {
			@Override
			public void run() {
				imageView.bitmap = BitmapFactory.decodeFile(url, null);
			}
		});
		return true;
	}

	/**
	 * 加载元素组件(暂时没有使用)
	 * 
	 * @param element
	 * @param context
	 * @return true:加载成功；false:加载失败
	 */
	public static Boolean loadElement(ElementBean element, Context context) {
		boolean isSucces = false;
		ElementType elementType = element.getType();
		switch (elementType) {
			case 文字:// 文本
				if (loadTextElement(element, context)) {
					isSucces = true;
				}
				break;
			case 音频:// 音频
				if (loadMediaElement(element, context)) {
					isSucces = true;
				}
				break;
			case 图片:// 图片
				if (loadImageElement(element, context)) {
					isSucces = true;
				}
				break;
			case 视频:// 视频
				if (loadVideoElement(element, context)) {
					isSucces = true;
				}
				break;
			case 网页:// 网页
				if (loadWebElement(element, context)) {
					isSucces = true;
				}
				break;
			case flash:// flash
				if (loadFlashElement(element, context)) {
					isSucces = true;
				}
				break;
			case office:// office

				break;
			case 流媒体:// 流媒体

				break;
			case 未定义:

				break;
		}
		// 始终都会执行
		return isSucces;
	}

	/**
	 * 判断src路径下的文件是否存在
	 * 
	 * @param flag
	 *            标示元素类型
	 * @param url
	 *            文件路径
	 * @param context
	 * @return true 存在； fase 不存在
	 */
	public static Boolean IsSrcExist(int flag, String url, Context context) {
		if (!(new File(url).exists())) {
			Log.e("yi", "element type:" + flag + "file is not exits");
			return false;
		}
		return true;
	}

	/**
	 * 判断网页路径是否为空
	 * 
	 * @param url
	 * @param context
	 * @return true 为空； false 不为空
	 */
	public static Boolean IsWebSrcNull(String url, Context context) {
		if (url == null || url == "") {
			Log.e("yi", "websrc is not exits");
			return true;
		}
		return false;
	}

}
