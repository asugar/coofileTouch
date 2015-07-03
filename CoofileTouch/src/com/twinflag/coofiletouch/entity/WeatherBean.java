package com.twinflag.coofiletouch.entity;

import java.util.List;

/**
 * @author wanghuayi 天气实体类
 */
public class WeatherBean {

	private List<ItemBean> items;
	private PicBean pic;
	private Integer style;

	public List<ItemBean> getItems() {
		return items;
	}

	public void setItems(List<ItemBean> items) {
		this.items = items;
	}

	public PicBean getPic() {
		return pic;
	}

	public void setPic(PicBean pic) {
		this.pic = pic;
	}

	public Integer getStyle() {
		return style;
	}

	public void setStyle(Integer style) {
		this.style = style;
	}

}
