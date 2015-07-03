package com.twinflag.coofiletouch.database;

/**
 * @author zhouyiran 存库的节目单消息类
 */
public class ProgramData {

	// 任务id
	private String pgId;
	// 任务名字
	private String pgName;
	// 任务内容
	private String pgContent;
	// 任务下载标识（2：下载完毕；0：未下载好，需要重新下载；1：）
	private String stopflag;
	// 任务更新时间
	private String updateDate;
	// 任务下载完毕，插入数据库的时间，用来标记谁是最后一个发送的
	private String insertTime;

	public ProgramData() {
	}

	public ProgramData(String pgId, String pgName, String pgContent, String stopflag, String updateDate,
					String insertTime) {
		this.pgId = pgId;
		this.pgName = pgName;
		this.pgContent = pgContent;
		this.stopflag = stopflag;
		this.updateDate = updateDate;
		this.insertTime = insertTime;
	}

	public String getPgId() {
		return pgId;
	}

	public void setPgId(String pgId) {
		this.pgId = pgId;
	}

	public String getPgName() {
		return pgName;
	}

	public void setPgName(String pgName) {
		this.pgName = pgName;
	}

	public String getPgContent() {
		return pgContent;
	}

	public void setPgContent(String pgContent) {
		this.pgContent = pgContent;
	}

	public String getStopflag() {
		return stopflag;
	}

	public void setStopflag(String stopflag) {
		this.stopflag = stopflag;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getInsertTime() {
		return insertTime;
	}

	public void setInsertTime(String insertTime) {
		this.insertTime = insertTime;
	}

}
