package com.twinflag.coofiletouch.entity;

import java.util.List;

/**
 * @author wanghuayi 节目实体类
 */
public class ProgramBean {
	// 节目id
	private String id;
	// 节目名称
	private String name;
	// 节目宽度
	private Integer width;
	// 节目高度
	private Integer height;
	// 节目循环类型
	private Integer looptype;
	// 节目循环描述
	private String loopdesc;
	// 循环开始时间
	private String loopStartTime;
	// 循环结束时间
	private String loopStopTime;
	// 节目开始时间
	private String startDate;
	// 节目结束时间
	private String stopDate;
	// 节目更新时间（发布的时间）
	private String updateTime;
	
	//-----------------------------
	
	// 节目中场景列表
	private List<SceneBean> scenes;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer getLoopType() {
		return looptype;
	}

	public void setLoopType(Integer looptype) {
		this.looptype = looptype;
	}

	public String getLoopdesc() {
		return loopdesc;
	}

	public void setLoopdesc(String loopdesc) {
		this.loopdesc = loopdesc;
	}

	public String getLoopStartTime() {
		return loopStartTime;
	}

	public void setLoopStartTime(String loopStartTime) {
		this.loopStartTime = loopStartTime;
	}

	public String getLoopStopTime() {
		return loopStopTime;
	}

	public void setLoopStopTime(String loopStopTime) {
		this.loopStopTime = loopStopTime;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getStopDate() {
		return stopDate;
	}

	public void setStopDate(String stopDate) {
		this.stopDate = stopDate;
	}

	public List<SceneBean> getScenes() {
		return scenes;
	}

	public void setScenes(List<SceneBean> scenes) {
		this.scenes = scenes;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

}
