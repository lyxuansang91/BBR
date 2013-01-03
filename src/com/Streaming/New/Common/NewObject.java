package com.Streaming.New.Common;

public class NewObject {
	private String mId;
	private String mDescription;
	private String mThumb;
	private String mTitle;

	public NewObject() {
	}

	public NewObject(String id, String des, String thumb, String title) {
		mId = id;
		mDescription = des;
		mThumb = thumb;
		mTitle = title;
	}

	public void setId(String id) {
		mId = id;
	}

	public String getId() {
		return mId;
	}

	public void setDescription(String des) {
		mDescription = des;
	}

	public String getDescription() {
		return mDescription;
	}

	public void setThumb(String thumb) {
		mThumb = thumb;
	}

	public String getThumb() {
		return mThumb;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public String getTitle() {
		return mTitle;
	}
}
