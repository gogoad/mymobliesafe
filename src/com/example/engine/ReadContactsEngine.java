package com.example.engine;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.domain.BlackBean;
import com.example.domain.ContactBean;

public class ReadContactsEngine {
	/**
	 * 【1】 三张重要的表 一、data data1 里面存的是所有联系人的信息
	 * 二、data表里面的raw_contact_id实际上是raw_contact里面的id
	 * 三、data表里面的mimetype_id实际上对应的是mimetype表 【2】实现步骤 一、先读取raw_contact表
	 * 读取contact_id字段 从而我就知道手机里面一共有几条联系人 二、再读取data表 根据raw_contact_id读取data1列和
	 * mimetype
	 */
	public static List<ContactBean> readContacts(Context context) {
		List<ContactBean> beans = new ArrayList<ContactBean>();
		Uri uriContants = Uri.parse("content://com.android.contacts/contacts");
		Uri uriDatas = Uri.parse("content://com.android.contacts/data");

		Cursor cursor = context.getContentResolver().query(uriContants,
				new String[] { "_id" }, null, null, null);
		while (cursor.moveToNext()) {
			ContactBean bean = new ContactBean();
			String id = cursor.getString(0);
			Cursor cursor2 = context.getContentResolver().query(uriDatas,
					new String[] { "data1", "mimetype" },
					" raw_contact_id = ? ", new String[] { id }, null);
			//循环每条数据信息都是一个好友的一部分信息
			while(cursor2.moveToNext()) {
				String data  = cursor2.getString(0);
				String mimeType = cursor2.getString(1);
				

				if (mimeType.equals("vnd.android.cursor.item/name")) {
					System.out.println("第" +id + "个用户：名字：" + data);
					bean.setName(data);
				} else if (mimeType.equals("vnd.android.cursor.item/phone_v2")) {
					System.out.println("第" +id + "个用户：电话：" + data);
					bean.setPhone(data);
				}
			}
			cursor2.close();//关闭游标释放资源
			beans.add(bean);//加一条好友信息
		}
		cursor.close();
		return beans;

	}
	//1，电话日志的数据库
	//2,通过分析，db不能直接访问，需要内容提供者访问该数据库
	//3,看上层源码 找到uri content://calls
	//电话日志记录
	public static List<ContactBean> calllogs(Context context) {
		
		Uri uri = Uri.parse("content://call_log/calls");
		Cursor cursor = context.getContentResolver().query(uri, new String[]{"number","name"}, null, null, " _id desc");
		List<ContactBean> datas = new ArrayList<ContactBean>();
		while (cursor.moveToNext()) {
			ContactBean bean = new ContactBean();
			String phone = cursor.getString(0);
			String name = cursor.getString(1);
			
			bean.setPhone(phone);
			bean.setName(name);
			
			datas.add(bean);
		}
		return datas;
	}
	//获取短信日志
	public static List<ContactBean> readSmslogs(Context context) {
		Uri uri = Uri.parse("content://sms");
		//获取电话记录的联系人游标
		Cursor cursor = context.getContentResolver().query(uri, new String[]{"address"}, null, null, " _id desc");
		List<ContactBean> datas = new ArrayList<ContactBean>();
		
		while (cursor.moveToNext()) {
			ContactBean bean = new ContactBean();
			
			String phone = cursor.getString(0);//获取号码
			//String name = cursor.getString(1);//获取名字
			
			//bean.setName(name);
			bean.setPhone(phone);
			
			//添加数据
			datas.add(bean);
			
		}
		return datas;
	}
}
