package com.twinflag.coofiletouch.database;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author zhouyiran
 * @version 1.0 关于数据库操作的一些方法
 * */
public class MyDbHelper {

	// 任务表中的列名
	public static final String PG_TABLE = "program";
	public static final String PG_ID = "id";
	public static final String PG_NAME = "name";
	public static final String PG_CONTENT = "content";
	public static final String PG_STOPFLAG = "stopflag";
	public static final String PG_UPDATEDATE = "updateDate";
	public static final String PG_INSETTIME = "insertTime";

	// 滚动消息列表的列名
	public static final String MSG_TABLE = "message";
	public static final String MSG_ID = "id";
	public static final String MSG_CONTENT = "content";
	public static final String MSG_UPDATEDATE = "updateDate";

	// 定时下载表的名称
	public static final String TIMED_DOWNLOAD_TABLE = "t_timed_download";
	public static final String TIMED_DOWNLOAD_TERNIMAL_ID = "terminal_id";
	public static final String TIMED_DOWNLOAD_CONTENT = "timed_download_content";

	// 数据库创建表
	public static final String PG_SQL = "CREATE TABLE " + PG_TABLE + " ( " + PG_ID + " TEXT NOT NULL, " + PG_NAME
					+ " TEXT NOT NULL, " + PG_CONTENT + " TEXT NOT NULL, " + PG_STOPFLAG + " TEXT NOT NULL, "
					+ PG_UPDATEDATE + " TEXT NOT NULL, " + PG_INSETTIME + " );";
	public static final String MSG_SQL = "CREATE TABLE " + MSG_TABLE + " ( " + MSG_ID + " TEXT NOT NULL, "
					+ MSG_CONTENT + " TEXT NOT NULL, " + MSG_UPDATEDATE + " TEXT NOT NULL" + " );";
	public static final String TIMED_DOWNLAOD_SQL = "CREATE TABLE " + TIMED_DOWNLOAD_TABLE + " ( "
					+ TIMED_DOWNLOAD_TERNIMAL_ID + " TEXT NOT NULL, " + TIMED_DOWNLOAD_CONTENT + " TEXT NOT NULL"
					+ " );";

	// 数据库名称
	private static final String DATABASE_NAME = "coofiletouch.db3";
	private static final int DATABASE_VERSION = 2;
	public static Context mContext;
	public SQLiteDatabase mDatabase = null;
	public static DbHelper mDbHelper = null;

	public MyDbHelper(Context context) {
		mContext = context;
	}

	public static DbHelper getSingleInstance() {
		if (mDbHelper == null) {
			mDbHelper = new DbHelper(mContext);
		}
		return mDbHelper;
	}

