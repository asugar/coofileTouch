package com.twinflag.coofiletouch.parse;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.text.TextUtils;
import android.util.Xml;

import com.twinflag.coofiletouch.entity.AlbumBean;
import com.twinflag.coofiletouch.entity.ButtonBean;
import com.twinflag.coofiletouch.entity.ClockBean;
import com.twinflag.coofiletouch.entity.ElementBean;
import com.twinflag.coofiletouch.entity.ItemBean;
import com.twinflag.coofiletouch.entity.PicBean;
import com.twinflag.coofiletouch.entity.PlugBean;
import com.twinflag.coofiletouch.entity.ProgramBean;
import com.twinflag.coofiletouch.entity.RegionBean;
import com.twinflag.coofiletouch.entity.SceneBean;
import com.twinflag.coofiletouch.entity.TimepieceBean;
import com.twinflag.coofiletouch.entity.TouchVideoBean;
import com.twinflag.coofiletouch.entity.TwinflagBean;
import com.twinflag.coofiletouch.entity.WeatherBean;
import com.twinflag.coofiletouch.showUtil.LogUtil;
import com.twinflag.coofiletouch.type.ElementType;
import com.twinflag.coofiletouch.type.LogType;
import com.twinflag.coofiletouch.utils.IsNullUtil;

