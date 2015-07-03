package com.twinflag.coofiletouch.type;

public enum ButtonType {

	切换(1), 返回(2), 主场景(3), 未定义(100);

	private int type;

	private ButtonType(int type) {
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
