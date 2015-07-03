package com.twinflag.coofiletouch.playLog;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.twinflag.coofiletouch.entity.ElementBean;
import com.twinflag.coofiletouch.entity.PlugBean;
import com.twinflag.coofiletouch.entity.ProgramBean;
import com.twinflag.coofiletouch.entity.SceneBean;

public class PlugLogBean {
	// 任务id和name
	private String programId;
	private String programName;
	// 场景id和name
	private String sceneId;
	private String sceneName;
	// 插件id和name
	private String plugId;
	private String plugName;
	// 元素id，name,类型，开始时间，结束时间，周期
	private String elementId;
	private String elementName;
	private Integer type;
	private String startTime;
	private String endTime;
	private Integer life;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public String toString() {
		String messageFormat = "''{0}'',''{1}'',''{2}'',''{3}'',{4},{5},''{6}'',''{7}'';\r\n";
		// return new
		// StringBuffer().append(programId).append(",'").append(programName).append("',").append(sceneId)
		// .append(",'").append(sceneName).append("',").append(plugId).append(",'").append(plugName)
		// .append("',").append(elementId).append(",'").append(elementName).append("',").append(type)
		// .append(",").append(life).append(",'").append(startTime).append("','").append(endTime)
		// .append("';").append("\r\n").toString();

		// return new
		// StringBuffer().append("'").append(programName).append("','").append(sceneName).append("','")
		// .append(plugName).append("','").append(elementName).append("',").append(type).append(",")
		// .append(life).append(",'").append(startTime).append("','").append(endTime).append("';")
		// .append("\r\n").toString();
		return MessageFormat.format(messageFormat, programName, sceneName, plugName, elementName, type, life,
						startTime, endTime);
	}

	public PlugLogBean(ProgramBean program, SceneBean scene, ElementBean element) {
		this.programId = program.getId();
		this.programName = program.getName();
		this.sceneId = scene.getId();
		this.sceneName = scene.getName();
		PlugBean plug = element.getPlug();
		if (plug != null) {
			this.plugId = plug.getId();
			this.plugName = plug.getName();
		}
		this.elementId = element.getId();
		this.elementName = element.getName();
		this.type = element.getType().getIntType();
		this.life = element.getLife();
		String date = sdf.format(new Date());
		this.startTime = date;
		// this.endTime = getLogDate(date, life);

	}

	/**
	 * 获取元素播放的结束时间
	 * */
	private String getLogDate(String date, Integer durtime) {
		try {
			long start = sdf.parse(date).getTime();
			long end = start + durtime * 1000;
			Date date1 = new Date(end);
			String str = sdf.format(date1);
			return str;
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getProgramId() {
		return programId;
	}

	public void setProgramId(String programId) {
		this.programId = programId;
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public String getSceneId() {
		return sceneId;
	}

	public void setSceneId(String sceneId) {
		this.sceneId = sceneId;
	}

	public String getSceneName() {
		return sceneName;
	}

	public void setSceneName(String sceneName) {
		this.sceneName = sceneName;
	}

	public String getPlugId() {
		return plugId;
	}

	public void setPlugId(String plugId) {
		this.plugId = plugId;
	}

	public String getPlugName() {
		return plugName;
	}

	public void setPlugName(String plugName) {
		this.plugName = plugName;
	}

	public String getElementId() {
		return elementId;
	}

	public void setElementId(String elementId) {
		this.elementId = elementId;
	}

	public String getElementName() {
		return elementName;
	}

	public void setElementName(String elementName) {
		this.elementName = elementName;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Integer getLife() {
		return life;
	}

	public void setLife(Integer life) {
		this.life = life;
	}

}
