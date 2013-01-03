package com.Streaming.SlideShow.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.Streaming.R;
import com.Streaming.SlideShow.Controller.SlideShowController;

public class SlideShowView extends LinearLayout {
	private ImageView imgView;

	public SlideShowView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initControls();
	}

	public SlideShowView(Context context) {
		super(context);
		initControls();
	}

	private void initControls() {
		LayoutInflater layoutInflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(R.layout.slideshow_layout, this);
		imgView = (ImageView) view.findViewById(R.id.imageView);
		SlideShowController slideShowController = new SlideShowController(
				getContext(), this);
		slideShowController.handleMessage(SlideShowController.SHOWSLIDE);
	}

	public ImageView getImageView() {
		return imgView;
	}

}
