package com.twinflag.coofiletouch.filter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.twinflag.coofiletouch.entity.MessageBean;
import com.twinflag.coofiletouch.entity.ProgramBean;
import com.twinflag.coofiletouch.showUtil.LogUtil;
import com.twinflag.coofiletouch.type.LogType;
import com.twinflag.coofiletouch.value.GlobalVariable;

/**
 * @author wanghuayi 过滤工具类
 */
public class FilterUtil {

	private static SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat mDataFormater = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat mTimeFormater = new SimpleDateFormat("HH:mm:ss");

	/**
	 * 过滤消息使用的方法
	 */
	public static List<MessageBean> filterMsg() {

		List<MessageBean> msgs = new ArrayList<MessageBean>();

		if (GlobalVariable.globalAllMsgs.size() == 0) {
			return null;
		}

		// 从全局列表中删除过期任务
		outer:
		{
			int position = 0;
			while (true) {
				if (GlobalVariable.globalAllMsgs.size() == 0) {
					return null;
				}
				inner:
				{
					for (int i = position; i < GlobalVariable.globalAllMsgs.size(); i++) {
						MessageBean msg = GlobalVariable.globalAllMsgs.get(i);
						if (isExpired(msg.getStopDate())) {
							position = i;
							GlobalVariable.messageRemove(msg.getId());
							break inner;
						}
					}
					break outer;
				}
			}
		}

		/**
		 * 从globalAllMessages中过滤出时间符合的任务列表
		 */
		for (MessageBean msg : GlobalVariable.globalAllMsgs) {
			if (checkDataAndTime(msg.getStartDate(), msg.getStopDate())) {
				msgs.add(msg);
			}
		}

		return msgs;

	}

