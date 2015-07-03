package com.twinflag.coofiletouch.value;

public interface ReceiverAction {

	// 开机自启动
	String BOOT_COMPLETED_ACTION = "android.intent.action.BOOT_COMPLETED";
	// usb装载
	String USB_REMOVED_ACTION = "android.intent.action.MEDIA_REMOVED";
	// usb卸载
	String USB_MOUNTED_ACTION = "android.intent.action.MEDIA_MOUNTED";
//////////////////////////////////////////////////////////////////////////////////	
	
	
	String SYNC_MESSAGE_ACTION = "com.twinflag.coofile.receiver.SYNC_MESSAGE";
	String SYNC_TEMPLATE_ACTION = "com.twinflag.coofile.receiver.SYNC_TEMPLATE";
	String UPDATE_REGION_ACTION = "com.twinflag.coofile.receiver.UPDATE_REGION";

}
