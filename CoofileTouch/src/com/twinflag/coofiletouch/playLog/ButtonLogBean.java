package com.twinflag.coofiletouch.playLog;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.twinflag.coofiletouch.entity.ButtonBean;
import com.twinflag.coofiletouch.entity.ProgramBean;
import com.twinflag.coofiletouch.entity.SceneBean;

public class ButtonLogBean {
	// 任务id和name
	private String programId;
	private String programName;
	// 场景id和name
	private String sceneId;
	private String sceneName;
	// 元素id，name,类型，开始时间，结束时间，周期
	private String buttonId;
	private String buttonName;
	private Integer type;
	private String clickTime;
	// 要跳转的场景id和name
	private String sceneIdTo;
	private String sceneNameTo;

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public String toString() {
		String messageFormat = "''{0}'',''{1}'',''{2}'',{3},''{4}'',''{5}'';\r\n";
		// return new
		// StringBuffer().append("'").append(programName).append("','").append(sceneName).append("','")
		// .append(buttonName).append("',").append(type).append(",'").append(clickTime).append("',")
		// .append(sceneIdTo).append(",'").append(sceneNameTo).append("';").append("\r\n").toString();
		return MessageFormat.format(messageFormat, programName, sceneName, buttonName, type, sceneNameTo, clickTime);
	}

	public ButtonLogBean(ProgramBean program, SceneBean scene, ButtonBean button) {
		this.programId = program.getId();
		this.programName = program.getName();
		this.sceneId = scene.getId();
		this.sceneName = scene.getName();
		this.buttonId = button.getId();
		this.buttonName = button.getName();
		this.type = button.getType().getIntType();
		String date = sdf.format(new Date());
		this.clickTime = date;
		this.sceneIdTo = button.getSceneid();
		this.sceneNameTo = getSceneName(program, sceneId);
	}

	private String getSceneName(ProgramBean program, String id) {
		for (SceneBean scene : program.getScenes()) {
			if (id.equals(scene.getId())) {
				return scene.getName();
			}
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

	public String getButtonId() {
		return buttonId;
	}

	public void setButtonId(String buttonId) {
		this.buttonId = buttonId;
	}

	public String getButtonName() {
		return buttonName;
	}

	public void setButtonName(String buttonName) {
		this.buttonName = buttonName;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getClickTime() {
		return clickTime;
	}

	public void setClickTime(String clickTime) {
		this.clickTime = clickTime;
	}

	public String getSceneIdTo() {
		return sceneIdTo;
	}

	public void setSceneIdTo(String sceneIdTo) {
		this.sceneIdTo = sceneIdTo;
	}

	public String getSceneNameTo() {
		return sceneNameTo;
	}

	public void setSceneNameTo(String sceneNameTo) {
		this.sceneNameTo = sceneNameTo;
	}

	public SimpleDateFormat getSdf() {
		return sdf;
	}

	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}

}