public class ParseProgram {

//	public static ProgramBean getProgram(String str) {
	public static TwinflagBean getProgram(String str) {

		InputStream is = new ByteArrayInputStream(str.getBytes());
		//---------new add begin
		TwinflagBean twinflag = null;
		List<ProgramBean> programs = null;
		//---------new add end
		ProgramBean program = null;
		List<SceneBean> scenes = null;
		SceneBean scene = null;
		List<RegionBean> regions = null;
		List<ButtonBean> buttons = null;
		RegionBean region = null;
		List<ElementBean> elements = null;
		ElementBean element = null;
		ButtonBean button = null;
		List<PlugBean> plugs = null;// 插件
		PlugBean plug = null;
		AlbumBean album = null;// 图片互动插件
		WeatherBean weather = null;// 天气插件
		TouchVideoBean touchvideo = null;// 互动视频插件
		TimepieceBean timepiece = null;
		ClockBean clock = null;
		List<ItemBean> items = null;
		ItemBean item = null;
		PicBean pic = null;

		XmlPullParser parser = Xml.newPullParser();
		try {
			parser.setInput(is, "utf-8");
			int event = parser.getEventType();

			while (event != XmlPullParser.END_DOCUMENT) {
				switch (event) {
					case XmlPullParser.START_DOCUMENT:
						break;
					case XmlPullParser.START_TAG:
						//---------new add begin
						if("twinflag".equals(parser.getName())){
							twinflag = new TwinflagBean();
//							twinflag.setId(IsNullUtil.stringIsNull(parser.getAttributeValue(null, "id")));
//							twinflag.setName(IsNullUtil.stringIsNull(parser.getAttributeValue(null, "name")));
							twinflag.setWidth(IsNullUtil.integerIsNull(parser.getAttributeValue(null, "width")));
							twinflag.setHeight(IsNullUtil.integerIsNull(parser.getAttributeValue(null, "height")));
//							twinflag.setLoopType(IsNullUtil.integerIsNull(parser.getAttributeValue(null, "looptype")));
//							twinflag.setLoopdesc(IsNullUtil.stringIsNull(parser.getAttributeValue(null, "loopdesc")));
//							twinflag.setLoopStartTime(IsNullUtil.stringIsNull(parser.getAttributeValue(null,
//											"loopstarttime")));
//							twinflag.setLoopStopTime(IsNullUtil.stringIsNull(parser.getAttributeValue(null,
//											"loopstoptime")));
//							twinflag.setStartDate(IsNullUtil.stringIsNull(parser.getAttributeValue(null, "startdate")));
//							twinflag.setStopDate(IsNullUtil.stringIsNull(parser.getAttributeValue(null, "stopdate")));
//							twinflag.setUpdateTime(IsNullUtil.stringIsNull(parser.getAttributeValue(null, "updatetime")));
//							// 需要修改
//							String publishtime = IsNullUtil.stringIsNull(parser.getAttributeValue(null, "updatetime"));
//							if (publishtime != null) {
//								twinflag.setUpdateTime(publishtime);
//							}

							programs = new ArrayList<ProgramBean>();
						}
						//---------new add end
						if ("program".equals(parser.getName())) {
							program = new ProgramBean();
							program.setId(IsNullUtil.stringIsNull(parser.getAttributeValue(null, "id")));
							program.setName(IsNullUtil.stringIsNull(parser.getAttributeValue(null, "name")));
							program.setWidth(IsNullUtil.integerIsNull(parser.getAttributeValue(null, "width")));
							program.setHeight(IsNullUtil.integerIsNull(parser.getAttributeValue(null, "height")));
							program.setLoopType(IsNullUtil.integerIsNull(parser.getAttributeValue(null, "looptype")));
							program.setLoopdesc(IsNullUtil.stringIsNull(parser.getAttributeValue(null, "loopdesc")));
							program.setLoopStartTime(IsNullUtil.stringIsNull(parser.getAttributeValue(null,
											"loopstarttime")));
							program.setLoopStopTime(IsNullUtil.stringIsNull(parser.getAttributeValue(null,
											"loopstoptime")));
							program.setStartDate(IsNullUtil.stringIsNull(parser.getAttributeValue(null, "startdate")));
							program.setStopDate(IsNullUtil.stringIsNull(parser.getAttributeValue(null, "stopdate")));
							program.setUpdateTime(IsNullUtil.stringIsNull(parser.getAttributeValue(null, "updatetime")));
							// 需要修改
							String publishtime = IsNullUtil.stringIsNull(parser.getAttributeValue(null, "updatetime"));
							if (publishtime != null) {
								program.setUpdateTime(publishtime);
							}

							scenes = new ArrayList<SceneBean>();
						}
						if ("scene".equals(parser.getName())) {
							scene = new SceneBean();
							scene.setId(IsNullUtil.stringIsNull(parser.getAttributeValue(null, "id")));
							scene.setName(IsNullUtil.stringIsNull(parser.getAttributeValue(null, "sceneName")));
							String backimg = IsNullUtil.stringIsNull(parser.getAttributeValue(null, "backimg"));
							scene.setBackimg(backimg == null ? null : backimg.replace("\\", "/").substring(7));
							scene.setBackcolor(IsNullUtil.stringIsNull(parser.getAttributeValue(null, "backcolor")));
							scene.setIsmain(IsNullUtil.booleanIsNull(parser.getAttributeValue(null, "ismain")));
							regions = new ArrayList<RegionBean>();
							buttons = new ArrayList<ButtonBean>();
							plugs = new ArrayList<PlugBean>();
						}
						if ("region".equals(parser.getName())) {
							region = new RegionBean();
							region.setId(IsNullUtil.stringIsNull(parser.getAttributeValue(null, "id")));
							region.setName(IsNullUtil.stringIsNull(parser.getAttributeValue(null, "name")));
							region.setType(IsNullUtil.integerIsNull(parser.getAttributeValue(null, "type")));
							region.setTop(IsNullUtil.integerIsNull(parser.getAttributeValue(null, "top")));
							region.setLeft(IsNullUtil.integerIsNull(parser.getAttributeValue(null, "left")));
							region.setWidth(IsNullUtil.integerIsNull(parser.getAttributeValue(null, "width")));
							region.setHeight(IsNullUtil.integerIsNull(parser.getAttributeValue(null, "height")));
							elements = new ArrayList<ElementBean>();

						}
						if ("element".equals(parser.getName())) {
							element = new ElementBean();
							element.setId(IsNullUtil.stringIsNull(parser.getAttributeValue(null, "id")));
							element.setName(IsNullUtil.stringIsNull(parser.getAttributeValue(null, "name")));
							element.setLife(IsNullUtil.integerIsNull(parser.getAttributeValue(null, "life")));
							String src = IsNullUtil.stringIsNull(parser.getAttributeValue(null, "src"));
							element.setSrc(src == null ? null : src.replace("\\", "/").substring(7));
							element.setWebSrc(IsNullUtil.stringIsNull(parser.getAttributeValue(null, "websrc")));
							element.setSceneid(IsNullUtil.integerIsNull(parser.getAttributeValue(null, "sceneid")));
							element.setType(IsNullUtil.integerIsNull(parser.getAttributeValue(null, "type")));
						}
						if ("item".equals(parser.getName())) {
							item = new ItemBean();
							item.setColor(IsNullUtil.stringIsNull(parser.getAttributeValue(null, "color")));
							item.setFont(IsNullUtil.stringIsNull(parser.getAttributeValue(null, "font")));
							item.setFontsize(IsNullUtil.integerIsNull(parser.getAttributeValue(null, "fontsize")));
							item.setLeft(IsNullUtil.integerIsNull(parser.getAttributeValue(null, "left")));
							item.setTop(IsNullUtil.integerIsNull(parser.getAttributeValue(null, "top")));
							item.setType(IsNullUtil.integerIsNull(parser.getAttributeValue(null, "type")));
							item.setContent(IsNullUtil.stringIsNull(parser.getAttributeValue(null, "content")));
						}
						if ("pic".equals(parser.getName())) {
							pic = new PicBean();
							pic.setHeight(IsNullUtil.integerIsNull(parser.getAttributeValue(null, "height")));
							pic.setLeft(IsNullUtil.integerIsNull(parser.getAttributeValue(null, "left")));
							pic.setTop(IsNullUtil.integerIsNull(parser.getAttributeValue(null, "top")));
							pic.setWidth(IsNullUtil.integerIsNull(parser.getAttributeValue(null, "width")));
							pic.setSrc(IsNullUtil.stringIsNull(parser.getAttributeValue(null, "src")));
						}
						if ("button".equals(parser.getName())) {
							button = new ButtonBean();
							button.setId(IsNullUtil.stringIsNull(parser.getAttributeValue(null, "id")));
							button.setName(IsNullUtil.stringIsNull(parser.getAttributeValue(null, "name")));
							button.setType(IsNullUtil.integerIsNull(parser.getAttributeValue(null, "type")));
							button.setTop(IsNullUtil.integerIsNull(parser.getAttributeValue(null, "top")));
							button.setLeft(IsNullUtil.integerIsNull(parser.getAttributeValue(null, "left")));
							button.setWidth(IsNullUtil.integerIsNull(parser.getAttributeValue(null, "width")));
							button.setHeight(IsNullUtil.integerIsNull(parser.getAttributeValue(null, "height")));
							String src = IsNullUtil.stringIsNull(parser.getAttributeValue(null, "src"));
							button.setBackImg((src == null) ? null : src.replace("\\", "/").substring(7));
							button.setValue(IsNullUtil.stringIsNull(parser.getAttributeValue(null, "value")));
							button.setSceneid(IsNullUtil.stringIsNull(parser.getAttributeValue(null, "sceneid")));
						}
						if ("plug".equals(parser.getName())) {
							plug = new PlugBean();
							plug.setId(IsNullUtil.stringIsNull(parser.getAttributeValue(null, "id")));
							plug.setName(IsNullUtil.stringIsNull(parser.getAttributeValue(null, "name")));
							plug.setType(IsNullUtil.integerIsNull(parser.getAttributeValue(null, "type")));
							plug.setTop(IsNullUtil.integerIsNull(parser.getAttributeValue(null, "top")));
							plug.setLeft(IsNullUtil.integerIsNull(parser.getAttributeValue(null, "left")));
							plug.setWidth(IsNullUtil.integerIsNull(parser.getAttributeValue(null, "width")));
							plug.setHeight(IsNullUtil.integerIsNull(parser.getAttributeValue(null, "height")));
						}
						if ("album".equals(parser.getName())) {
							album = new AlbumBean();
							album.setStyle(IsNullUtil.integerIsNull(parser.getAttributeValue(null, "style")));
							elements = new ArrayList<ElementBean>();
						}
						if ("weather".equals(parser.getName())) {
							weather = new WeatherBean();
							weather.setStyle(IsNullUtil.integerIsNull(parser.getAttributeValue(null, "style")));
							items = new ArrayList<ItemBean>();
						}
						if ("timepiece".equals(parser.getName())) {
							timepiece = new TimepieceBean();
							timepiece.setStyle(IsNullUtil.integerIsNull(parser.getAttributeValue(null, "style")));
						}
						if ("touchvideo".equals(parser.getName())) {
							touchvideo = new TouchVideoBean();
							touchvideo.setStyle(IsNullUtil.integerIsNull(parser.getAttributeValue(null, "style")));
							elements = new ArrayList<ElementBean>();
						}
						if ("clock".equals(parser.getName())) {
							clock = new ClockBean();
							clock.setTop(IsNullUtil.integerIsNull(parser.getAttributeValue(null, "top")));
							clock.setLeft(IsNullUtil.integerIsNull(parser.getAttributeValue(null, "left")));
							clock.setWidth(IsNullUtil.integerIsNull(parser.getAttributeValue(null, "width")));
							clock.setHeight(IsNullUtil.integerIsNull(parser.getAttributeValue(null, "height")));
							clock.setSize(IsNullUtil.integerIsNull(parser.getAttributeValue(null, "size")));
							clock.setFont(IsNullUtil.stringIsNull(parser.getAttributeValue(null, "font")));
							clock.setBgcolor(IsNullUtil.stringIsNull(parser.getAttributeValue(null, "bgcolor")));
							clock.setFgcolor(IsNullUtil.stringIsNull(parser.getAttributeValue(null, "fgcolor")));
							clock.setFormat(IsNullUtil.stringIsNull(parser.getAttributeValue(null, "format")));
						}

						break;
					case XmlPullParser.END_TAG:
						if ("element".equals(parser.getName())) {
							if (element.getType() == ElementType.office) {
								int _pagelife = element.getLife();
								String _src = element.getSrc();
								String _FolderPath = _src.substring(0, _src.indexOf('.'));
								int _start = _FolderPath.indexOf('/', 1);
								int _end = _FolderPath.indexOf("_");
								String _pageNum = _FolderPath.substring(_start + 1, _end);
								int pageNum = Integer.parseInt(_pageNum);
								ElementBean _clone = null;
								for (int i = 1; i <= pageNum; i++) {
									try {
										_clone = element.clone();
										_clone.setLife(_pagelife);
										_clone.setType(3);
										_clone.setId(String.valueOf(i));
										_clone.setSrc(_FolderPath + File.separator + pageNum + "_" + i + ".jpg");
										_clone.setRegion(region);
										element.setPlug(plug);
										elements.add(_clone);
									} catch (CloneNotSupportedException e) {
										e.printStackTrace();
									}
								}

							} else {
								element.setRegion(region);
								element.setPlug(plug);
								elements.add(element);
							}
						}
						if ("region".equals(parser.getName())) {
							// 如果区域为空区域，则不再对该区域处理，直接放弃
							if (elements.size() > 0) {
								region.setElements(elements);
								regions.add(region);
							}
						}
						if ("button".equals(parser.getName())) {
							buttons.add(button);
						}
						if ("item".equals(parser.getName())) {
							items.add(item);
						}
						if ("pic".equals(parser.getName())) {
							weather.setPic(pic);
						}
						if ("weather".equals(parser.getName())) {
							weather.setItems(items);
							plug.setWeather(weather);
						}
						if ("timepiece".equals(parser.getName())) {
							timepiece.setClock(clock);
							plug.setTimepiece(timepiece);
						}
						if ("touchvideo".equals(parser.getName())) {
							touchvideo.setElements(elements);
							plug.setTouchvideo(touchvideo);
						}
						if ("album".equals(parser.getName())) {
							album.setElements(elements);
							plug.setAlbum(album);
						}
						if ("plug".equals(parser.getName())) {
							if (weather != null || (album != null && album.getElements().size() > 0) || clock != null
											|| touchvideo != null) {
								plugs.add(plug);
							}
						}
						if ("scene".equals(parser.getName())) {
							scene.setRegions(regions);
							scene.setButtons(buttons);
							scene.setPlugs(plugs);
							scenes.add(scene);
						}
						if ("program".equals(parser.getName())) {
							program.setScenes(scenes);
							programs.add(program);
						}
						//---------new add begin
						if("twinflag".equals(parser.getName())){
							twinflag.setTwinflag(programs);
						}
						//---------new add end
						break;
				}

				event = parser.next();
			}

		} catch (XmlPullParserException e) {
			LogUtil.printApointedLog(ParseProgram.class, "parser program XmlPullParserException error!!", LogType.error);
			e.printStackTrace();
		} catch (IOException e) {
			LogUtil.printApointedLog(ParseProgram.class, "parser program IOException error!!", LogType.error);
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
					is = null;
				} catch (Exception e2) {
					LogUtil.printApointedLog(ParseProgram.class, "inputstream close error!!", LogType.error);
				}

			}
		}

