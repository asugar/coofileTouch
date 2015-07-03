package com.twinflag.coofiletouch.entity;

/**
 * @author wanghuayi 时钟实体类
 */
public class ClockBean {

	private String bgcolor;// 背景色
	private String fgcolor;// 前景色
	private String font;// 字体
	private String format;// 格式
	private Integer top;// 距离顶
	private Integer left;// 距离左边
	private Integer height;// 高
	private Integer width;// 宽
	private Integer size;// 字体大小

	public String getBgcolor() {
		return bgcolor;
	}

	public void setBgcolor(String bgcolor) {
		this.bgcolor = bgcolor;
	}

	public String getFgcolor() {
		return fgcolor;
	}

	public void setFgcolor(String fgcolor) {
		this.fgcolor = fgcolor;
	}

	public String getFont() {
		return font;
	}

	public void setFont(String font) {
		this.font = font;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
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

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

}
