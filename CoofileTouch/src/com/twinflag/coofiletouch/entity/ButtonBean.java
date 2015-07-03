package com.twinflag.coofiletouch.entity;

import android.view.View;

import com.twinflag.coofiletouch.show.ShowButton;
import com.twinflag.coofiletouch.type.ButtonType;

/**
 * @author wanghuayi button实体类
 */
public class ButtonBean {
	// 按钮id
	private String id;
	// 按钮名字
	private String name;
	// 按钮类型
	private ButtonType type;
	// y轴坐标
	private Integer top;
	// x轴坐标
	private Integer left;
	// 按钮宽
	private Integer width;
	// 按钮高
	private Integer height;
	// 要跳转到的场景id
	private String sceneid;

	// 按钮背景图片
	private String backImg;
	// 按钮背景色
	private String backColor;
	// 按钮显示文字
	private String value;
	// 按钮字体大小
	private String fontSize;
	// 按钮字体类型
	private String fontFamily;
	// 按钮字体颜色
	private String fontColor;
	// 标示按钮是否透明
	private Integer style;

	// 按钮view
	private View view;
	// 显示按钮的引用
	private ShowButton showButton;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ButtonType getType() {
		return type;
	}

	public void setType(Integer type) {
		switch (type) {
			case 1:
				this.type = ButtonType.切换;
				break;
			case 2:
				this.type = ButtonType.返回;
				break;
			case 3:
				this.type = ButtonType.主场景;
				break;
			default:
				this.type = ButtonType.未定义;
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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getSceneid() {
		return sceneid;
	}

	public void setSceneid(String sceneid) {
		this.sceneid = sceneid;
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

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public ShowButton getShowButton() {
		return showButton;
	}

	public void setShowButton(ShowButton showButton) {
		this.showButton = showButton;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBackImg() {
		return backImg;
	}

	public void setBackImg(String backImg) {
		this.backImg = backImg;
	}

	public String getBackColor() {
		return backColor;
	}

	public void setBackColor(String backColor) {
		this.backColor = backColor;
	}

	public String getFontSize() {
		return fontSize;
	}

	public void setFontSize(String fontSize) {
		this.fontSize = fontSize;
	}

	public String getFontFamily() {
		return fontFamily;
	}

	public void setFontFamily(String fontFamily) {
		this.fontFamily = fontFamily;
	}

	public String getFontColor() {
		return fontColor;
	}

	public void setFontColor(String fontColor) {
		this.fontColor = fontColor;
	}

	public Integer getStyle() {
		return style;
	}

	public void setStyle(Integer style) {
		this.style = style;
	}

}
