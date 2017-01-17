package com.bee.k24.services;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class DataSource {
	
	private SQLiteDatabase mSQL;
	private Db mDBH;
	
	public DataSource (Context context){
		mDBH = new Db(context);
	}

	public void open() throws SQLiteException {
		mSQL = mDBH.getWritableDatabase();
	}
	
	public void close() {
		mDBH.close();
	}

	//Insert data siswa
	public void addSiswa(String ID, String NAMA, String ASAL, String JOIN){
		mSQL.execSQL("insert into siswa values('" + ID + "','" + NAMA + "','" + ASAL + "','" + JOIN + "')");
		}		
		//update siswa
		public void updateSiswa(){
			mSQL.execSQL("DROP TABLE IF EXISTS siswa");
			mSQL.execSQL("create table siswa(id text NOT NULL, nama text null, asal text null, masuk text NULL)");
		}
		public void cleardata(){
			mSQL.execSQL("delete from siswa");
		}

 }