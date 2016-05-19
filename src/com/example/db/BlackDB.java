package com.example.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * 黑名单管理数据库
 * @author jxa
 *
 */
public class BlackDB extends SQLiteOpenHelper {

	
	public BlackDB(Context context) {
		super(context, "black.db", null, 1);
	}
	//数据库第一次创建时调用
	@Override
	public void onCreate(SQLiteDatabase db) {
		// 创建黑名单表，字段：_id,number(电话号码)，mode(拦截模式，1.表示短信拦截，2.表示电话拦截，3，表示全部拦截)
		db.execSQL("create table blacknumber (_id integer primary key autoincrement, phone varchar(20),mode integer)");

	}
	//数据库更新时，version发生变化 ，调用此方法
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table blacknumber");
		onCreate(db);

	}

}
