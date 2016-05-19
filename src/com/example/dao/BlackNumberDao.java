package com.example.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.db.BlackDB;
import com.example.domain.BlackBean;
import com.example.domain.BlackTable;

/**
 * 黑名单数据业务封装类
 * 
 * @author jxa
 * 
 */
public class BlackNumberDao {

	private BlackDB blackDB;
	public BlackNumberDao(Context context){
		this.blackDB = new BlackDB(context);
	}

	// 返回所有黑名单数据
	public List<BlackBean> getAllDatas() {
		List<BlackBean> datas = new ArrayList<BlackBean>();
		SQLiteDatabase database = blackDB.getReadableDatabase();

		Cursor cursor = database.rawQuery("select " + BlackTable.PHONE + ","
				+ BlackTable.MODE + " from " + BlackTable.BLACKTABLE, null);
		
		while (cursor.moveToNext()) {
			BlackBean bean = new BlackBean();
			bean.setPhone(cursor.getString(0));
			bean.setMode(cursor.getInt(1));

			datas.add(bean);
		}
		cursor.close();
		database.close();
		return datas;
	}

	/**
	 * 删除黑名单号码
	 * 
	 * @param phone
	 *            要删除的黑名单号码
	 */
	public void delete(String phone) {
		SQLiteDatabase database = blackDB.getWritableDatabase();
		database.delete(BlackTable.BLACKTABLE, BlackTable.PHONE + "=?",
				new String[] { phone });
		database.close();
	}

	/**
	 * @param phone
	 *            修改的黑名单号码
	 * @param mode
	 *            修改的新的拦截模式
	 */
	public void update(String phone, int mode) {
		SQLiteDatabase database = blackDB.getWritableDatabase();
		ContentValues content = new ContentValues();
		content.put(BlackTable.MODE, mode);

		database.update(BlackTable.BLACKTABLE, content,
				BlackTable.PHONE + "=?", new String[] { phone });
		database.close();
	}
	
	/**
	 * 添加黑名单号码
	 * @param bean
	 *     黑名单信息的封装bean
	 */
	public void add(BlackBean bean) {
		add(bean.getPhone(), bean.getMode());
	}

	/**
	 * 添加黑名单号码
	 * @param phone
	 *     黑名单号码
	 * @param mode
	 *    拦截模式
	 */
	public void add(String phone,int mode){
		//获取黑名单数据库
		SQLiteDatabase db = blackDB.getWritableDatabase();
		ContentValues values = new ContentValues();
		//设置黑名单号码
		values.put(BlackTable.PHONE, phone);
		
		//设置黑名单拦截模式
		values.put(BlackTable.MODE, mode);
		
		//往黑名单表中插入一条记录
		db.insert(BlackTable.BLACKTABLE, null, values );
		
		//关闭数据库
		db.close();
	}
}
