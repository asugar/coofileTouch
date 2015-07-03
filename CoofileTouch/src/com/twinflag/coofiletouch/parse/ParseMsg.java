package com.twinflag.coofiletouch.parse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import com.twinflag.coofiletouch.entity.MessageBean;
import com.twinflag.coofiletouch.showUtil.LogUtil;
import com.twinflag.coofiletouch.type.LogType;
import com.twinflag.coofiletouch.utils.IsNullUtil;

public class ParseMsg {
	public static String TAG = "parseMessage";

	public static MessageBean getMessage(String str) {
		InputStream is = new ByteArrayInputStream(str.getBytes());
		MessageBean msg = null;
		XmlPullParser parser = Xml.newPullParser();
		try {
			parser.setInput(is, "UTF-8");
			int event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				switch (event) {
					case XmlPullParser.START_DOCUMENT:
						break;
					case XmlPullParser.START_TAG:
						if ("runmsgdata".equals(parser.getName())) {
							msg = new MessageBean();
							msg.setId(IsNullUtil.stringIsNull(parser.getAttributeValue(null, "id")));
							msg.setSpeed(IsNullUtil.integerIsNull(parser.getAttributeValue(null, "speed")));
							msg.setPosition(IsNullUtil.integerIsNull(parser.getAttributeValue(null, "direction")));
							msg.setFont(IsNullUtil.stringIsNull(parser.getAttributeValue(null, "font")));
							msg.setSize(IsNullUtil.integerIsNull(parser.getAttributeValue(null, "size")));
							msg.setFgcolor(IsNullUtil.stringIsNull(parser.getAttributeValue(null, "fgcolor")));
							msg.setBgcolor(IsNullUtil.stringIsNull(parser.getAttributeValue(null, "bgcolor")));
							msg.setStartDate(IsNullUtil.stringIsNull(parser.getAttributeValue(null, "startdate")));
							msg.setStopDate(IsNullUtil.stringIsNull(parser.getAttributeValue(null, "stopdate")));
							msg.setContent(IsNullUtil.stringIsNull(parser.getAttributeValue(null, "content")));
							msg.setUpdateTime(IsNullUtil.stringIsNull(parser.getAttributeValue(null, "updatetime")));
						}
						break;
					case XmlPullParser.END_TAG:
						break;
				}
				event = parser.next();
			}
		}
		catch (XmlPullParserException e) {
			e.printStackTrace();
			LogUtil.printApointedLog(ParseMsg.class, "parser message error!!", LogType.error);
		}
		catch (IOException e) {
			LogUtil.printApointedLog(ParseMsg.class, "parser message error!!", LogType.error);
			e.printStackTrace();
		}
		finally {
			try {
				if (is != null) {
					is.close();
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		return msg;
	}

}
