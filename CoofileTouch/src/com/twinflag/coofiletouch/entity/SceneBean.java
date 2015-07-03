package com.twinflag.coofiletouch.entity;

import java.util.List;

import android.view.View;

/**
 * @author wanghuayi 场景实体类
 */
public class SceneBean {
	// 场景id
	private String id;
	// 场景name
	private String name;
	// 场景背景图片
	private String backimg;
	// 场景背景颜色
	private String backcolor;
	// 是否是主场景
	private Boolean ismain;
	// 区域列表
	private List<RegionBean> regions;
	// 按钮列表
	private List<ButtonBean> buttons;
	// 插件
	private List<PlugBean> plugs;
	// 场景页面引用
	private View view;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBackimg() {
		return backimg;
	}

	public void setBackimg(String backimg) {
		this.backimg = backimg;
	}

	public String getBackcolor() {
		return backcolor;
	}

	public void setBackcolor(String backcolor) {
		this.backcolor = backcolor;
	}

	public Boolean getIsmain() {
		return ismain;
	}

	public void setIsmain(Boolean ismain) {
		this.ismain = ismain;
	}

	public List<RegionBean> getRegions() {
		return regions;
	}

	public void setRegions(List<RegionBean> regions) {
		this.regions = regions;
	}

	public List<ButtonBean> getButtons() {
		return buttons;
	}

	public void setButtons(List<ButtonBean> buttons) {
		this.buttons = buttons;
	}

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public List<PlugBean> getPlugs() {
		return plugs;
	}

	public void setPlugs(List<PlugBean> plugs) {
		this.plugs = plugs;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
