package com.twinflag.coofiletouch.entity;

import android.view.View;

import com.twinflag.coofiletouch.show.ShowPlug;
import com.twinflag.coofiletouch.type.PlugType;

/**
 * @author wanghuayi 插件实体类
 */
public class PlugBean {
	// 插件id
	private String id;
	// 插件name
	private String name;
	// 插件类型，详见read
	private PlugType type;
	// 插件y轴
	private Integer top;
	// 插件x轴
	private Integer left;
	// 插件宽度
	private Integer width;
	// 插件高度
	private Integer height;
	// 互动插件
	private AlbumBean album;
	// 天气插件
	private WeatherBean weather;
	// 时钟插件
	private TimepieceBean timepiece;
	// 互动视频插件
	private TouchVideoBean touchvideo;
	// 插件view引用
	private View view;
	// ShowPlug
	private ShowPlug showPlug;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public PlugType getType() {
		return type;
	}

	public void setType(Integer type) {
		switch (type) {
			case 1:
				this.type = PlugType.图片插件;
				break;
			case 5:
				this.type = PlugType.时钟插件;
				break;
			case 6:
				this.type = PlugType.天气插件;
				break;
			case 7:
				this.type = PlugType.office插件;
				break;
			default:
				this.type = PlugType.未定义;
				break;
		}
	}

	public Integer getTop() {
		return top;
	}

	public void setTop(Integer top) {
		this.top = top;
	}

	public Integer getLeft() {
		return left;
	}

	public void setLeft(Integer left) {
		this.left = left;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public AlbumBean getAlbum() {
		return album;
	}

	public void setAlbum(AlbumBean album) {
		this.album = album;
	}

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public ShowPlug getShowPlug() {
		return showPlug;
	}

	public void setShowPlug(ShowPlug showPlug) {
		this.showPlug = showPlug;
	}

	public WeatherBean getWeather() {
		return weather;
	}

	public void setWeather(WeatherBean weather) {
		this.weather = weather;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public TimepieceBean getTimepiece() {
		return timepiece;
	}

	public void setTimepiece(TimepieceBean timepiece) {
		this.timepiece = timepiece;
	}

	public TouchVideoBean getTouchvideo() {
		return touchvideo;
	}

	public void setTouchvideo(TouchVideoBean touchvideo) {
		this.touchvideo = touchvideo;
	}

}
