package com.twinflag.coofiletouch.plug;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.aphidmobile.flip.FlipViewController;
import com.twinflag.coofiletouch.R;
import com.twinflag.coofiletouch.entity.ElementBean;
import com.twinflag.coofiletouch.entity.PlugBean;
import com.twinflag.coofiletouch.showUtil.DestroyUtil;
import com.twinflag.coofiletouch.utils.BitmapUtil;

public class FlipPlug extends FrameLayout implements BasePlug {

	private FlipViewController flipView;
	private Context mContext;
	private int width;
	private int height;
	private List<ElementBean> elements;
	PlipPlugAdapter mAdapter;

	public FlipPlug(Context context) {
		super(context);
		mContext = context;
	}

	@Override
	public void load(PlugBean plug) {
		elements = plug.getAlbum().getElements();
		width = plug.getWidth();
		height = plug.getHeight();

		flipView = new FlipViewController(mContext, FlipViewController.HORIZONTAL);
		this.addView(flipView);
		mAdapter = new PlipPlugAdapter();
		flipView.setAdapter(mAdapter);
	}

	private class PlipPlugAdapter extends BaseAdapter {
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder _Holder = null;
			if (convertView == null) {
				_Holder = new ViewHolder();
				convertView = LayoutInflater.from(mContext).inflate(R.layout.flip_item, parent, false);
				_Holder._Image = (ImageView) convertView.findViewById(R.id.iv_flip_image);
				convertView.setTag(_Holder);
			} else {
				_Holder = (ViewHolder) convertView.getTag();
			}
			_Holder._Image.setImageBitmap(BitmapUtil.getBitmap(elements.get(position).getSrc(), width, height));
			return convertView;

		}

		class ViewHolder {
			ImageView _Image;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public int getCount() {
			return elements.size();
		}

	}

	@Override
	public void destroy() {
		this.removeAllViews();
		this.destroyDrawingCache();
		for (int i = 0; i < flipView.getChildCount(); i++) {
			ImageView image = (ImageView) flipView.getItemAtPosition(0);
			BitmapDrawable bd = (BitmapDrawable) image.getDrawable();
			DestroyUtil.destroyDrawable(bd);
		}
	}

}
