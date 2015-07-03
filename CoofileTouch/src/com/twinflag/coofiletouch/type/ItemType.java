package com.twinflag.coofiletouch.type;

public enum ItemType {

	// type 1:cityname; 2:temp; 3:sky; 4:wind

	城市(1), 温度(2), 天气(3), 风速(4), 未定义(100);

	private int type;

	private ItemType(int type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return String.valueOf(this.type);
	}
}
