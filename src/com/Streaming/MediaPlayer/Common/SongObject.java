package com.Streaming.MediaPlayer.Common;

public class SongObject {
	private int mId;
	private String mLink;
	private String mIdSong;
	private String mTitle;

	public void setID(int id) {
		this.mId = id;
	}

	public int getID() {
		return mId;
	}

	public void setLink(String link) {
		this.mLink = link;
	}

	public String getLink() {
		return mLink;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setIdSong(String idSong) {
		this.mIdSong = idSong;
	}

	public String getIdSong() {
		return mIdSong;
	}

	public SongObject() {
	}

	public SongObject(int id, String link, String idSong, String title) {
		this.mId = id;
		this.mLink = link;
		this.mIdSong = idSong;
		this.mTitle = title;
	}
}
