package com.twinflag.coofiletouch.value;

public interface ErrorValue {

	String ERROR_01 = "请配置服务器的IP";
	String ERROR_02 = "服务器的IP不合法";
	String ERROR_03 = "请配置服务器的端口";
	String ERROR_04 = "请配置FTP服务器的IP";
	String ERROR_05 = "FTP服务器的IP不合法";
	String ERROR_06 = "请配置FTP服务器的端口";
	String ERROR_07 = "请配置FTP服务器的用户名";
	String ERROR_08 = "请配置FTP服务器的密码";
	String ERROR_09 = "请配置主机的IP";
	String ERROR_10 = "主机的IP不合法";
	String ERROR_11 = "请配置主机的端口";
	String ERROR_12 = "配置文件缺失";
	String ERROR_13 = "配置参数无效";

	String ERROR_MSG_ERROR_UNKNOWN = "下载出错";
	String ERROR_MSG_ERROR_NO_SUFFICENT_SPACE = "磁盘空间不足";
	String ERROR_MSG_ERROR_FILE_NOT_FOUND = "远程文件不存在";
	String ERROR_MSG_ERROR_IO_EXCEPTION = "读写磁盘出错";

}
