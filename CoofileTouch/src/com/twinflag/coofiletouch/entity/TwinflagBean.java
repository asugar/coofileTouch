package com.twinflag.coofiletouch.entity;

import java.util.ArrayList;
import java.util.List;

public class TwinflagBean {
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
	// 节目列表
	private List<ProgramBean> programs = new ArrayList<ProgramBean>();

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

	public Integer getLooptype() {
		return looptype;
	}

	public void setLooptype(Integer looptype) {
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

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public List<ProgramBean> getTwinflag() {
		return programs;
	}

	public void setTwinflag(List<ProgramBean> twinflag) {
		this.programs = twinflag;
	}
}
