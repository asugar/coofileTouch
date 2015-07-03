package com.twinflag.coofiletouch.database;

/**
 * @author zhouyiran 存库的滚动消息实体类
 */
public class MsgData {

	// 消息id
	private String msgId;
	// 消息内容
	private String msgContent;
	// 消息更新时间
	private String updateDate;

	public MsgData() {
	}

	public MsgData(String msgId, String msgContent, String updateDate) {
		this.msgId = msgId;
		this.msgContent = msgContent;
		this.updateDate = updateDate;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
}
