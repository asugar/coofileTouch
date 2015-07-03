package com.twinflag.coofiletouch.plug;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.twinflag.coofiletouch.CoofileTouchApplication;
import com.twinflag.coofiletouch.R;
import com.twinflag.coofiletouch.entity.ItemBean;
import com.twinflag.coofiletouch.entity.PicBean;
import com.twinflag.coofiletouch.entity.PlugBean;
import com.twinflag.coofiletouch.entity.WeatherBean;
import com.twinflag.coofiletouch.showUtil.DestroyUtil;
import com.twinflag.coofiletouch.showUtil.LogUtil;
import com.twinflag.coofiletouch.type.ItemType;
import com.twinflag.coofiletouch.type.LogType;
import com.twinflag.coofiletouch.value.Constant;
import com.twinflag.coofiletouch.value.GlobalValue;

public class WeatherPlug extends RelativeLayout implements BasePlug {

	private TextView city;// 城市
	private TextView temp;// 温度
	private TextView wind;// 风速
	private TextView sky;// 天气
	private ImageView icon;// 图标
	private AddWeatherThread addWeather;// 定时获取天气的异步任务
	private WeatherBean weather;

	public WeatherPlug(Context context) {
		super(context);
		setBackgroundColor(Color.TRANSPARENT);
		View view = View.inflate(context, R.layout.weather, this);
		city = (TextView) view.findViewById(R.id.weather_city);
		temp = (TextView) view.findViewById(R.id.weather_temp);
		wind = (TextView) view.findViewById(R.id.weather_wind);
		sky = (TextView) view.findViewById(R.id.weather_sky);
		icon = (ImageView) view.findViewById(R.id.weather_icon);
	}

	@Override
	public void load(PlugBean plug) {
		this.weather = plug.getWeather();
		if (weather == null) {
			LogUtil.printApointedLog(WeatherPlug.class, "weather is null", LogType.error);
			return;
		}
		refreshWearther();
		setItemLayout(weather);
	}

	/**
	 * 初始化天气控件的布局
	 */
	public void setItemLayout(WeatherBean weatherBean) {
		List<ItemBean> items = weatherBean.getItems();
		for (ItemBean item : items) {
			switch (item.getType()) {
				case 城市:
					initItemLayout(city, item);
					break;
				case 天气:
					initItemLayout(sky, item);
					break;
				case 温度:
					initItemLayout(temp, item);
					break;
				case 风速:
					initItemLayout(wind, item);
					break;
				case 未定义:
					break;
			}
		}
		PicBean pic = weatherBean.getPic();
		icon.setX(pic.getLeft());
		icon.setY(pic.getTop());
		icon.setLayoutParams(new RelativeLayout.LayoutParams(pic.getWidth(), pic.getHeight()));
	}

	/**
	 * 初始化天气条目的布局
	 */
	private void initItemLayout(TextView view, ItemBean item) {
		view.setX(item.getLeft());
		view.setY(item.getTop());
		view.setTextSize(item.getFontsize());
		view.setTextColor(Color.parseColor(item.getColor()));
	}

	/**
	 * 刷新天气
	 */
	public void refreshWearther() {
		if (addWeather == null) {
			addWeather = new AddWeatherThread();
			addWeather.execute();
		}
	}

	/**
	 * 停止刷新天气的任务
	 */
	private void stopRefresh() {
		if (addWeather != null) {
			addWeather.cancel(true);
			addWeather.executorService.shutdownNow();
			addWeather = null;
		}
	}

	/**
	 * 销毁天气插件
	 */
	@Override
	public void destroy() {
		stopRefresh();
		if (city != null) {
			this.removeView(city);
			city.destroyDrawingCache();
			city = null;
		}
		if (temp != null) {
			this.removeView(temp);
			temp.destroyDrawingCache();
			temp = null;
		}
		if (wind != null) {
			this.removeView(wind);
			wind.destroyDrawingCache();
			wind = null;
		}
		if (sky != null) {
			this.removeView(sky);
			sky.destroyDrawingCache();
			sky = null;
		}
		if (icon != null) {
			this.removeView(wind);
			Drawable drawable = icon.getDrawable();
			BitmapDrawable bd = (BitmapDrawable) drawable;
			DestroyUtil.destroyDrawable(bd);
			icon.destroyDrawingCache();
			icon = null;
		}
	}

