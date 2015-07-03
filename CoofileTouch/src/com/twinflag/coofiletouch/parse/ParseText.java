package com.twinflag.coofiletouch.parse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

import com.twinflag.coofiletouch.CoofileTouchApplication;
import com.twinflag.coofiletouch.entity.ElementBean;
import com.twinflag.coofiletouch.utils.IsNullUtil;

public class ParseText {

	public static ElementBean getText(ElementBean element) {
		InputStream is = null;
		// 路径问题没有解决
		File file = new File(CoofileTouchApplication.getAppResBasePath() + element.getSrc());
		try {
			is = new FileInputStream(file);
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(is, "UTF-8");
			int event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				switch (event) {
					case XmlPullParser.START_TAG:
						if ("text".equals(parser.getName())) {
							int lenght = 0;
							String bgcolor = IsNullUtil.stringIsNull(parser.getAttributeValue(null, "bgcolor"));
							lenght = bgcolor.length();
							element.setBgcolor(bgcolor.substring(lenght - 7, lenght));
							String fgcolor = IsNullUtil.stringIsNull(parser.getAttributeValue(null, "fgcolor"));
							lenght = fgcolor.length();
							element.setFgcolor(fgcolor.substring(lenght - 7, lenght));
							element.setContent(IsNullUtil.stringIsNull(parser.getAttributeValue(null, "content")));
							element.setFont(IsNullUtil.stringIsNull(parser.getAttributeValue(null, "font")));
							element.setSize(IsNullUtil.integerIsNull(parser.getAttributeValue(null, "size")));
						}
						break;
					case XmlPullParser.END_TAG:
						if ("text".equals(parser.getName())) {

						}
						break;
				}
				event = parser.next();
			}
		}
		catch (Exception e) {
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
		return element;
	}

}
