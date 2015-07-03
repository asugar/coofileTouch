package com.twinflag.coofiletouch.value;

import java.util.ArrayList;
import java.util.List;

import com.twinflag.coofiletouch.entity.MessageBean;
import com.twinflag.coofiletouch.entity.ProgramBean;
import com.twinflag.coofiletouch.entity.TwinflagBean;
import com.twinflag.coofiletouch.show.ShowMsg;
import com.twinflag.coofiletouch.show.ShowProgram;

public class GlobalVariable {
	// 是否加密
	public static boolean isencrypt = false;
	
	// 显示任务的引用
	public static ShowProgram globalShowProgram;
	
	// 多个任务
	public static TwinflagBean globalTwinflag = new TwinflagBean();
	
	// 显示消息的引用
	public static ShowMsg globalShowMsg;
	// 全局的消息引用
	public static MessageBean globalMsg;

	// 所有消息列表
	public static List<MessageBean> globalAllMsgs = new ArrayList<MessageBean>();
	// 播放消息列表
	public static List<MessageBean> globalMsgs = new ArrayList<MessageBean>();

	// 所有任务列表
	public static List<ProgramBean> globalAllPgs = new ArrayList<ProgramBean>();
	// 播放任务
	public static ProgramBean globalProgram;

	public static synchronized boolean pgIsContain(ProgramBean program) {
		if (globalAllPgs.size() == 0)
			return false;
		for (ProgramBean t : globalAllPgs) {
			if (t.getId().equals(program.getId())) {
				return true;
			}
		}
		return false;
	}

	public static synchronized boolean pgIsContain(String programId) {
		if (globalAllPgs.size() == 0)
			return false;
		for (ProgramBean t : globalAllPgs) {
			if (t.getId().equals(programId)) {
				return true;
			}
		}
		return false;
	}

	public static synchronized boolean pgRemove(String taskId) {
		if (globalAllPgs.size() == 0)
			return false;
		;
		for (ProgramBean t : globalAllPgs) {
			if (t.getId().equals(taskId)) {
				globalAllPgs.remove(t);
				return true;
			}
		}
		return false;

	}

	public static synchronized boolean pgAdd(ProgramBean program) {
		boolean result = false;
		if (program != null) {
			globalAllPgs.add(program);
			result = true;
		}
		return result;
	}

	public static synchronized void pgClear() {
		if (globalAllPgs.size() == 0)
			return;
		globalAllPgs.clear();
	}

	public static synchronized boolean messageAdd(MessageBean message) {
		return globalAllMsgs.add(message);
	}

	public static synchronized boolean messageRemove(String messageId) {
		if (globalAllMsgs.size() == 0)
			return true;
		for (MessageBean message : globalAllMsgs) {
			if (messageId.equals(message.getId())) {
				return globalAllMsgs.remove(message);
			}
		}
		return false;
	}

	public static synchronized void messageClear() {
		globalAllMsgs.clear();
	}
}
