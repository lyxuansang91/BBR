package com.Streaming.SlideShow.Common;

import android.graphics.Bitmap;

public class ImageObject {
	private String mUrl;
	private Bitmap mBitmap;

	public String getUrl() {
		return this.mUrl;
	}

	public void setUrl(String url) {
		this.mUrl = url;
	}

	public Bitmap getBitmap() {
		return this.mBitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.mBitmap = bitmap;
	}

	public ImageObject() {

	}

	public ImageObject(String url, Bitmap bm) {
		this.mUrl = url;
		this.mBitmap = bm;
	}

	public void dispose() {
		this.mBitmap = null;
	}

}
