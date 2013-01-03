package com.Streaming.NhatKy.Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.Streaming.NhatKy.Common.NhatKyObject;

public class NhatKyModel {
	private Context mContext;

	private static final String KEY_ID = "_id";
	private static final String KEY_TITLE = "_title";
	private static final String KEY_MESSAGE = "_message";
	private static final String KEY_TIME = "_time";
	private static final String KEY_ISIMAGE = "_isImage";
	private static final String KEY_PATHIMAGE = "_pathImage";
	private static final String DATABASE_NAME = "dbNhatKy";
	private static final String DATABASE_TABLE = "tblNhatKy";
	private static final int DATABASE_VERSION = 2;

	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDB;
	private static final String DATABASE_CREATE = "Create table "
			+ DATABASE_TABLE + "(" + KEY_ID
			+ " integer primary key autoincrement," + KEY_TITLE + " text,"
			+ KEY_MESSAGE + " text," + KEY_TIME + " text," + KEY_ISIMAGE
			+ " boolean," + KEY_PATHIMAGE + " text)";

	public NhatKyModel(Context context) {
		mContext = context;
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
			db.execSQL("DROP TABLE IF EXISTS tblNhatKy");
			onCreate(db);
		}
	}

	public NhatKyModel open() {
		mDbHelper = new DatabaseHelper(mContext, DATABASE_NAME, null,
				DATABASE_VERSION);
		mDB = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		mDbHelper.close();
	}

	public long insertNotHasImage(String title, String message, Date time) {
		ContentValues initValue = new ContentValues();
		initValue.put(KEY_TITLE, title);
		initValue.put(KEY_MESSAGE, message);
		initValue.put(KEY_ISIMAGE, false);
		initValue.put(KEY_PATHIMAGE, "");
		initValue.put(KEY_TIME, toFullDate.format(time));
		open();
		long id = mDB.insert(DATABASE_TABLE, null, initValue);
		close();
		return id;
	}

	public long insertHasImage(String title, String message, Date time,
			String imgPath) {
		ContentValues initValue = new ContentValues();
		initValue.put(KEY_TITLE, title);
		initValue.put(KEY_MESSAGE, message);
		initValue.put(KEY_ISIMAGE, true);
		initValue.put(KEY_PATHIMAGE, imgPath);
		initValue.put(KEY_TIME, toFullDate.format(time));
		open();
		long id = mDB.insert(DATABASE_TABLE, null, initValue);
		close();
		return id;
	}

	public boolean delete(long id) {
		open();
		long i = mDB.delete(DATABASE_TABLE, KEY_ID + "='" + id + "'", null);
		close();
		return i > 0;
	}

	public boolean updateImage(long id, String imagePath) {
		open();
		ContentValues initValue = new ContentValues();
		initValue.put(KEY_ID, id);
		initValue.put(KEY_PATHIMAGE, imagePath);
		long i = mDB.update(DATABASE_TABLE, initValue,
				KEY_ID + "='" + id + "'", null);
		close();
		return i > 0;
	}

	public NhatKyObject getNhatKyByID(long id) {
		NhatKyObject obj = null;
		open();
		Cursor cursor = mDB.query(DATABASE_TABLE, new String[] { KEY_ID,
				KEY_TITLE, KEY_MESSAGE, KEY_TIME, KEY_ISIMAGE, KEY_PATHIMAGE },
				KEY_ID + "=" + id, null, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			try {
				obj = new NhatKyObject(
						cursor.getInt(cursor.getColumnIndex(KEY_ID)),
						cursor.getString(cursor.getColumnIndex(KEY_TITLE)),
						cursor.getString(cursor.getColumnIndex(KEY_MESSAGE)),
						toFullDate.parse(cursor.getString(cursor
								.getColumnIndex(KEY_TIME))),
						Boolean.parseBoolean(cursor.getString(
								cursor.getColumnIndex(KEY_ISIMAGE)).equals("1") ? "true"
								: "false"), cursor.getString(cursor
								.getColumnIndex(KEY_PATHIMAGE)));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		cursor.close();
		close();
		return obj;
	}

	public ArrayList<NhatKyObject> getAllNhatKy() {
		ArrayList<NhatKyObject> glstNhatky = new ArrayList<NhatKyObject>();
		try {
			open();
			Cursor cursor = mDB.query(DATABASE_TABLE, new String[] { KEY_ID,
					KEY_TITLE, KEY_MESSAGE, KEY_TIME, KEY_ISIMAGE,
					KEY_PATHIMAGE }, null, null, null, null, null);
			if (cursor != null) {
				try {
					cursor.moveToFirst();
					do {
						NhatKyObject obj = new NhatKyObject(
								cursor.getInt(cursor.getColumnIndex(KEY_ID)),
								cursor.getString(cursor
										.getColumnIndex(KEY_TITLE)),
								cursor.getString(cursor
										.getColumnIndex(KEY_MESSAGE)),
								toFullDate.parse(cursor.getString(cursor
										.getColumnIndex(KEY_TIME))),
								Boolean.parseBoolean(cursor.getString(
										cursor.getColumnIndex(KEY_ISIMAGE))
										.equals("1") ? "true" : "false"),
								cursor.getString(cursor
										.getColumnIndex(KEY_PATHIMAGE)));
						glstNhatky.add(obj);
					} while (cursor.moveToNext());
				} catch (Exception e) {
				}
			}
			cursor.close();
			close();
		} catch (Exception ex) {
		}
		return glstNhatky;
	}

	SimpleDateFormat toFullDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
}
