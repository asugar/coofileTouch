package com.twinflag.coofiletouch.type;

public enum ElementType {

	文字(1), 音频(2), 图片(3), 视频(4), 网页(5), flash(6), office(7), 流媒体(8), 未定义(100);

	private int type;

	private ElementType(int type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return String.valueOf(this.type);
	}

	public int getIntType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
