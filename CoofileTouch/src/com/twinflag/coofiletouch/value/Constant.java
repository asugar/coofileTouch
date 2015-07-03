package com.twinflag.coofiletouch.value;

public class Constant {

	// 是否支持授权
	public final static boolean IS_SUPPORT_AUTHORIZED_INSTALLATION = false;
	// 场景加载时间
	public final static long SCENE_PRLOAD_TIME = 250;
	// 场景延迟销毁时间
	public final static long SCENE_DELAY_DESTROY_TIME = 100;
	// 区域延迟销毁的时间
	public final static long REGION_DELAY_DESTROY_TIME = 500;
	// 区域预加载时间
	public final static long REGION_PRELOAD_TIME = 2000;
	// 应用根目录
	public final static String APP_RES_PARENT_FOLDER = "CooFileTouch";
	// 素材下载目录
	public final static String APP_RES_MATERIAL_FOLDER = "Processed";
	// 授权文件目录
	public final static String APP_RES_AUTHORITY_FOLDER = "Authority";
	// 配置文件目录
	public final static String APP_RES_CONFIGFILE_FOLDER = "Config";
	// 天气文件夹
	public final static String APP_RES_WEATHERIMAGE_FOLDER = "WeatherImage";
	// 播放日志文件夹
	public final static String APP_RES_LOGFILE_FOLDER = "Log";
	// 错误日志文件夹
	public final static String APP_RES_ERROR_LOGFILE_FOLDER = "ErrorLog";
	// 配置文件名
	public final static String APP_RES_CONFIGFILE = "coofiletouch.properties";
	// 心跳间隔时间
	public final static long HEARBEAT_INTERVAL_TIME = 2000;
	// socket中断后重连间隔时间
	public final static long SOCKET_INTERRUPT_RECONNECT_INTERVAL_TIME = 5000;
	// DownloadQueue下载列表为0时，等待等候间隔时间
	public final static long DOWNLOADQUEUE_INTERVAL_TIME = 2000;
	// USB发布根目录root directory
	public final static String USB_PUBLISH_ROOT_DIRECTORY = "/iis";
	// USB发布素材目录"/ftpFile/processed"
	public final static String USB_PUBLISH_PROCESSED = "/ftpFile/processed";
	// USB发布播放单目录 /ftpFile/programXmlFile
	public final static String USB_PUBLISH_XMLFOLDER = "/ftpFile/programXmlFile";
	// 日志上传间隔时间60分钟
	public final static long UPLOAD_PLAY_LOG_PERIOD = 2000;
	// 日志上传在ftp的文件夹 statistics
	public final static String LOG_FOLDER_IN_FTP = "statistics";

	// ///// 下面的暂时没有用
	public final static boolean IS_SUPPORT_TERMINAL_CONTROL = false;
	public final static boolean IS_SUPPORT_REMOTE_UPDATE = false;
	public final static boolean IS_SUPPORT_POWER_ON_OFF_ON_SCHEDULE = false;
	//
	public final static String APP_RES_COPY_TEMPLATE_ID = "_-copy-_";
	// First character lower case, same with the server
	public final static String APP_RES_UPDATEFILE_FOLDER = "updateFile";

	public final static String APP_RES_SCREENSHOT_FOLDER = "ScreenShot";
	public final static String APP_RES_UPDATE_APK_FOLDER = "UpdateFile";
	public final static String CHECK_AUTHORITY_SERVER_IP = "192.168.13.95";
	public final static int CHECK_AUTHORITY_SERVER_PORT = 60000;
	public final static long DISK_CLEAR_PERCENTAGE = 90L;
	public final static long DELAY_DESTROY_TIME = 500L;
	public final static long FETCH_SCROLL_MSG_INTERVAL = 5000L;
	public final static long FETCH_PLAY_TASK_INTERVAL = 5000L;
	public final static long CLEAR_UNUSED_MATERIAL_INTERVAL = 10 * 60 * 10 * 1000L;
	public final static long CHECK_VERSION_UPDATE_INTERVAL = 60 * 60 * 1000L;
	public final static long UPLOAD_CLIENT_STATUE_INTERVAL = 10 * 1000L;

}
