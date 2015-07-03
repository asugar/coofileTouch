package com.twinflag.coofiletouch.value;

public interface MessageValue {

	// 开始任务
	int START_PROGRAM = 1;
	// 停止任务
	int STOP_PROGRAM = 2;
	// 切换任务
	int SWITCH_PROGRAM = 3;

	// 开始消息
	int START_MESSAGE = 4;
	// 停止消息
	int STOP_MESSAGE = 5;
	// 切换消息
	int SWITCH_MESSAGE = 6;
	// 下载出错提示
	int DOWNLOAD_TASK_ERROR = 7;
	
	int LOAD_ELEMENT = 10;
	int SHOW_ELEMENT = 11;
	int DESTROY_ELEMENT = 12;

	int LOAD_SCENE = 13;
	int SHOW_SCENE = 14;
	int DESTROY_SCENE = 15;

	int IMAGE_LOAD = 20;
	int VIDEO_LOAD = 21;
	int BUTTON_LOAD = 22;

}
