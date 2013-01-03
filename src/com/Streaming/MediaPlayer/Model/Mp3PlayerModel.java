package com.Streaming.MediaPlayer.Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.Streaming.MediaPlayer.Common.SongObject;

public class Mp3PlayerModel {
	private Context mContext;
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDB;

	private static final String KEY_ID = "_id";
	private static final String KEY_LINK = "_link";
	private static final String KEY_SONGID = "_songID";
	private static final String KEY_TITLE = "_title";
	private static final String DATABASE_NAME = "dbMP3";
	private static final String DATABASE_TABLE = "tblMp3";
	private static final int DATABASE_VERSION = 2;
	private static final String DATABASE_CREATE = "Create table "
			+ DATABASE_TABLE + "(" + KEY_ID
			+ " integer primary key autoincrement," + KEY_LINK + " text,"
			+ KEY_SONGID + " text," + KEY_TITLE + " text)";

	public static String EX_MAX = "Het Link";

	public Mp3PlayerModel(Context context) {
		this.mContext = context;
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.i("Upgrading DB", "Upgrading DB");
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);
		}
	}

	public Mp3PlayerModel open() {
		mDbHelper = new DatabaseHelper(mContext, DATABASE_NAME, null,
				DATABASE_VERSION);
		mDB = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		mDbHelper.close();
	}

	public long insert(String link, String idSong, String title) {
		ContentValues initValue = new ContentValues();
		initValue.put(KEY_LINK, link);
		initValue.put(KEY_SONGID, idSong);
		initValue.put(KEY_TITLE, title);
		open();
		long id = mDB.insert(DATABASE_TABLE, null, initValue);
		close();
		return id;
	}

	public long delete(long id) {
		open();
		long i = mDB.delete(DATABASE_TABLE, KEY_ID + "='" + id + "'", null);
		close();
		return i;
	}

	public SongObject getLinkByID(long id) {
		SongObject obj = null;
		open();
		Cursor cursor = mDB.query(DATABASE_TABLE, new String[] { KEY_ID,
				KEY_LINK, KEY_SONGID, KEY_TITLE }, KEY_ID + "=" + id, null,
				null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			obj = new SongObject(cursor.getInt(cursor.getColumnIndex(KEY_ID)),
					cursor.getString(cursor.getColumnIndex(KEY_LINK)),
					cursor.getString(cursor.getColumnIndex(KEY_SONGID)),
					cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
		}
		cursor.close();
		close();
		return obj;
	}

	public ArrayList<SongObject> getAllSong() {
		ArrayList<SongObject> glstSong = new ArrayList<SongObject>();
		try {
			open();
			Cursor cursor = mDB.query(DATABASE_TABLE, new String[] { KEY_ID,
					KEY_LINK, KEY_SONGID, KEY_TITLE }, null, null, null, null,
					null);
			if (cursor != null) {
				try {
					cursor.moveToFirst();
					do {
						SongObject songobj = new SongObject(
								cursor.getInt(cursor.getColumnIndex(KEY_ID)),
								cursor.getString(cursor
										.getColumnIndex(KEY_LINK)),
								cursor.getString(cursor
										.getColumnIndex(KEY_SONGID)),
								cursor.getString(cursor
										.getColumnIndex(KEY_TITLE)));
						glstSong.add(songobj);
					} while (cursor.moveToNext());
				} catch (Exception e) {
				}
			}
			cursor.close();
			close();
		} catch (Exception ex) {
		}
		return glstSong;
	}

	public boolean isRequestLike(String url) {
		try {
			URL cn = new URL(url);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					cn.openStream()));
			String str;
			String html = "";
			while ((str = in.readLine()) != null) {
				html += str;
			}
			in.close();
			JSONArray entries = new JSONArray(html);
			JSONObject obj = entries.getJSONObject(0);
			String r = obj.getString("result");
			if (r.equals("1"))
				return true;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	public ArrayList<SongObject> LayNhac(String url) throws Exception {
		ArrayList<SongObject> glstSong = new ArrayList<SongObject>();
		try {
			URL cn = new URL(url);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					cn.openStream()));
			String str;
			String html = "";
			while ((str = in.readLine()) != null) {
				html += str;
			}
			in.close();
			JSONArray entries = new JSONArray(html);
			for (int i = 0; i < entries.length(); i++) {
				SongObject s = new SongObject();
				String link = entries.getJSONObject(i).getString("link");
				s.setLink(link);
				String id = entries.getJSONObject(i).getString("id");
				s.setIdSong(id);
				String title = entries.getJSONObject(i).getString("title");
				s.setTitle(title);
				glstSong.add(s);
			}
		} catch (MalformedURLException e) {
		} catch (IOException e) {
		} catch (JSONException e) {
			throw new Exception(EX_MAX);
		}
		Log.d("TAG", "Đã lấy xong link nhạc , sô lượng link nhạc là : "
				+ glstSong.size());
		return glstSong;
	}

}
