package com.twinflag.coofiletouch.showUtil;

import java.util.List;

import android.util.Log;

import com.twinflag.coofiletouch.entity.ElementBean;
import com.twinflag.coofiletouch.entity.MessageBean;
import com.twinflag.coofiletouch.value.GlobalVariable;

public class GetNextUtil {

	/**
	 * 获取下一个滚动字幕
	 * 
	 * @return
	 */
	public synchronized static MessageBean getNextMessage(MessageBean msg) {

		try {
			if (GlobalVariable.globalMsgs.size() == 0) {
				return null;
			}

			if (msg == null) {
				return GlobalVariable.globalMsgs.get(0);
			} else {
				String msgId = msg.getId();
				for (int i = 0; i < GlobalVariable.globalMsgs.size(); i++) {
					if (msgId.equals(GlobalVariable.globalMsgs.get(i).getId())) {
						if (i != GlobalVariable.globalMsgs.size() - 1) {
							return GlobalVariable.globalMsgs.get(i + 1);
						} else {
							return GlobalVariable.globalMsgs.get(0);
						}
					}
				}
				Log.e("yi", "get next message error!!!");
				return GlobalVariable.globalMsgs.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("yi", "get next Message error!!!");
			return null;
		}

	}

	/**
	 * getNextElement 获取下一个元素，根据element的sortId查找下一个元素 sortId都是从0开始的
	 */
	public static ElementBean getNextElement(List<ElementBean> elements, ElementBean element) {
		try {
			if (element == null) {
				return elements.get(0);
			} else if (elements.indexOf(element) != elements.size() - 1) {
				return elements.get(elements.indexOf(element) + 1);
			} else if (elements.indexOf(element) == elements.size() - 1) {
				return elements.get(0);
			} else {
				Log.e("yi", "get next element error!!!");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("yi", "get next element elements is null error!!!");
			return null;
		}

	}

	/**
	 * getNextTemplate 获得下一张模板
	 * 
	 * @return TemplateBean
	 */
	// public synchronized static TemplateBean getNextTemplate(Integer SortId) {
	// TemplateBean tem = null;
	// if (ShowTemplate.playingTask == null) {
	// tem = GlobalVariable.globalTasks.get(0).getList().get(0);
	// ShowTemplate.playingTask = GlobalVariable.globalTasks.get(0);
	// // Log.i(TAG, "playingTask == null");
	// }
	// else {
	// int taskSize = GlobalVariable.globalTasks.size();
	// for (int j = 0; j < taskSize; j++) {
	// TaskBean task = GlobalVariable.globalTasks.get(j);
	// //
	// 先判断playingTask是不是最后一个，如果是，如果template也是task的最后一个，则返回第一个任务的第一个模板，否则返回下一个任务的第一个模板
	// if (task.getId().equals(ShowTemplate.playingTask.getId())) {
	// // Log.i(TAG, "task.getId().equals(playingTask.getId())");
	// // 判断任务是不是最后一个任务
	// if (j != taskSize - 1) {
	// // 不是最后一个任务
	// int templateSize = task.getList().size();
	// for (int i = 0; i < templateSize; i++) {
	// TemplateBean templateBean = task.getList().get(i);
	//
	// if (templateBean.getSortId().equals(SortId)) {
	//
	// if (i != templateSize - 1) {
	// // 不等于最后一个，返回下一个
	// tem = task.getList().get(i + 1);
	// ShowTemplate.playingTask = task;
	// return tem;
	// }
	// else {
	// // 返回下一个任务的第一个模板
	// tem = GlobalVariable.globalTasks.get(j + 1).getList().get(0);
	// ShowTemplate.playingTask = GlobalVariable.globalTasks.get(j + 1);
	// return tem;
	// }
	// }
	// }
	// }
	// else {
	// // 是最后一个任务
	// int templateSize = task.getList().size();
	// for (int i = 0; i < templateSize; i++) {
	// TemplateBean templateBean = task.getList().get(i);
	//
	// if (templateBean.getSortId().equals(SortId)) {
	//
	// if (i != templateSize - 1) {
	// // 不等于最后一个，返回下一个
	// tem = task.getList().get(i + 1);
	// ShowTemplate.playingTask = task;
	// return tem;
	// }
	// else {
	// // 返回第一个任务的第一个模板
	// tem = GlobalVariable.globalTasks.get(0).getList().get(0);
	// ShowTemplate.playingTask = GlobalVariable.globalTasks.get(0);
	// return tem;
	// }
	// }
	// }
	// }
	// }
	//
	// }
	//
	// if (tem == null) {
	// // 如果template所在的task移除了，则从新开始
	// // 数组越界的问题
	// // Log.i(TAG, "tem == null");
	// tem = GlobalVariable.globalTasks.get(0).getList().get(0);
	// ShowTemplate.playingTask = GlobalVariable.globalTasks.get(0);
	//
	// }
	//
	// }
	// return tem;
	// }

}
