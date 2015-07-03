package com.twinflag.coofiletouch.value;

import java.util.ArrayList;
import java.util.List;

import com.twinflag.coofiletouch.entity.MessageBean;

final public class GlobalValue {

	public static String errorMsg;
	public static boolean isLoading;
	public static long syncStartTime;
	public static boolean templateSyncing;
	public static boolean updateRegionSyncing;
	public static OptionsBean options;
	public static long templateStartTime;
	public static int templateIndex;
	// public static TemplateBean currentTemplate;
	public static long templateLife;
	// public static TaskBean currentTask;
	public static int messageIndex;
	public static MessageBean currentMessage;
	// public static ShowMusic showMusic;// 控制音频播放
	// public static ShowTemplate showTemplate;// 控制模板播放
	// public static ShowMessage showMessage;// 控制消息播放
	public static final int SYNC_MIN_TEMPLATE = 1000;
	public static final int SYNC_MAX_TEMPLATE = 3000;
	public static final int SYNC_MIN_ELEMENT = 500;
	public static final int SYNC_MAX_ELEMENT = 1500;
	public static final int MIN_SHOWTIME = 3000;// 元素最少显示的时间
	public static final int MAX_DIMENSIONS = 4096;// 元素最大支持的分辨率
	public static final int DELAY_SHOW_TIME = 1000;// 延迟显示
	public static final int DELAY_LOAD_TIME = 3400;// 提前加载
	public static final int DELAY_REMOVE_TIME = 1200;// 延迟销毁
	// public static volatile List<TaskBean> playTasks = new
	// ArrayList<TaskBean>();// 播放的任务
	public static volatile List<MessageBean> playMessages = new ArrayList<MessageBean>();// 播放的消息
	// public static volatile List<TemplateBean> playTemplates = new
	// ArrayList<TemplateBean>();// 播放的模板
	// public static volatile List<UpdateRegionBean> updateRegions = new
	// ArrayList<UpdateRegionBean>();// 局部更新的区域
	// public static volatile List<TimedDownloadBean> timedDownloads = new
	// ArrayList<TimedDownloadBean>(); // 定时更新列表

}
