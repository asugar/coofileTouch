package com.twinflag.coofiletouch.value;

public class OptionsBean {

	public String server_ip;// 服务器的IP（必填）
	public int server_port;// 服务器的端口（必填）
	public String ftp_ip;// FTP服务器的IP（必填）
	public int ftp_port;// FTP服务器的端口（必填）
	public String ftp_username;// FTP用户名（必填）
	public String ftp_password;// FTP密码（必填）
	
	public String weatherurl;// 天气地址（非必填）
	public boolean isencrypt;// 是否对资源解码 （非必填）

}