	/**
	 * 过滤任务得到符合条件的任务数组
	 */
	public static ProgramBean filterTask() {
		ProgramBean program = null;
		try {
			if (GlobalVariable.globalAllPgs == null || GlobalVariable.globalAllPgs.size() == 0) {
				return null;
			}

			// 从全局列表中删除过期任务
			outer:
			{
				int position = 0;
				while (true) {
					if (GlobalVariable.globalAllPgs.size() == 0) {
						return null;
					}
					inner:
					{
						for (int i = position; i < GlobalVariable.globalAllPgs.size(); i++) {
							ProgramBean pg = GlobalVariable.globalAllPgs.get(i);
							// 删除过期的任务
							if (isExpired(pg.getStopDate())) {
								position = i;
								GlobalVariable.pgRemove(pg.getId());
								break inner;
							}
						}
						break outer;
					}
				}
			}

			for (ProgramBean pg : GlobalVariable.globalAllPgs) {
				// 判断是否在播放时间内，在播放时间里
				if (isPgInPlayPeriod(pg)) {
					if (program == null) {
						program = pg;
					} else {
						// 比较任务更新时间，谁最后发取谁
						if (pg.getUpdateTime().compareTo(program.getUpdateTime()) > 0) {
							program = pg;
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.printApointedLog(FilterUtil.class, "过滤出错", LogType.error);
		}

		return program;
	}

	/**
	 * 判断任务是否过期
	 * 
	 * @param stopDate
	 * @return true:过期;false:未过期
	 */
	public static boolean isExpired(String stopDate) {
		String date = mSimpleDateFormat.format(new Date());
		if (date.compareTo(stopDate) > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 比较两个时间，对于滚动消息就是判断是否在播放时间内
	 * 
	 * @return true:在符合时间里；false:不在符合时间里
	 */
	public static boolean checkDataAndTime(String startTime, String stopTime) {
		String time = mSimpleDateFormat.format(new Date());
		if (time.compareTo(startTime) >= 0 && time.compareTo(stopTime) <= 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 比较两个时间，对于滚动消息就是判断是否在播放时间内
	 * 
	 * @return true:在符合时间里；false:不在符合时间里
	 */
	public static boolean checkTime(String startTime, String stopTime) {
		String time = mTimeFormater.format(new Date());
		if (time.compareTo(startTime) >= 0 && time.compareTo(stopTime) <= 0) {
			return true;
		} else {
			return false;
		}
	}

	// 判断是否在播放的时间里
	public static boolean isPgInPlayPeriod(ProgramBean pg) {
		String startDate = pg.getStartDate();
		String stopDate = pg.getStopDate();

		String loopStartTime = pg.getLoopStartTime();

		String loopStopTime = pg.getLoopStopTime();
		String loopDesc = pg.getLoopdesc();

		switch (pg.getLoopType()) {
		// 表示无循环
			case 0:
				return checkDefault(startDate, stopDate, loopStartTime, loopStopTime);
				// 表示周循环
			case 2:
				if (checkWeekRecycle(loopDesc)) {
					// 和case1 一样了，
					return checkDefault(startDate, stopDate, loopStartTime, loopStopTime);
				} else {
					return false;
				}
				// 判断月循环
			case 3:
				if (checkDayRecycle(loopDesc)) {
					// 和case1 一样了，
					return checkDefault(startDate, stopDate, loopStartTime, loopStopTime);
				} else {
					return false;
				}
			default:
				return false;

		}

	}

	/**
	 * 每种情况都需要比较的一个方法
	 */
	private static boolean checkDefault(String startDate, String stopDate, String loopStartTime, String loopStopTime) {
		String[] start = startDate.split(" ");
		startDate = start[0];
		String startTime = start[1];
		String[] stop = stopDate.split(" ");
		stopDate = stop[0];
		String stopTime = stop[1];
		if (loopStartTime == null) {
			// 无日循环
			// 比较date，如果date一样，在比较checkTime，如果date不一样，直接return true；
			if (checkDate(startDate) > 0 && checkDate(stopDate) < 0) {
				return true;
			} else {
				return checkTime(startTime, stopTime);
			}

		} else {
			// 有日循环：首先比较日期，如果当前日期比开始日期大并且结束日期比当前日期大，按照loopTime播放，如果出startTime和loopStartTime
			// 比较date
			// 在播放时间内，还没有开始播放呢
			if (checkDate(startDate) < 0) {
				return false;
			} else if (checkDate(startDate) > 0) {
				if (checkDate(stopDate) < 0) {
					return checkTime(loopStartTime, loopStopTime);
				} else if (checkDate(stopDate) == 0) {
					if (stopTime.compareTo(loopStopTime) < 0) {
						loopStopTime = stopTime;
					}
					return checkTime(loopStartTime, loopStopTime);
				} else {
					// 不在播放时间内
					return false;
				}
			} else {
				if (startTime.compareTo(loopStartTime) > 0) {
					loopStartTime = startTime;
				}
				if (checkDate(stopDate) < 0) {
					return checkTime(loopStartTime, loopStopTime);
				} else if (checkDate(stopDate) == 0) {
					if (stopTime.compareTo(loopStopTime) < 0) {
						loopStopTime = stopTime;
					}
					return checkTime(loopStartTime, loopStopTime);
				} else {
					// 不在播放时间内
					return false;
				}

			}

		}
	}

	/**
	 * 比较日期
	 * 
	 * @param date
	 * @return 0:相等 ；-:当前时间小; +:当前时间大
	 */
	public static int checkDate(String date) {
		String currentDate = mDataFormater.format(new Date());
		return currentDate.compareTo(date);
	}

	/**
	 * 判断当天是否是符合的星期
	 * 
	 * @return true：在循环中；false：今天不在循环中
	 */
	public static boolean checkWeekRecycle(String loopDesc) {
		// 如果loopDesc为空，就返回true，权宜之计
		if (loopDesc == null) {
			return true;
		}
		String[] desc = loopDesc.split(",");
		String week = getWeekOfDate(new Date());
		for (int i = 0; i < desc.length; i++) {
			if (desc[i].trim().equals(week)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取当天是星期几
	 */
	public static String getWeekOfDate(Date dt) {
		String[] weekDays = { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return weekDays[w];
	}

	/**
	 * 判断当天是否是符合时间的月循环 1.获取今天 2.遍历比较
	 */
	public static boolean checkDayRecycle(String loopDesc) {

		int day = getDayOfDate(new Date());
		String[] desc = loopDesc.split(",");

		for (int i = 0; i < desc.length; i++) {
			if (day == Integer.valueOf(desc[i])) {
				return true;
			}
		}
		return false;
	}

	public static int getDayOfDate(Date df) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(df);
		return cal.get(Calendar.DAY_OF_MONTH);
	}

}
