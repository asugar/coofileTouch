package com.twinflag.coofiletouch.ftp;

/**
 * 下载状态
 * */
public class DownloadStatus {

	/**
	 * 响应内容
	 * */
	private String response;

	/**
	 * 下载是否成功
	 * */
	private boolean succeed;

	/**
	 * 无参构造器
	 * */
	public DownloadStatus() {

	}

	/**
	 * 构造器
	 * 
	 * @param res
	 *            响应数据
	 * */
	public DownloadStatus(String res) {
		this.response = res;
	}

	/**
	 * 构造器
	 * 
	 * @param suc
	 *            下载成功标志
	 * @param ti
	 *            下载完成时间
	 * @param res
	 *            下载完成或失败的响应
	 * */

	public DownloadStatus(boolean suc, String res) {
		this.succeed = suc;
		this.response = res;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public boolean isSucceed() {
		return succeed;
	}

	public void setSucceed(boolean succeed) {
		this.succeed = succeed;
	}

}
