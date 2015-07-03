package com.twinflag.coofiletouch.entity;

import com.twinflag.coofiletouch.type.MsgType;

/**
 * @author wanghuayi 滚动消息实体类
 */
public class MessageBean {
	// 滚动消息id
	private String id;
	// 滚动消息速度
	private Integer speed;
	// 滚动消息的位置，1,2,3,4；上，下，左，右
	private MsgType position;
	// 字体样式
	private String font;
	// 字体大小
	private Integer size;
	// 字体前景色
	private String fgcolor;
	// 字体背景色
	private String bgcolor;
	// 开始日期和时间
	private String startDate;
	// 结束日期和时间
	private String stopDate;
	// 字体内容
	private String content;
	// 更新时间
	private String updateTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getSpeed() {
		return speed;
	}

	public void setSpeed(Integer speed) {
		this.speed = speed;
	}

	public MsgType getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		switch (position) {
			case 1:
				this.position = MsgType.上边;
				break;
			case 2:
				this.position = MsgType.下边;
				break;
			case 3:
				this.position = MsgType.左边;
				break;
			case 4:
				this.position = MsgType.右边;
				break;
			default:
				this.position = MsgType.下边;
				break;
		}
	}

	public String getFont() {
		return font;
	}

	public void setFont(String font) {
		this.font = font;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public String getFgcolor() {
		return fgcolor;
	}

	public void setFgcolor(String fgcolor) {
		this.fgcolor = fgcolor;
	}

	public String getBgcolor() {
		return bgcolor;
	}

	public void setBgcolor(String bgcolor) {
		this.bgcolor = bgcolor;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getStopDate() {
		return stopDate;
	}

	public void setStopDate(String stopDate) {
		this.stopDate = stopDate;
	}

}
