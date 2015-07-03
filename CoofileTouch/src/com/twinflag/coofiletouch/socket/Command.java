package com.twinflag.coofiletouch.socket;

public interface Command {

	String HEARTBEAT = "heartbeat";// 心跳
	String LOGIN = "login";// 登陆成功
	String HISTORYTASK = "historyTask";// 历史任务
	String HISTORYMESSAGE = "historyMessage";// 历史消息
	String PUBLISHTASK = "publishTask";// 发布任务
	String PUBLISHMESSAGE = "publishMessage";// 发布消息
	String STOPTASK = "stopTask";// 停止任务
	String DELETETASK = "deleteTask";// 删除任务
	String STOPMESSAGE = "stopMessage";// 停止消息
	String DELETEMESSAGE = "deleteMessage";// 删除消息
	String PARTUPDATE = "partupdate";// 局部更新
	String PREDOWNLOADTIME = "preDownloadTime";// 定时下载
	String SETVOLUME = "setvolume";// 设置音量
	String UPDATEAPP = "updateAPP";// 更新版本
	String SHUTDOWN = "shutdown";// 关机
	String REBOOT = "reboot";// 重新启动
	String TIMEDPOWER = "autopower";// 定时关机和取消设置，服务端的定义有歧义
	String GETVOLUME = "getvolume";// 获取音量
	String SYNCTIME = "synctime";// 校准系统时间
	String REQUESTTASK = "requestTask";// 请求任务
	String REQUESTMESSAGE = "requestMessage";// 请求消息
	String UPDATE = "update";// 检测版本更新
	String TASKDOWNLOAD = "taskdownload";// 下载任务
	String CLIENTSTATUS = "clientstatus";// 客户端状态信息

}


