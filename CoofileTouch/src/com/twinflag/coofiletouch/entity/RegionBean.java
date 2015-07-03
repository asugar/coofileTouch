package com.twinflag.coofiletouch.entity;

import java.util.List;

import com.twinflag.coofiletouch.show.ShowBase;
import com.twinflag.coofiletouch.type.ElementType;

/**
 * @author wanghuayi 区域实体类
 */
public class RegionBean {
	// 区域id
	private String id;
	// 区域name
	private String name;
	// y轴定位
	private Integer top;
	// x轴定位
	private Integer left;
	// 区域宽度
	private Integer width;
	// 区域高度
	private Integer height;
	// 区域类型：多一个0混合
	private ElementType type;
	// 是否是覆盖区域
	private boolean overLayFlag;

	private List<ElementBean> elements;

	private ShowBase showRegion;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public ElementType getType() {
		return type;
	}

	public void setType(Integer type) {
		switch (type) {
			case 1:
				this.type = ElementType.文字;
				break;
			case 2:
				this.type = ElementType.音频;
				break;
			case 3:
				this.type = ElementType.图片;
				break;
			case 4:
				this.type = ElementType.视频;
				break;
			case 5:
				this.type = ElementType.网页;
				break;
			case 6:
				this.type = ElementType.flash;
				break;
			case 7:
				this.type = ElementType.office;
				break;
			case 8:
				this.type = ElementType.流媒体;
				break;
		}
	}

	public boolean isOverLayFlag() {
		return overLayFlag;
	}

	public void setOverLayFlag(boolean overLayFlag) {
		this.overLayFlag = overLayFlag;
	}

	public List<ElementBean> getElements() {
		return elements;
	}

	public void setElements(List<ElementBean> elements) {
		this.elements = elements;
	}

	public ShowBase getShowRegion() {
		return showRegion;
	}

	public void setShowRegion(ShowBase showRegion) {
		this.showRegion = showRegion;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
