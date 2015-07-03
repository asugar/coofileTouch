package com.twinflag.coofiletouch.entity;

import com.twinflag.coofiletouch.type.ItemType;

/**
 * @author wanghuayi 天气项实体类
 */
public class ItemBean {

	private String color;
	private String font;
	private Integer fontsize;
	private Integer top;
	private Integer left;
	private ItemType type;
	private String content;

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getFont() {
		return font;
	}

	public void setFont(String font) {
		this.font = font;
	}

	public Integer getFontsize() {
		return fontsize;
	}

	public void setFontsize(Integer fontsize) {
		this.fontsize = fontsize;
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

	public ItemType getType() {
		return type;
	}

	public void setType(Integer type) {
		switch (type) {
			case 1:
				this.type = ItemType.城市;
				break;
			case 2:
				this.type = ItemType.温度;
				break;
			case 3:
				this.type = ItemType.天气;
				break;
			case 4:
				this.type = ItemType.风速;
				break;
			default:
				this.type = ItemType.未定义;
				break;
		}
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
