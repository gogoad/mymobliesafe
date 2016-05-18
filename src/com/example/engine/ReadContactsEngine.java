package com.example.engine;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

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
}
