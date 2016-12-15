package com.hyc.db;

import java.io.File;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.hyc.bean.PictureId;

public class DBManagerAdvert {

	public SQLiteDatabase db;

	public void creatDB() {						
		File dbf = new File(getDir() + "/baige/db");		
		if (!dbf.exists()) {
			dbf.mkdirs();
		}
		db = SQLiteDatabase.openOrCreateDatabase(dbf.toString() + "/"
				+ "advert.db", null);
		// db.execSQL("DROP TABLE IF EXISTS stu");
		db.execSQL("create table if not exists advert (_id INTEGER PRIMARY KEY,src VARCHAR, time TEXT,stime VARCHAR(50),etime VARCHAR(50),srcname VARCHAR(50))");
		System.out.println("------DBManagerAdvert.creatDB-----------");
	}

	public void openDB() {
		File dbf = new File(getDir() + "/baige/db");
		if (!dbf.exists()) {
			dbf.mkdirs();
		}
		db = SQLiteDatabase.openOrCreateDatabase(dbf.toString() + "/"
				+ "advert.db", null);
		System.out.println("------DBManagerAdvert.openDB-----------");
	}

	public void insert(PictureId picture) {
		if (picture != null) {
			ContentValues values = new ContentValues();
			values.put("src", picture.getSrc());
			values.put("time", picture.getTime());
			values.put("stime", picture.getStime());
			values.put("etime", picture.getEtime());
			values.put("srcname", picture.getSrcname());
			db.insert("advert", "_id", values);
		}
		System.out.println("------DBManagerAdvert.insert-----------");

	}

	public PictureId query(String val) {
		PictureId picture = new PictureId();
		String[] columns = { "_id", "src", "time", "stime", "etime" };
		String[] selectionArgs = { val };
		Cursor c = db.query("advert", columns, "_id=?", selectionArgs, null,
				null, null);
		/*
		 * Cursor c = db.query("stu", columns, "cardno='"+val+"'", null, null,
		 * null, null);
		 */
		while (c.moveToNext()) {
			picture.setTime(c.getString(c.getColumnIndex("time")));
			picture.setStime(c.getString(c.getColumnIndex("stime")));
			picture.setEtime(c.getString(c.getColumnIndex("etime")));
			picture.setSrc(c.getString(c.getColumnIndex("src")));
		}
		System.out.println("------DBManagerAdvert.query-----------");
		c.close();
		return picture;

	}

	public void closeDB() {
		db.close();
	}

    public void deleteDir(String srcname){
    	db.delete("advert", "srcname=?", new String[]{srcname});
    }
	public void deleteDB() {
		db.execSQL("DROP TABLE IF EXISTS advert");
	}

	private File getDir() {
		// 得到SD卡根目录
		File dir = Environment.getExternalStorageDirectory();
		if (dir.exists()) {
			return dir;
		} else {
			dir.mkdirs();
			return dir;
		}
	}
}