	public static class DbHelper extends SQLiteOpenHelper {
		public DbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(PG_SQL);
			db.execSQL(MSG_SQL);
			db.execSQL(TIMED_DOWNLAOD_SQL);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + PG_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + MSG_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + TIMED_DOWNLOAD_TABLE);
			onCreate(db);
		}
	}

	// 打开数据库
	public synchronized MyDbHelper open() {
		mDbHelper = getSingleInstance();
		mDatabase = mDbHelper.getWritableDatabase();
		return this;
	}

	// 关闭数据库
	public static void close() {
		if (mDbHelper != null) {
			mDbHelper.close();
		}
	}

	/**
	 * 获得数据库中的所有任务
	 * */
	public synchronized List<ProgramData> getFullTasks() {
		List<ProgramData> list = new ArrayList<ProgramData>();
		String sql = "SELECT * from " + PG_TABLE;
		Cursor cursor = mDatabase.rawQuery(sql, new String[] {});
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			String taskId = cursor.getString(cursor.getColumnIndex(PG_ID));
			String taskName = cursor.getString(cursor.getColumnIndex(PG_NAME));
			String taskContent = cursor.getString(cursor.getColumnIndex(PG_CONTENT));
			String stopflag = cursor.getString(cursor.getColumnIndex(PG_STOPFLAG));
			String updateDate = cursor.getString(cursor.getColumnIndex(PG_UPDATEDATE));
			String insertTime = cursor.getString(cursor.getColumnIndex(PG_INSETTIME));
			list.add(new ProgramData(taskId, taskName, taskContent, stopflag, updateDate, insertTime));
		}
		cursor.close();
		return list;
	}

	/**
	 * 获得未下载完成的任务
	 * */
	public synchronized List<ProgramData> getUnfinishTask() {
		List<ProgramData> list = new ArrayList<ProgramData>();
		String sql = "SELECT * from " + PG_TABLE + " WHERE " + PG_STOPFLAG + " = 0";
		Cursor cursor = mDatabase.rawQuery(sql, new String[] {});
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			String taskId = cursor.getString(cursor.getColumnIndex(PG_ID));
			String taskName = cursor.getString(cursor.getColumnIndex(PG_NAME));
			String taskContent = cursor.getString(cursor.getColumnIndex(PG_CONTENT));
			String stopflag = cursor.getString(cursor.getColumnIndex(PG_STOPFLAG));
			String updateDate = cursor.getString(cursor.getColumnIndex(PG_UPDATEDATE));
			String insertTime = cursor.getString(cursor.getColumnIndex(PG_INSETTIME));
			list.add(new ProgramData(taskId, taskName, taskContent, stopflag, updateDate, insertTime));
		}
		cursor.close();
		return list;
	}

	public synchronized List<ProgramData> getUsefulTasks() {
		List<ProgramData> list = new ArrayList<ProgramData>();
		String sql = "SELECT * from " + PG_TABLE + " WHERE " + PG_STOPFLAG + " = 2";
		Cursor cursor = mDatabase.rawQuery(sql, new String[] {});
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			String taskId = cursor.getString(cursor.getColumnIndex(PG_ID));
			String taskName = cursor.getString(cursor.getColumnIndex(PG_NAME));
			String taskContent = cursor.getString(cursor.getColumnIndex(PG_CONTENT));
			String stopflag = cursor.getString(cursor.getColumnIndex(PG_STOPFLAG));
			String updateDate = cursor.getString(cursor.getColumnIndex(PG_UPDATEDATE));
			String insertTime = cursor.getString(cursor.getColumnIndex(PG_INSETTIME));
			list.add(new ProgramData(taskId, taskName, taskContent, stopflag, updateDate, insertTime));
		}
		cursor.close();
		return list;
	}

	/**
	 * 获得所有的滚动消息
	 * */
	public synchronized List<MsgData> getFullMessage() {
		List<MsgData> list = new ArrayList<MsgData>();
		Cursor cursor = mDatabase.rawQuery("SELECT * from " + MSG_TABLE, new String[] {});
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			String msgId = cursor.getString(cursor.getColumnIndex(MSG_ID));
			String msg = cursor.getString(cursor.getColumnIndex(MSG_CONTENT));
			String updateDate = cursor.getString(cursor.getColumnIndex(MSG_UPDATEDATE));
			list.add(new MsgData(msgId, msg, updateDate));
		}
		cursor.close();
		return list;
	}

	/**
	 * 根据Id查找任务 PG_TABLE = "t_tasks";
	 * */
	public synchronized ProgramData getTaskById(String id) {
		ProgramData mTaskBean = new ProgramData();
		String sql = "SELECT " + PG_ID + "," + PG_NAME + "," + PG_CONTENT + "," + PG_STOPFLAG + "," + PG_UPDATEDATE
						+ " from " + PG_TABLE + " WHERE " + PG_ID + " = " + id;
		Cursor cursor = mDatabase.rawQuery(sql, new String[] {});
		boolean flag = cursor.moveToFirst();
		if (flag) {
			String taskId = cursor.getString(cursor.getColumnIndex(PG_ID));
			String taskName = cursor.getString(cursor.getColumnIndex(PG_NAME));
			String taskContent = cursor.getString(cursor.getColumnIndex(PG_CONTENT));
			String stopflag = cursor.getString(cursor.getColumnIndex(PG_STOPFLAG));
			String updateDate = cursor.getString(cursor.getColumnIndex(PG_UPDATEDATE));
			String insertTime = cursor.getString(cursor.getColumnIndex(PG_INSETTIME));
			mTaskBean = new ProgramData(taskId, taskName, taskContent, stopflag, updateDate, insertTime);
		}
		cursor.close();
		return mTaskBean;
	}

	// 根据ID查找消息
	public synchronized MsgData getMessageById(String id) {
		MsgData msg = new MsgData();
		String sql = "SELECT " + MSG_ID + "," + MSG_CONTENT + "," + MSG_UPDATEDATE + " from " + MSG_TABLE + " WHERE "
						+ MSG_ID + " = " + id;
		Cursor cursor = mDatabase.rawQuery(sql, new String[] {});
		boolean flag = cursor.moveToFirst();
		if (flag) {
			msg.setMsgId(cursor.getString(cursor.getColumnIndex(MSG_ID)));
			msg.setMsgContent(cursor.getString(cursor.getColumnIndex(MSG_CONTENT)));
			msg.setUpdateDate(cursor.getString(cursor.getColumnIndex(MSG_UPDATEDATE)));
		} else {
			msg = null;
		}
		cursor.close();
		return msg;
	}

	/**
	 * 插入一个条消息
	 * */
	public void insertMessage(String msgid, String msgcontent, String updateTime) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(MSG_ID, msgid);
		contentValues.put(MSG_CONTENT, msgcontent);
		contentValues.put(MSG_UPDATEDATE, updateTime);
		mDatabase.insert(MSG_TABLE, null, contentValues);
	}

	/**
	 * 删除一条消息
	 * */
	public void deleteMessage(String msgid) {
		mDatabase.delete(MSG_TABLE, MSG_ID + "=?", new String[] { msgid });
	}

	/**
	 * 删除一条任务记录
	 * */
	public void deleteTask(String taskid) {
		mDatabase.delete(PG_TABLE, PG_ID + "=?", new String[] { taskid });
	}

	public void insertTask(String taskid, String taskname, String taskcontent, String taskstopflag, String task_update,
					String insertTime) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(PG_ID, taskid);
		contentValues.put(PG_NAME, taskname);
		contentValues.put(PG_CONTENT, taskcontent);
		contentValues.put(PG_STOPFLAG, taskstopflag);
		contentValues.put(PG_UPDATEDATE, task_update);
		contentValues.put(PG_INSETTIME, insertTime);
		mDatabase.insert(PG_TABLE, null, contentValues);
	}

	public void updateTask(String stopflag, String taskid) {
		ContentValues values = new ContentValues();
		values.put(PG_STOPFLAG, stopflag);
		mDatabase.update(PG_TABLE, values, PG_ID + "=?", new String[] { taskid });
	}

	/**
	 * 更新任务暂停标志
	 * */
	public synchronized void executeSQL(String sql) {
		mDatabase.execSQL(sql);
	}

	/**
	 * 获取元素播放的结束时间
	 * */
	public synchronized String getLogDate(String date, Integer durtime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			long start = sdf.parse(date).getTime();
			long end = start + durtime * 1000;
			Date date1 = new Date(end);
			String str = sdf.format(date1);
			return str;

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 删除所有的任务
	 * */
	public void deleteAllTask() {
		String sql = "delete from " + PG_TABLE;
		mDatabase.execSQL(sql);
	}

	/**
	 * 删除所有的消息
	 * */
	public void deleteAllMessage() {
		String sql = "delete from " + MSG_TABLE;
		mDatabase.execSQL(sql);
	}

	// 定时下载部分（暂时无用）
	public synchronized String getTimedDownloadInfo() {
		String timedDownloadInfo = null;
		String sql = "SELECT * from " + TIMED_DOWNLOAD_TABLE;
		Cursor cursor = mDatabase.rawQuery(sql, new String[] {});
		// 紧取第一条记录；
		if (cursor != null && !cursor.isAfterLast()) {
			cursor.moveToFirst();
			timedDownloadInfo = cursor.getString(cursor.getColumnIndex(TIMED_DOWNLOAD_CONTENT));
			cursor.close();
		}
		return timedDownloadInfo;
	}

	public void deleteAllTimedDownloadInfo() {
		String sql = "delete from " + TIMED_DOWNLOAD_TABLE;
		mDatabase.execSQL(sql);
	}

	public void insertTimedDownloadInfo(String terminalId, String content) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(TIMED_DOWNLOAD_TERNIMAL_ID, terminalId);
		contentValues.put(TIMED_DOWNLOAD_CONTENT, content);
		mDatabase.insert(TIMED_DOWNLOAD_TABLE, null, contentValues);
	}

}
