package com.twinflag.coofiletouch.plug;

import com.twinflag.coofiletouch.entity.PlugBean;

public interface BasePlug {

	public void load(PlugBean plug);// 加载插件资源

	public void destroy();// 销毁插件资源

}
