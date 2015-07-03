package com.twinflag.coofiletouch.filter;

import java.util.List;

import com.twinflag.coofiletouch.entity.MessageBean;

/**
 * @author wanghuayi 过滤比较帮助类
 */
final public class CompareUtil {

	/**
	 * 比较消息列表是否一致 true:一致，false：不一致
	 */
	public static boolean compareMessages(List<MessageBean> oldMessages, List<MessageBean> newMessages) {
		if (newMessages != null && newMessages.size() > 0) {
			if (oldMessages == null) {
				return false;
			}
			if (oldMessages.size() != newMessages.size()) {
				return false;
			} else {
				for (int a = 0; a < newMessages.size(); a++) {
					MessageBean oldMessage = oldMessages.get(a);
					MessageBean newMessage = newMessages.get(a);
					if (!compareMessage(oldMessage, newMessage)) {
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * 判断消息是否在任务列表中
	 */
	public static boolean isExistMessage(MessageBean message, List<MessageBean> newMessages) {
		if (newMessages != null && newMessages.size() > 0) {
			if (message == null) {
				return false;
			}
			for (int a = 0; a < newMessages.size(); a++) {
				MessageBean newMessage = newMessages.get(a);
				if (compareMessage(message, newMessage)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 比较消息是否一致
	 * 
	 * @param oldMessage
	 * @param newMessage
	 * @return true：一致；false：不一致
	 */
	private static boolean compareMessage(MessageBean oldMessage, MessageBean newMessage) {
		if (newMessage != null) {
			if (oldMessage == null) {
				return false;
			}
			if (!oldMessage.getId().equals(newMessage.getId())) {
				return false;
			}
			if (!oldMessage.getUpdateTime().equals(newMessage.getUpdateTime())) {
				return false;
			}
		}
		return true;
	}

}
