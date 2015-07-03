package com.twinflag.coofiletouch.test;

import java.util.ArrayList;
import java.util.List;

import com.twinflag.coofiletouch.entity.AlbumBean;
import com.twinflag.coofiletouch.entity.ElementBean;
import com.twinflag.coofiletouch.entity.PlugBean;

public class TestPlug {

	private static String path = "/image/img";

	public static PlugBean getData() {
		PlugBean plug = new PlugBean();
		plug.setWidth(1920);
		plug.setHeight(1080);
		AlbumBean album = new AlbumBean();
		List<ElementBean> elements = new ArrayList<ElementBean>();
	
		for(int i = 1; i < 900; i++){
			ElementBean e = new ElementBean();
			e.setSrc(path + i%8 + ".jpg");
			elements.add(e);
		}

		album.setElements(elements);
		plug.setAlbum(album);
		return plug;
	}

}
