package com.Streaming.NhatKy.Common;

import java.util.Date;

public class NhatKyObject {
	private int mID;
	private String mTitle;
	private String mMessage;
	private Date mTime;
	private boolean mIsImage;
	private String mImagePath;

	public void setID(int id) {
		mID = id;
	}

	public int getID() {
		return mID;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public void setMessage(String message) {
		mMessage = message;
	}

	public String getMessage() {
		return mMessage;
	}

	public void setTime(Date time) {
		mTime = time;
	}

	public Date getTime() {
		return mTime;
	}

	public void setIsImage(boolean isImage) {
		this.mIsImage = isImage;
	}

	public boolean getIsImage() {
		return this.mIsImage;
	}

	public void setImagePath(String imagePath) {
		this.mImagePath = imagePath;
	}

	public String getImagePath() {
		return this.mImagePath;
	}

	public NhatKyObject() {
	}

	public NhatKyObject(int id, String title, String message, Date time,
			boolean isImage, String imagePath) {
		mID = id;
		mTitle = title;
		mMessage = message;
		mTime = time;
		mIsImage = isImage;
		mImagePath = imagePath;
	}
}
