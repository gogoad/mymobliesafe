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

	/**
	 * @return 总数据个数
	 */
	public int getTotalRows() {
		SQLiteDatabase database = blackDB.getReadableDatabase();
		Cursor cursor = database.rawQuery("select count(1) from "
				+ BlackTable.BLACKTABLE, null);
		cursor.moveToNext();
		// 总行数
		int totalRows = cursor.getInt(0);

		cursor.close();// 关闭游标
		return totalRows;
	}
	
	
	/**
	 * @param datasNumber
	 *       分批加载的数据条目数
	 * @param startIndex
	 *       取数据的起始位置
	 * @return
	 *    分批加载数据
	 */
	public List<BlackBean> getMoreDatas(int datasNumber,int startIndex){
		List<BlackBean> datas = new ArrayList<BlackBean>();
		SQLiteDatabase database = blackDB.getReadableDatabase();
		// 获取blacktb的所有数据游标 (2 + 3) + ""
		Cursor cursor = database.rawQuery("select " + BlackTable.PHONE + ","
				+ BlackTable.MODE + " from " + BlackTable.BLACKTABLE
				+ " order by _id desc limit ?,? ", new String[] {startIndex + "",datasNumber + ""});

		while (cursor.moveToNext()) {
			// 有数据，数据封装
			BlackBean bean = new BlackBean();

			// 封装黑名单号码
			bean.setPhone(cursor.getString(0));

			// 封装拦截模式
			bean.setMode(cursor.getInt(1));

			// 添加数据到集合中
			datas.add(bean);
		}

		cursor.close();// 关闭游标
		database.close();// 关闭数据库

		return datas;
	}

	/**
	 * @param currentPage
	 *            当前页的页码
	 * @param perPage
	 *            每页显示多少条数据
	 * @return 当前页的数据
	 */
	public List<BlackBean> getPageDatas(int currentPage, int perPage) {
		List<BlackBean> datas = new ArrayList<BlackBean>();
		SQLiteDatabase database = blackDB.getReadableDatabase();
		// 获取blacktb的所有数据游标 (2 + 3) + ""
		Cursor cursor = database.rawQuery("select " + BlackTable.PHONE + ","
				+ BlackTable.MODE + " from " + BlackTable.BLACKTABLE
				+ " limit ?,? ", new String[] {((currentPage - 1) * perPage) + "",perPage + ""});

		while (cursor.moveToNext()) {
			// 有数据，数据封装
			BlackBean bean = new BlackBean();

			// 封装黑名单号码
			bean.setPhone(cursor.getString(0));

			// 封装拦截模式
			bean.setMode(cursor.getInt(1));

			// 添加数据到集合中
			datas.add(bean);
		}

		cursor.close();// 关闭游标
		database.close();// 关闭数据库

		return datas;

	}

	/**
	 * @param perPage
	 *            指定每页显示多少条数据
	 * @return 总页数
	 */
	public int getTotalPages(int perPage) {
		int totalRows = getTotalRows();
		// 计算出多少页，采用ceil函数，返回不小于该数的最小整数 如 :6.1 返回7.0
		int totalPages = (int) Math.ceil(totalRows * 1.0 / perPage);
		return totalPages;

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
