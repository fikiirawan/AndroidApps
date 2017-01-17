package com.bee.k24.services;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
/*
 create by OM FIKI
 */

public class Db extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "k24.db";
	private static final int DATABASE_VERSION = 1;
	public Db(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
		//buat 
		String sql = "create table siswa(id text NOT NULL, nama text null, asal text null, masuk text null);";
		Log.d("Data", "onCreate: " + sql);
		db.execSQL(sql);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
        db.execSQL("DROP TABLE IF EXISTS siswa");
        onCreate(db);
		
	}

}
