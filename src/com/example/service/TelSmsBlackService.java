package com.example.service;


import com.example.dao.BlackNumberDao;
import com.example.domain.BlackTable;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.SmsMessage;

public class TelSmsBlackService extends Service {
	private BlackNumberDao dao;
	private SmsReceiver receiver;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	private class SmsReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			Object[] datas = (Object[]) intent.getExtras().get("pdus");
			for (Object sms : datas) {
				//获取短信的数据
				SmsMessage sm = SmsMessage.createFromPdu((byte[]) sms);
				
				//取短信的发件人号码
				String address = sm.getOriginatingAddress();
				
				//判断是否存在黑名单中
				int mode = dao.getMode(address);
				
				//短信拦截
				if ((mode & BlackTable.SMS) != 0) {
					//具有短信拦截
					// if (mode == 短信 || mode == 全部)
					//拦截此短信
					abortBroadcast();//终止广播传递
				}
			}
		}
		
	}
	
	@Override
	public void onCreate() {
		
		//初始化黑名单的业务类
		dao = new BlackNumberDao(getApplicationContext());
		
		receiver = new SmsReceiver();
		//短信广播意图
		IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
		//设置拦截模式为最高
		filter.setPriority(Integer.MAX_VALUE);
		//注册短信广播
		registerReceiver(receiver, filter);
		
		// 注册电话监听
		super.onCreate();
	}
	
	@Override
	public void onDestroy() {
		
		//取消电话监听
		
		//取消短信监听
		unregisterReceiver(receiver);
		super.onDestroy();
	}

}
