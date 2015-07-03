package com.twinflag.coofiletouch.type;

public enum MsgType {

	上边(1), 下边(2), 左边(3), 右边(4);

	private int type;

	private MsgType(int type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return String.valueOf(this.type);
	}
}
