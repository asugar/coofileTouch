package com.twinflag.coofiletouch.entity;

import android.media.MediaPlayer;
import android.view.View;

import com.twinflag.coofiletouch.type.ElementType;

/**
 * @author wanghuayi 元素实体类
 */
public class ElementBean implements Cloneable {
	// 元素id
	private String id;
	// 元素name
	private String name;
	// 元素时间
	private Integer life;
	// 元素路径问题
	private String src;
	// 跳转的场景id
	private Integer sceneid;
	// 网页路径
	private String webSrc;
	// 元素类型：详见read.txt
	private ElementType type;
	private String font;
	private Integer size;
	private String fgcolor;
	private String bgcolor;
	private String content;

	// 未在解析中添加的属性
	// private Integer download;
	// private Integer speed;
	// private Integer direction;
	// private Integer effect;

	private View view;// 记录元素显示引用
	private MediaPlayer media;// 记录音频播放的引用
	private RegionBean region;// 当前元素所在的区域
	private PlugBean plug;// 当前元素所在的插件

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getLife() {
		return life;
	}

	public void setLife(Integer life) {
		this.life = life;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public Integer getSceneid() {
		return sceneid;
	}

	public void setSceneid(Integer sceneid) {
		this.sceneid = sceneid;
	}

	public ElementType getType() {
		return type;
	}

	public void setType(int type) {
		// 应该有更好的方法给枚举类型赋值
		switch (type) {
			case 1:
				this.type = ElementType.文字;
				break;
			case 2:
				this.type = ElementType.音频;
				break;
			case 3:
				this.type = ElementType.图片;
				break;
			case 4:
				this.type = ElementType.视频;
				break;
			case 5:
				this.type = ElementType.网页;
				break;
			case 6:
				this.type = ElementType.flash;
				break;
			case 7:
				this.type = ElementType.office;
				break;
			case 8:
				this.type = ElementType.流媒体;
				break;
			default:
				this.type = ElementType.未定义;
				break;
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWebSrc() {
		return webSrc;
	}

	public void setWebSrc(String webSrc) {
		this.webSrc = webSrc;
	}

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public RegionBean getRegion() {
		return region;
	}

	public void setRegion(RegionBean region) {
		this.region = region;
	}

	public MediaPlayer getMedia() {
		return media;
	}

	public void setMedia(MediaPlayer media) {
		this.media = media;
	}

	@Override
	public ElementBean clone() throws CloneNotSupportedException {
		return (ElementBean) super.clone();
	}

	public String getFont() {
		return font;
	}

	public void setFont(String font) {
		this.font = font;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public String getFgcolor() {
		return fgcolor;
	}

	public void setFgcolor(String fgcolor) {
		this.fgcolor = fgcolor;
	}

	public String getBgcolor() {
		return bgcolor;
	}

	public void setBgcolor(String bgcolor) {
		this.bgcolor = bgcolor;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setType(ElementType type) {
		this.type = type;
	}

	public PlugBean getPlug() {
		return plug;
	}

	public void setPlug(PlugBean plug) {
		this.plug = plug;
	}

}
