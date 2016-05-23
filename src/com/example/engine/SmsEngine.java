package com.example.engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.concurrent.CountDownLatch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.utils.EncryptTools;
import com.example.utils.JsonStrTools;

import android.R.integer;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;

public class SmsEngine {

	/*
	 * 接口回调，这里的回调方法主要是用于监听短信备 份的进度的。短信备份是一个耗时的过程，为了让用户体验更好，一般都在界
	 * 面上显示备份的进度，这时就需要知道短信备份的进度
	 */
	public interface SmsCallBack {
		// 显示回调的进度
		void show();

		// 显示回调的最大值
		void setMax(int max);

		// 显示当前进度
		void setProgress(int progress);

		// 进度完成的回调
		void end();
	}

	private static class Data {
		int progress;
	}

	/**
	 * 通过子线程来做短信的还原json格式
	 * 
	 * @param context
	 * @param pd
	 *            通过接口回调备份的数据（所有回调方法都在主线程中执行）
	 */
	public static void smsResumn(final Activity context,
			final SmsCallBack smsCallBack) {
		final Data data = new Data();
		new Thread() {
			public void run() {
				Uri uri = Uri.parse("content://sms");
			
				try {
					// 2,获取备份的短信
					FileInputStream fis = new FileInputStream(new File(
							Environment.getExternalStorageDirectory(),
							"sms.json"));
					//json数据合并
					StringBuilder builder = new StringBuilder();
					// io流的封装 把字节流封装成缓冲的字符流
					BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
					String line = reader.readLine();
					if (line != null) {
						builder.append(line);
						line = reader.readLine();
					}
					//解析JSON数据
					JSONObject jsonObject = new JSONObject(builder.toString());
					//获取短信的个数
					final int count = Integer.parseInt(jsonObject.getString("count"));
					//设置回调结果的show()和setMax()方法
					context.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							smsCallBack.show();
							smsCallBack.setMax(count);
						}
					});
					//循环读取短信
					JSONArray jsonArray = (JSONArray) jsonObject.get("smses");
					for (int i = 0; i < count ;i++) {
						data.progress = i;
						//获取一条短信
						JSONObject smsjson = jsonArray.getJSONObject(i);
						
						ContentValues values = new ContentValues();
						values.put("address", smsjson.getString("address"));
						values.put("body", EncryptTools.decryption( smsjson.getString("body")));
						values.put("date", smsjson.getString("date"));
						values.put("type", smsjson.getString("type"));
						
						//往短信数据中加一条记录
						context.getContentResolver().insert(uri, values);
						
						//回调结果当前进度
						context.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								smsCallBack.setProgress(data.progress);
							}
						});
					}
					reader.close();// 关闭io流
					
					//回调备份完成的结果
					context.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							smsCallBack.end();
						}
					});
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				super.run();
			};
		}.start();
	}

	public static void smsBaikeJson(final Activity context,
			final SmsCallBack smsCallBack) {
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub

				Uri uri = Uri.parse("content://sms");
				// 获取短信记录游标
				final Cursor cursor = context.getContentResolver().query(uri,
						new String[] { "address", "date", "body", "type" },
						null, null, "_id desc");
				// 写到文件中去
				File file = new File(Environment.getExternalStorageDirectory(),
						"sms.json");
				try {
					FileOutputStream fos = new FileOutputStream(file);
					PrintWriter pw = new PrintWriter(file);
					context.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							smsCallBack.show();
							smsCallBack.setMax(cursor.getCount());// 设置进度条总进度

						}
					});

					final Data data = new Data();
					// 写根标记 {"count":"10"
					pw.println("{\"count\":\"" + cursor.getCount() + "\"");
					// ,"smses":[
					pw.println(",\"smses\":[");
					while (cursor.moveToNext()) {
						data.progress++;
						SystemClock.sleep(100);
						// 取短信
						if (cursor.getPosition() == 0) {
							pw.println("{");
						} else {
							pw.println(",{");
						}

						// address 封装 "address":"hello"
						pw.println("\"address\":\"" + cursor.getString(0)
								+ "\",");
						// date 封装
						pw.println("\"date\":\"" + cursor.getString(1) + "\",");
						// body 封装
						String mbody = EncryptTools.encrypt(JsonStrTools.changeStr(cursor.getString(2))) ;
						pw.println("\"body\":\"" + cursor.getString(2) + "\",");
						// type 封装
						pw.println("\"type\":\"" + cursor.getString(3) + "\"");

						pw.println("}");
						// 封装成xml标记

						context.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								smsCallBack.setProgress(data.progress);
							}
						});

					}

					context.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							smsCallBack.end();
						}
					});
					// 写根标记结束标记
					pw.println("]}");
					pw.flush();
					pw.close();
					cursor.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				super.run();
			}
		}.start();

	}

	/**
	 * 短信的备份
	 */
	public static void smsBaikeXml(Activity context,
			final SmsCallBack smsCallBack) {
		// 1,通过内容提供者获取到短信
		Uri uri = Uri.parse("content://sms");
		// 获取电话记录的联系人游标
		final Cursor cursor = context.getContentResolver().query(uri,
				new String[] { "address", "date", "body", "type" }, null, null,
				" _id desc");

		// 2,写到文件中
		File file = new File(Environment.getExternalStorageDirectory(),
				"sms.xml");

		try {
			FileOutputStream fos = new FileOutputStream(file);

			PrintWriter out = new PrintWriter(fos);
			context.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					smsCallBack.show();
					smsCallBack.setMax(cursor.getCount());// 设置进度条总进度

				}
			});

			final Data data = new Data();

			// 写根标记
			out.println("<smses count='" + cursor.getCount() + "'>");
			while (cursor.moveToNext()) {
				data.progress++;
				SystemClock.sleep(100);
				// 取短信
				out.println("<sms>");

				// address 封装
				out.println("<address>" + cursor.getString(0) + "</address>");
				// date 封装
				out.println("<date>" + cursor.getString(1) + "</date>");
				// body 封装
				out.println("<body>" + cursor.getString(2) + "</body>");
				// type 封装
				out.println("<type>" + cursor.getString(3) + "</type>");

				out.println("</sms>");
				// 封装成xml标记

				context.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						smsCallBack.setProgress(data.progress);
					}
				});

			}

			context.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					smsCallBack.end();
				}
			});
			// 写根标记结束标记
			out.println("</smses>");

			out.flush();
			out.close();// 关闭流
			cursor.close();// 关闭游标
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
