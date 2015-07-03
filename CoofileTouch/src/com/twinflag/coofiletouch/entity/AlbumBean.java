package com.twinflag.coofiletouch.entity;

import java.util.List;

import com.twinflag.coofiletouch.type.AlbumStyle;

/**
 * @author wanghuayi 图片插件实体类
 */
public class AlbumBean {
	// 样式类型
	private AlbumStyle style;
	// 包含元素
	List<ElementBean> elements;

	public AlbumStyle getStyle() {
		return style;
	}

	public void setStyle(Integer style) {
		switch (style) {
		// 相册预览效果(1), 图片翻页效果(2), 水平滑动效果(3), 立体旋转效果(4);
			case 1:
				this.style = AlbumStyle.相册预览效果;
				break;
			case 2:
				this.style = AlbumStyle.水平滑动效果;
				break;
			case 201:
				this.style = AlbumStyle.立体旋转效果;
				break;
			default:
				this.style = AlbumStyle.未定义;
				break;
		}

	}

	public List<ElementBean> getElements() {
		return elements;
	}

	public void setElements(List<ElementBean> elements) {
		this.elements = elements;
	}

}
