package com.twinflag.coofiletouch.entity;

/**
 * @author wanghuayi 时钟实体类
 */
public class TimepieceBean {

	private Integer style;// 时钟样式
	private ClockBean clock;// 时钟实体类

	public Integer getStyle() {
		return style;
	}

	public void setStyle(Integer style) {
		this.style = style;
	}

	public ClockBean getClock() {
		return clock;
	}

	public void setClock(ClockBean clock) {
		this.clock = clock;
	}

}
