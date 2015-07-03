package com.twinflag.coofiletouch.type;

public enum PlugType {

	图片插件(1), 时钟插件(5), 天气插件(6), office插件(7), 未定义(100);

	private int type;

	private PlugType(int type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return String.valueOf(this.type);
	}
}
