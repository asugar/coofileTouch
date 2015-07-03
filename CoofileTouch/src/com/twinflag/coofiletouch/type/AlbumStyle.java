package com.twinflag.coofiletouch.type;

public enum AlbumStyle {

	相册预览效果(1), 水平滑动效果(3), 立体旋转效果(4), 未定义(100);

	private int type;

	private AlbumStyle(int type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return String.valueOf(this.type);
	}
}