	/**
	 * 定时获取天气信息的线程
	 */
	private class AddWeatherThread extends AsyncTask<Void, Boolean, Void> {

		ScheduledExecutorService executorService;

		@Override
		protected void onPreExecute() {
			executorService = Executors.newSingleThreadScheduledExecutor();
		}

		@Override
		protected Void doInBackground(Void... params) {
			executorService.scheduleAtFixedRate(new Runnable() {
				@Override
				public void run() {
					publishProgress(getData());
				}
			}, 0, 4, TimeUnit.HOURS);
			return null;
		}

		@Override
		protected void onProgressUpdate(Boolean... values) {
			Boolean isGetData = values[0];
			if (!isGetData) {
				Toast.makeText(getContext(), "天气信息获取失败", Toast.LENGTH_LONG).show();
				city.setText(null);
				temp.setText(null);
				sky.setText(null);
				wind.setText(null);
				Drawable drawable = icon.getDrawable();
				icon.setImageDrawable(null);
				icon.setImageBitmap(null);
				icon.setImageURI(null);
				BitmapDrawable bd = (BitmapDrawable) drawable;
				DestroyUtil.destroyDrawable(bd);
				return;
			}
			for (ItemBean item : weather.getItems()) {
				ItemType itemType = item.getType();
				// 城市(1), 温度(2), 天气(3), 风速(4);
				switch (itemType) {
					case 城市:
						city.setText(item.getContent());
						break;
					case 温度:
						temp.setText(item.getContent());
						break;
					case 天气:
						sky.setText(item.getContent());
						break;
					case 风速:
						wind.setText(item.getContent());
						break;
					case 未定义:
						break;
				}
			}
			icon.setImageURI(Uri.parse(CoofileTouchApplication.getAppResBasePath() + File.separator
							+ Constant.APP_RES_WEATHERIMAGE_FOLDER + File.separator + weather.getPic().getSrc()));// 设置天气图标
		}

	}

	/**
	 * 从服务器更新天气信息
	 */
	private Boolean getData() {
		try {
			String[] strs = GlobalValue.options.weatherurl.split("=");
			String needEncoded = URLEncoder.encode(strs[1], "utf-8");
			StringBuffer afterStr = new StringBuffer();
			afterStr.append(strs[0]).append("=").append(needEncoded);

			URL url = new URL(afterStr.toString());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(3000);
			if (conn.getResponseCode() == 200) {
				InputStream is = conn.getInputStream();
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = is.read(buffer)) != -1) {
					bos.write(buffer, 0, len);
				}
				String jsonString = bos.toString();
				bos.close();
				is.close();
				return parserWearther(jsonString);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 解析天气的JSON信息
	 */
	private Boolean parserWearther(String jsonString) throws Exception {
		JSONObject json = new JSONObject(jsonString);
		JSONArray jsonArray = json.getJSONArray("weatherinfo");
		if (jsonArray.length() == 0) {
			return false;
		}
		// for (int i = 0; i < jsonArray.length(); i++) {
		JSONObject jsonObject = jsonArray.getJSONObject(0);
		for (ItemBean item : weather.getItems()) {
			switch (item.getType()) {
				case 城市:
					item.setContent(jsonObject.getString("cityname"));
					break;
				case 天气:
					item.setContent(jsonObject.getString("sky"));
					break;
				case 温度:
					item.setContent(jsonObject.getString("temp"));
					break;
				case 风速:
					item.setContent(jsonObject.getString("wind"));
					break;
				case 未定义:
					break;
			}
		}
		weather.getPic().setSrc(jsonObject.getString("pic"));
		// }
		return true;
	}
}
