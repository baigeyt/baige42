package com.tony.autoscroll;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.widget.VideoView;

/**
 * 设置视频播放的宽高
 * @author Administrator
 *
 */

public class FullScreenVideoView extends VideoView {
	ViewPager vp ;

	public FullScreenVideoView(Context context,ViewPager vp) {
		super(context);
		this.vp  = vp;
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		int width = getDefaultSize(vp.getWidth()/2, widthMeasureSpec);
		int height = getDefaultSize(vp.getHeight(), heightMeasureSpec);

		setMeasuredDimension(width, height);
	}

}