//		return program;
		return twinflag;
	}

	public static List<String> getDownloadPath(ProgramBean program) {
		List<String> paths = new ArrayList<String>();
		List<SceneBean> scenes = program.getScenes();
		for (SceneBean scene : scenes) {
			String _backimg = scene.getBackimg();
			if (!TextUtils.isEmpty(_backimg) && !paths.contains(_backimg)) {
				paths.add(_backimg);
			}
			List<ButtonBean> buttons = scene.getButtons();
			for (ButtonBean button : buttons) {
				String _src = button.getBackImg();
				if (!TextUtils.isEmpty(_src) && !paths.contains(_src)) {
					paths.add(_src);
				}
			}

			List<RegionBean> regions = scene.getRegions();
			for (RegionBean region : regions) {
				List<ElementBean> elements = region.getElements();
				for (ElementBean element : elements) {
					if (element.getType().equals(ElementType.网页)) {
						System.out.println("---------------" + element.getSrc());
						continue;
					}
					if (!TextUtils.isEmpty(element.getSrc()) && !paths.contains(element.getSrc())) {
						paths.add(element.getSrc());
					}
				}
			}
			List<PlugBean> plugs = scene.getPlugs();
			for (PlugBean plug : plugs) {
				AlbumBean album = plug.getAlbum();
				if (album != null) {
					List<ElementBean> elements = album.getElements();
					for (ElementBean element : elements) {
						if (!TextUtils.isEmpty(element.getSrc()) && !paths.contains(element.getSrc())) {
							paths.add(element.getSrc());
						}
					}
				}
			}
		}
		return paths;
	}

	// 使用set集合存储下载列表
	public static Set<String> getDownloadPath2(ProgramBean program) {
		long time = System.currentTimeMillis();
		Set<String> paths = new HashSet<String>();
		List<SceneBean> scenes = program.getScenes();
		for (SceneBean scene : scenes) {
			if (!TextUtils.isEmpty(scene.getBackimg())) {
				paths.add(scene.getBackimg());
			}
			List<ButtonBean> buttons = scene.getButtons();
			for (ButtonBean button : buttons) {
				if (!TextUtils.isEmpty(button.getBackImg())) {
					paths.add(button.getBackImg());
				}
			}

			List<RegionBean> regions = scene.getRegions();
			for (RegionBean region : regions) {
				List<ElementBean> elements = region.getElements();
				for (ElementBean element : elements) {
					if (!TextUtils.isEmpty(element.getSrc())) {
						paths.add(element.getSrc());
					}
				}
			}
			List<PlugBean> plugs = scene.getPlugs();
			for (PlugBean plug : plugs) {
				AlbumBean album = plug.getAlbum();
				List<ElementBean> elements = album.getElements();
				for (ElementBean element : elements) {
					if (!TextUtils.isEmpty(element.getSrc())) {
						paths.add(element.getSrc());
					}
				}
			}
		}
		LogUtil.printSameLog("利用map获取元素路径费时：" + (System.currentTimeMillis() - time));
		return paths;
	}

}
