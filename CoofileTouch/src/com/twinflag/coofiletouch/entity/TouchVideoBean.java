package com.twinflag.coofiletouch.entity;

import java.util.List;

/**
 * @author wanghuayi 互动视频实体类（暂时不支持）
 */
public class TouchVideoBean {

	// 样式类型
	private Integer style;
	// 包含元素
	List<ElementBean> elements;

	public Integer getStyle() {
		return style;
	}

	public void setStyle(Integer style) {
		this.style = style;
	}

	public List<ElementBean> getElements() {
		return elements;
	}

	public void setElements(List<ElementBean> elements) {
		this.elements = elements;
	}

}
